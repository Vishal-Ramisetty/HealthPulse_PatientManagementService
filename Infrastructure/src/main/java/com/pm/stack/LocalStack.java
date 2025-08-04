package com.pm.stack;

import software.amazon.awscdk.*;
import software.amazon.awscdk.services.ec2.InstanceClass;
import software.amazon.awscdk.services.ec2.InstanceSize;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ecs.*;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.logs.LogGroup;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.amazon.awscdk.services.msk.CfnCluster;
import software.amazon.awscdk.services.rds.*;
import software.amazon.awscdk.services.route53.CfnHealthCheck;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LocalStack extends Stack {

    private static final String LocalStackId = "LocalStack12345678";
    private final Vpc vpc;
    private final Cluster ecsCluster;

    public LocalStack(final App scope, final String id, final StackProps props) {
        super(scope, id, props);
        this.vpc = createVpc();
        DatabaseInstance authServiceDb = createDatabase("AuthServiceDb", "auth-service-postgres-db");
        DatabaseInstance patientServiceDb = createDatabase("PatientServiceDb", "patient-service-db");
        CfnHealthCheck authServiceDbHealthCheck = creatDbHealthCheck(authServiceDb, "AuthServiceDbHealthCheck");
        CfnHealthCheck patientServiceDbHealthCheck = creatDbHealthCheck(patientServiceDb, "PatientServiceDbHealthCheck");
        CfnCluster mskCluster = createMskCluster("MyLocalStackMskCluster", "localstack-msk-cluster");
        this.ecsCluster = createEcsCluster();

        FargateService authService = createFargateService("AuthService", "auth-service-2:latest",
                List.of(4005), authServiceDb, Map.of("JWT_SECRET", "bf1f5267b3cf085058171f67ad8916e7c10b8e9ccb12cb8875a551e0f0c36c95"));

        authService.getNode().addDependency(authServiceDb);
        authService.getNode().addDependency(authServiceDbHealthCheck);

        FargateService billingService = createFargateService("BillingService", "billing-service:latest",
                List.of(4001, 9001), null, null);

        FargateService analyticsService = createFargateService("AnalyticsService", "analytics-service:latest",
                List.of(4002), null, null);
        analyticsService.getNode().addDependency(mskCluster);

        FargateService patientService = createFargateService("PatientService", "patient-service",
                List.of(4000), patientServiceDb,
                Map.of("BILLING_SERVICE_GRPC_PORT", "9001",
                        "BILLING_SERVICE_ADDRESS", "host.docker.internal"));

        patientService.getNode().addDependency(patientServiceDbHealthCheck);
        patientService.getNode().addDependency(patientServiceDb);
        patientService.getNode().addDependency(mskCluster);
        patientService.getNode().addDependency(billingService);


        createApiGatewayService();
    }

    private Vpc createVpc() {
        return Vpc.Builder.create(this, "MyLocalStackVpc")
                .maxAzs(2)
                .build();
    }

    private DatabaseInstance createDatabase(String id, String dbName) {
        return DatabaseInstance.Builder.create(this, id)
                .engine(DatabaseInstanceEngine.postgres(PostgresInstanceEngineProps
                        .builder().version(PostgresEngineVersion.VER_11_22).build()))
                .maxAllocatedStorage(20)
                .instanceType(InstanceType.of(InstanceClass.BURSTABLE2, InstanceSize.MICRO))
                .vpc(this.vpc)
                .databaseName(dbName)
                // To use it only for development and testing purposes else replace with RemovalPolicy.RETAIN
                .removalPolicy(RemovalPolicy.DESTROY)
                .credentials(Credentials.fromGeneratedSecret("admin_user"))
                .build();
    }

    private CfnHealthCheck creatDbHealthCheck(DatabaseInstance dbName, String dbId) {
        return CfnHealthCheck.Builder.create(this, dbId)
                .healthCheckConfig(CfnHealthCheck.HealthCheckConfigProperty.builder()
                        .type("TCP")
                        .resourcePath("/health")
                        .port(Token.asNumber(dbName.getDbInstanceEndpointPort()))
                        .ipAddress(dbName.getDbInstanceEndpointAddress())
                        .failureThreshold(3)
                        .requestInterval(30)
                        .build())
                .build();
    }

    private CfnCluster createMskCluster(String id, String clusterName) {
        return CfnCluster.Builder.create(this, id)
                .clusterName(clusterName)
                .kafkaVersion("2.8.0")
                .numberOfBrokerNodes(1)
                .brokerNodeGroupInfo(CfnCluster.BrokerNodeGroupInfoProperty.builder()
                        .instanceType("kafka.m5.large")
                        .brokerAzDistribution("DEFAULT")
                        .clientSubnets(vpc.getPrivateSubnets().stream()
                                .map(subnet -> subnet.getSubnetId())
                                .collect(Collectors.toList()))
                        .build())
                .build();
    }

    //
    private FargateService createFargateService(String id,
                                                String imageName, List<Integer> ports, DatabaseInstance db,
                                                Map<String, String> additionalEnvironmentVariables) {
        FargateTaskDefinition taskDefinition =
                FargateTaskDefinition.Builder.create(this, id + "Task")
                        .cpu(256)
                        .memoryLimitMiB(512)
                        .build();

        ContainerDefinitionOptions.Builder containerOptionsBuilder =
                ContainerDefinitionOptions.builder()
                        .image(ContainerImage.fromRegistry(imageName))
                        .portMappings(ports.stream()
                                .map(port -> PortMapping.builder()
                                        .containerPort(port)
                                        .hostPort(port)
                                        .protocol(Protocol.TCP)
                                        .build())
                                .toList())
                        .logging(LogDriver.awsLogs(AwsLogDriverProps.builder()
                                .logGroup(LogGroup.Builder.create(this, id + "LogGroup")
                                        .logGroupName("/ecs/" + imageName)
                                        .removalPolicy(RemovalPolicy.DESTROY)
                                        .retention(RetentionDays.FIVE_DAYS)
                                        .build())
                                .streamPrefix(imageName)
                                .build()));

        Map<String, String> envVars = new HashMap<>();
        envVars.put("SPRING_KAFKA_BOOTSTRAP_SERVERS", "localhost:localstack.cloud:4510, localhost:localstack.cloud:4511, localhost:localstack.cloud:4512");
        if (additionalEnvironmentVariables != null) {
            envVars.putAll(additionalEnvironmentVariables);
        }
        if (db != null) {
            envVars.put("SPRING_DATASOURCE_URL", "jdbc:postgresql://%s:%s:%s-db"
                    .formatted(db.getDbInstanceEndpointAddress()
                            , db.getDbInstanceEndpointPort(), imageName));
            envVars.put("SPRING_DATASOURCE_USERNAME", "admin_user");
            envVars.put("SPRING_DATASOURCE_PASSWORD", db.getSecret().secretValueFromJson("password")
                    .toString());
            envVars.put("SPRING_JPA_HIBERNATE_DDL_AUTO", "update");
            envVars.put("SPRING_SQL_INIT_MODE", "always");
            envVars.put("SPRING_DATASOURCE_HIKARI_INITIALIZATION_FAIL_TIMEOUT", "600000");
        }

        containerOptionsBuilder.environment(envVars);
        taskDefinition.addContainer(imageName + "Container"
                , containerOptionsBuilder.build());

        return FargateService.Builder.create(this, id)
                .cluster(ecsCluster)
                .taskDefinition(taskDefinition)
                .assignPublicIp(false)
                .serviceName(imageName)
                .build();
    }

    // This method is the entrypoint for the application
    private void createApiGatewayService() {

        FargateTaskDefinition apiGatewayTaskDefinition =
                FargateTaskDefinition.Builder.create(this, "ApiGatewayTask")
                        .cpu(256)
                        .memoryLimitMiB(512)
                        .build();

        ContainerDefinitionOptions containerOptions =
                ContainerDefinitionOptions.builder()
                        .image(ContainerImage.fromRegistry("api-gateway"))
                        .environment(Map.of(
                                "SPRING_PROFILES_ACTIVE", "prod",
                                "AUTH_SERVICE_URL", "http://host.docker.internal:4005"
                        ))
                        .portMappings(List.of(4004).stream()
                                .map(port -> PortMapping.builder()
                                        .containerPort(port)
                                        .hostPort(port)
                                        .protocol(Protocol.TCP)
                                        .build())
                                .toList())
                        .logging(LogDriver.awsLogs(AwsLogDriverProps.builder()
                                .logGroup(LogGroup.Builder.create(this, "ApiGatewayLogGroup")
                                        .logGroupName("/ecs/" + "api-gateway")
                                        .removalPolicy(RemovalPolicy.DESTROY)
                                        .retention(RetentionDays.FIVE_DAYS)
                                        .build())
                                .streamPrefix("api-gateway")
                                .build())).build();

        apiGatewayTaskDefinition.addContainer("ApiGatewayContainer"
                , containerOptions);

        ApplicationLoadBalancedFargateService apiGatewayService = ApplicationLoadBalancedFargateService.Builder.create(this, "APIGatewayService")
                .cluster(ecsCluster)
                .taskDefinition(apiGatewayTaskDefinition)
                .desiredCount(1)
                .serviceName("api-gateway")
                .healthCheckGracePeriod(Duration.seconds(600))
                .build();
    }


    // Example for Service Discovery with ECS Cluster
    // auth-service.patient-management.local , patient-service.patient-management.local, billing-service.patient-management.local
    private Cluster createEcsCluster() {
        return Cluster.Builder.create(this, "MyPatientManagementEcsCluster")
                .vpc(this.vpc)
                // This is used for service discovery in patient management system that has different microservices
                .defaultCloudMapNamespace(CloudMapNamespaceOptions.builder()
                        .name("patient-management.local").build())
                .build();
    }

    public static void main(String[] args) {
        App app = new App(AppProps.builder().outdir("./cdk.out").build());
        String id = LocalStackId;
        StackProps props = StackProps.builder()
                .synthesizer(new BootstraplessSynthesizer())
                .stackName(id)
                .build();

        new LocalStack(app, id, props);
        app.synth();
        System.out.println("LocalStack is created Successfully with ID: " + id);


    }
}
