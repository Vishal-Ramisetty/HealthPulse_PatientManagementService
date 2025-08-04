#!/bin/bash
 # Above line is shebang to specify the shell script interpreter as bash
set -e # Stops the script if any command fails

# Pre-requisites:
  #1. LocalStack should be running on localhost:4566
  #2. AWS CLI should be configured to use LocalStack

# This command is used to delete the existing CloudFormation stack before deploying a new one.
aws --endpoint-url=http://localhost:4566 cloudformation delete-stack \
    --stack-name patient-management

# Deploy a new Stack in AWS using the cloudformation template
aws --endpoint-url=http://localhost:4566 cloudformation deploy \
    --stack-name patient-management \
    --template-file "./cdk.out/LocalStack12345678.template.json"

#Get the DNS name of the application load balance from output console to test the application
aws --endpoint-url=http://localhost:4566 elbv2 describe-load-balancers \
    --query "LoadBalancers[0].DNSName" --output text