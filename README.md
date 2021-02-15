# SCALE project BaT Authorisation Java microservce

This is the implementation of the BaT Oauth Service [Open API specification](https://github.com/Crown-Commercial-Service/ccs-scale-api-definitions/blob/master/bat/oauth.yaml).

It is deployed as part of the Terraform AWS environment provisioning scripts in [ccs-scale-infra-services-bat](https://github.com/Crown-Commercial-Service/ccs-scale-infra-services-bat).

CodeBuild project(s) exist in the Management account to build from source and deploy as a Docker image to ECR.
