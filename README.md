[![Build Status](https://travis-ci.com/efoncubierta/cloudspec.svg?branch=master)](https://travis-ci.com/efoncubierta/cloudspec)
[![codecov](https://codecov.io/gh/efoncubierta/cloudspec/branch/master/graph/badge.svg)](https://codecov.io/gh/efoncubierta/cloudspec)
[![docker](https://img.shields.io/docker/v/efoncubierta/cloudspec?color=blue&label=docker&sort=semver)](https://hub.docker.com/r/efoncubierta/cloudspec)
[![license](https://img.shields.io/github/license/efoncubierta/cloudspec)](https://github.com/efoncubierta/cloudspec/blob/master/LICENSE)

![CloudSpec Logo](/doc/images/logo.png)

# CloudSpec

CloudSpec is an open source tool for validating your resources in your cloud providers using a logical language that
everybody can understand. With its reasonably simple syntax, you can validate the configuration of your cloud resources,
avoiding mistakes that can lead to availability or confidentiality issues.

<p align="center"><img src="/doc/images/demo.gif?raw=true"/></p>

## Introduction

With CloudSpec you validate resources in your cloud provider. A resource can be anything, from an EC2 Instance to an SES
rule. Anything that a CloudSpec provider implements.

Resources have properties and associations. Properties define the shape, or configuration, of the resource, while
associations define its relationships with other resources. With CloudSpec, you not only can validate the configuration
of the resource, but also the configuration of its associated resources. For example, let's take an EC2 Instance. It has
properties defining its shape, like its unique instance ID, its name, its type, and the such. But it also has
associations like the subnet it belongs to, the EBS volumes attached to it, the AMI it uses, and the such. You not only
can validate whether an EC2 Instance is of a particular instance type, or has the delete termination flag enabled, but
also the size of its attached volumes, the CIDR block of its subnet, or any other property in its associated resources,
or associated resources to its associated resources, and so on. You follow me.

Your cloud resources are entangled together, creating a graph. A graph that you can traverse and validate as you see fit
according to your best practices or compliance policies. That ability, plus its logical language, is the beauty of
CloudSpec.

```
plan "Production environment"
    set aws:regions = ["us-east-1", "eu-west-1"]

    use module "my_module.csm"

    group "S3 validations"
        use rule "my_s3_rule.csr"

        rule "Buckets must have access logs enabled"
            on aws:s3:bucket
            assert access_logs is enabled
        end rule
    end group

    group "EC2 validations"
        use rule "my_ec2_rule.csr"

        rule "Instances must use 'gp2' volumes and be at least 50GiBs large."
            on aws:ec2:instance
            with tags["environment"] equal to "production"
            assert devices (
                > volume (
                    type equal to "gp2" and
                    size gte 50
                )
            )
        end rule
    end group
end plan
```

You can find the full syntax in the [CloudSpec Reference](https://cloudspec.pro/docs) documentation.
 
## Providers

CloudSpec itself does not support any resource. The core of CloudSpec is the syntax interpreter for the specification
files and its validation engine. However, CloudSpec does use providers, which are extensions to CloudSpec supporting
each different type of resource.

A provider defines the shape of each resource type, properties and associations, and the logic to load those resources.

You can find the available providers and resources they provide in the [CloudSpec Reference](https://cloudspec.pro/docs/)
 documentation.

## Running CloudSpec docker image

You can either build and run the CloudSpec jar yourself, or you can run the latest docker image straight from the
Docker Hub registry.

To use the Docker image, you first need to put your spec files (e.g. `specs`) in a directory to mount it in the Docker 
container. Otherwise, the CloudSpec will not be able to open the spec files outside the container. 

```$bash
export AWS_ACCESS_KEY_ID=***
export AWS_SECRET_ACCESS_KEY=***
export AWS_REGION=eu-west-1
docker run -v "/my/specs:/specs" -e AWS_ACCESS_KEY_ID -e AWS_SECRET_ACCESS_KEY -e AWS_REGION efoncubierta/cloudspec run -p specs/my.csplan
```

If you are running the docker container in AWS with a dedicated IAM role attached, you can omit the AWS environment 
variables.

For more options of the CloudSpec command, see help:

```$bash
docker run efoncubierta/cloudspec -h
```

## Build CloudSpec

If you want to build CloudSpec yourself, follow these instructions.

Requirements:

- Git
- Maven 3
- OpenJDK 8
- Docker

Pull the source code and build CloudSpec:

```$bash
# Clone git repo
git clone https://github.com/efoncubierta/cloudspec
cd cloudspec
# Build CloudSpec
mvn clean install
# Run CloudSpec
java -jar runner/target/cloudspec-${VERSION}.jar -h
```

## Collaborations

For the time being, this is a pet project and, therefore, only some resources in AWS are supported, but I am open to
collaborations. Feel free to fork this project and send pull requests.