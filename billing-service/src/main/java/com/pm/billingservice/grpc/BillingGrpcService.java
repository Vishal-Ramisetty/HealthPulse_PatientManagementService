package com.pm.billingservice.grpc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.billing.BillingRequest;
import com.billing.BillingResponse;
import com.billing.BillingServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {


    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    // Implement the gRPC methods here
    // For example:
     @Override
     public void createBillingAccount(BillingRequest request, StreamObserver<BillingResponse> responseObserver) {

         log.info("Received gRPC request for billing details for Billing Request: {}", request.toString());

         // Business Logic to handle the request from a client and generate a response/ do appropriate processing to Database
            BillingResponse response = BillingResponse.newBuilder()
                    .setAccountId("Billing-Service: UUID@12345")
                    .setStatus("AVAILABLE")
                    .build();

            // Send the response back to the client
            responseObserver.onNext(response);
            responseObserver.onCompleted();
     }
}
