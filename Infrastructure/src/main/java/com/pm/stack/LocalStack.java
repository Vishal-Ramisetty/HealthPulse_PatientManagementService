package com.pm.stack;

import software.amazon.awscdk.*;
import software.amazon.awscdk.services.ec2.Vpc;

public class LocalStack extends Stack {

    private static final String LocalStackId = "LocalStack12345678";
    private final Vpc vpc;

    public LocalStack(final App scope, final String id, final StackProps props){
        super(scope,id,props);
        this.vpc= createVpc();
    }

    private Vpc createVpc(){
        return Vpc.Builder.create(this, "MyLocalStackVpc")
                .maxAzs(2)
                .build();
    }

    public static void main(String[] args){
        App app=new App(AppProps.builder().outdir("./cdk.out").build());
        String id=LocalStackId;
        StackProps props = StackProps.builder()
                .synthesizer(new BootstraplessSynthesizer())
                .stackName(id)
                .build();

        new LocalStack(app,id,props);
        app.synth();
        System.out.println("LocalStack is created Successfully with ID: " + id);


    }
}
