[![Build Status](https://travis-ci.com/efoncubierta/cloudspec.svg?branch=master)](https://travis-ci.com/efoncubierta/cloudspec)
[![codecov](https://codecov.io/gh/efoncubierta/cloudspec/branch/master/graph/badge.svg)](https://codecov.io/gh/efoncubierta/cloudspec)
[![docker](https://img.shields.io/docker/v/efoncubierta/cloudspec?color=blue&label=docker&sort=semver)](https://hub.docker.com/r/efoncubierta/cloudspec)
[![license](https://img.shields.io/github/license/efoncubierta/cloudspec)](https://github.com/efoncubierta/cloudspec/blob/master/LICENSE)

# CloudSpec

CloudSpec is an open source tool for validating your resources in your cloud providers using a logical language that
everybody can understand. With its reasonably simple syntax, you can validate the configuration of your cloud resources,
avoiding mistakes that can lead to availability or confidentiality issues.

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
 
## Providers

CloudSpec itself does not support any resource. The core of CloudSpec is the syntax interpreter for the specification
files and its validation engine. However, CloudSpec does use providers, which are extensions to CloudSpec supporting
each different type of resource.

A provider defines the shape of each resource type, properties and associations, and the logic to load those resources.

You can find the available providers and resources they provide in the [CloudSpec Reference](/doc/index.md) documentation.

## CloudSpec Syntax

This is what a simple CloudSpec file looks like:

```
Spec "Production environment"

Group "S3 validations"
  Rule "Buckets must have access logs enabled"
  On aws:s3:bucket
  Assert
    access_logs is enabled

Group "EC2 validations"
  Rule "Instances must use 'gp2' volumes and be at least 50GiBs large."
    On aws:ec2:instance
    With
      tags["environment"] equal to "production"
    Assert
      devices (
        > volume (
            type equal to "gp2" and
            size gte 50
        )
      )
```

A CloudSpec file starts with a `Spec` declaration followed by `Group`'s and `Rule`'s declarations.

With `Rule`, you implement the validation logic for one kind of resource (e.g. S3 bucket):

```
Rule "My validation rule"
On aws:ec2:instance
...
```

Rules has titles to describe what the rule is about, for reporting purposes. The `On` clause defines the scope, or
type of resource this rule will apply to. A resource definition reference is made of three parts
PROVIDER:GROUP:RESOURCE_NAME. For example, `aws:s3:bucket`, where `aws` is the provider, `s3` is the group and `bucket`
the resource name. Resource definition references avoids naming collision on resources with the same name but different
providers.

For a complete list of supported providers and resources, check the [CloudSpec Reference](/doc/index.md) documentation.

When the scope (i.e. resource type) of the rule is defined, you can then filter out resources using `With` statements.
A `With` statement is used to narrow down the resource the rule is applied to, like `WHERE` clauses in SQL. `With`
statements are optional. If missing, all resources in the scope are validated.

```
Rule "My validation rule"
On aws:ec2:instance
With :member_path :predicate
...
```

A `:member_path` can be a property or an association. For example:

- Single property: `my_property :predicate`
- Nested property: `my_nested ( property :predicate )`
- Single property in an associated resource: `>my_association ( my_property :predicate )`

You can see all member paths currently supported in the [CloudSpec Syntax](/doc/syntax.md#members-paths) documentation.

A property can be of one of the following types:

- An integer (e.g `1`).
- A double (e.g. `3.1415`).
- A string (e.g. `"foo"`).
- A boolean (e.g. `true`).
- A date in ISO-8601 format (e.g. `"2020-05-27"`)
- A date time in ISO-8601 format (e.g. `"2020-05-27 00:00:00"`)
- A key value (e.g. `key="foo", value="bar"`).
- A container for nested properties (e.g. `{prop1=1, prop2="foo"}`).

Properties can also be multi-valued (e.g. `[1, 2, 3]` or `["foo", "bar"]`).

Predicates are used to validate a property value. For example.

```
my_property == 1
my_property is equal to 1
my_property is not equal to "foo"
my_property within [1, 2, 3]
my_property is not within ["foo", "bar"]
my_property equal to ip address "10.0.0.1"
my_property is within network "10.0.0.0/24"
my_property is enabled
```

You can see all predicates currently supported in the [CloudSpec Syntax](/doc/syntax.md#predicates) documentation.

You can concatenate multiple `With` clauses with `And`.

```
...
With my_property is equal to 1
And my_other_property is enabled
...
```
 
Finally, when you have the scope of your rule defined, you can then define the assertions.

The `Assert` clause is used to define assertions. An assertion validates whether a resource conforms or not to the rule.
An `Assert` clause has exactly the same syntax that `With` clauses, but starting with `Assert`.

```
...
Assert my_property is equel to 1
And my_other_property is disabled
...
```

The different between `With` and `Assert` is that the first is used to narrow down the scope, while the second is to
actually validate the resources in the scope.

You can find more information in the [CloudSpec Reference](/doc/index.md) documentation.

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