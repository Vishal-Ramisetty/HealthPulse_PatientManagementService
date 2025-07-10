package com.pm.patientservice.grpc;

import com.billing.BillingRequest;
import com.billing.BillingResponse;
import com.billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillingServiceGrpcClient {

    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);
    //This is the gRPC client stub that will be used to communicate with the Billing Service (Request and wait for Response from Billing Service)
    private final BillingServiceGrpc.BillingServiceBlockingStub billingServiceBlockingStub;


    // Example for Docker Container Environment Prop
    // localhost:9001/BillingService/CreatePatientAccount
    // aws:grpc:123123/BillingService/CreatePatientAccount
    public BillingServiceGrpcClient(@Value("${billing.service.address:localhost}") String serverAddress,
                                    @Value("${billing.service.grpc.port:9001}") int serverPort) {

        log.info("Connecting to Billing Service gRPC server at {}:{}", serverAddress, serverPort);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort)
                .usePlaintext() // Use plaintext for simplicity; consider using TLS in production
                .build();
        this.billingServiceBlockingStub = BillingServiceGrpc.newBlockingStub(channel);

    }
//    public BillingServiceGrpcClient(BillingServiceGrpc.BillingServiceBlockingStub billingServiceBlockingStub) {
//        this.billingServiceBlockingStub = billingServiceBlockingStub;
//    }

    public BillingResponse createBillingAccount(String patientId, String patientName, String patientEmail) {
        log.info("Creating billing account for patient: {} with ID: {}", patientName, patientId);
        // Create a request object
        BillingRequest request = BillingRequest.newBuilder()
                .setPatientId(patientId)
                .setName(patientName)
                .setEmail(patientEmail)
                .build();

        // Call the gRPC service and get the response
        BillingResponse response = billingServiceBlockingStub.createBillingAccount(request);
        log.info("Received response from Billing Service via GRPC: {}", response);
        return response;
    }

}
