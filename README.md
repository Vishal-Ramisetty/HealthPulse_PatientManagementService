Setup Guide
This Project contains following services and folders:

**Project Structure:**

.
├── .idea
│   ├── dataSources
│   ├── httpRequests
│   ├── runConfigurations
│   └── shelf
├── analytics-service
│   ├── .mvn
│   │   └── wrapper
│   ├── auth-service
│   │   └── classes  (Note: this is under target originally, but we’ve excluded `target`—so skip if strictly excluding everything under target)
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── pm
│       │   │           └── analyticsservice
│       │   │               └── kafka
│       │   ├── proto
│       │   └── resources
│       │       ├── static
│       │       └── templates
│       └── test
│           └── java
│               └── com
│                   └── pm
│                       └── analyticsservice
├── api-gateway
│   ├── .mvn
│   │   └── wrapper
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── pm
│       │   │           └── apigateway
│       │   │               ├── exception
│       │   │               └── filter
│       │   └── resources
│       └── test
│           └── java
│               └── com
│                   └── pm
│                       └── apigateway
├── api-requests
│   ├── api_gateway-router
│   ├── auth-service
│   └── patient-service
├── auth-service
│   ├── .mvn
│   │   └── wrapper
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── pm
│       │   │           └── authservice
│       │   │               ├── contoller
│       │   │               ├── model
│       │   │               ├── nosqlInjection
│       │   │               └── repository
│       │   └── resources
│       └── test
│           └── java
│               └── com
│                   └── pm
│                       └── authservice
├── auth-service-2dot0
│   ├── .mvn
│   │   └── wrapper
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── pm
│           │           └── authservice2dot0
│           │               ├── config
│           │               ├── controller
│           │               ├── dto
│           │               ├── model
│           │               ├── repository
│           │               ├── service
│           │               └── utils
│           └── resources
├── billing-service
│   ├── .mvn
│   │   └── wrapper
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── pm
│       │   │           └── billingservice
│       │   │               └── grpc
│       │   ├── proto
│       │   └── resources
│       │       ├── static
│       │       └── templates
│       └── test
│           └── java
│               └── com
│                   └── pm
│                       └── billingservice
├── cdk.out
├── grpc-requests
│   └── billing-service
├── Infrastructure
│   ├── .mvn
│   ├── cdk.out
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── pm
│       │   │           └── stack
│       │   └── resources
│       └── test
│           └── java
│               └── com
│                   └── pm
├── integration-tests
│   ├── .mvn
│   └── src
│       └── test
│           └── java
├── Microservices-Run Config and Architecture Diagrams
│   └── Container Environment Config
├── out
│   └── production
├── patient-service
│   ├── .mvn
│   │   └── wrapper
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── pm
│       │   │           └── patientservice
│       │   │               ├── controller
│       │   │               ├── dto
│       │   │               │   └── validators
│       │   │               ├── exception
│       │   │               ├── grpc
│       │   │               ├── kafka
│       │   │               ├── mapper
│       │   │               ├── model
│       │   │               ├── repository
│       │   │               └── service
│       │   ├── proto
│       │   └── resources
│       │       ├── static
│       │       └── templates
│       └── test
│           └── java
│               └── com
│                   └── pm
│                       └── patientservice



To Do:

-> Documentation Using Swagger and API 
-> Provide Microservice Architecture HLSD
-> Explain About the App in a gist
-> List Down Services Used

api-server: HTTP API Server for REST API's
build-server: Docker Image code which clones, builds and pushes the build to S3
s3-reverse-proxy: Reverse Proxy the subdomains and domains to s3 bucket static assets
