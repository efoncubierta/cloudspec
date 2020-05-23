[![Build Status](https://travis-ci.com/efoncubierta/cloudspec.svg?branch=master)](https://travis-ci.com/efoncubierta/cloudspec)
[![codecov](https://codecov.io/gh/efoncubierta/cloudspec/branch/master/graph/badge.svg)](https://codecov.io/gh/efoncubierta/cloudspec)

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
Spec "Production account"

Group "S3 Buckets"
  Rule "All S3 buckets must have Access Logs enabled"
  On aws:s3:bucket
  Assert
    access_logs is enabled

  Rule "Images S3 bucket must have Versioning enabled"
  On aws:s3:bucket
  With
    name equal to "my-images-bucket"
  Assert
    versioning is enabled
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
With MEMBER_PATH PREDICATE
...
```

A member path can be a property or an association, and are defined as follows:

- Single property: `my_property $PREDICATE`
- Nested property: `my_nested.property $PREDICATE`
- Single property in an associated resource: `my_association ( my_property $PREDICATE )`
- Nested property in an associated resource: `my_association ( my_nested.property $PREDICATE )`
- Single property in an associated resource in an associated resource:
`my_association ( another_association ( my_property $PREDICATE) )`
- Single property in a associated resource in a nested property: `my_nested.association ( my_property $PREDICATE)`
- And so on and so forth.

A property can be of one of the following types:

- An integer (e.g `1`).
- A double (e.g. `3.1415`).
- A string (e.g. `"foo"`).
- A boolean (e.g. `true`).
- A key value (e.g. `key="foo", value="bar"`).
- A container for nested properties (e.g. `{prop1=1, prop2="foo"}`).

Properties can also be multi-valued (e.g. `[1, 2, 3]` or `["foo", "bar"]`).

Predicates are used to validate the shape of the property. The following predicates are supported:

- `== $VALUE`: property value is equal to a value.
- `EQUAL TO $VALUE`: same equal predicate but using words.
- `!= $VALUE`: property value is not equal to a value.
- `NOT EQUAL TO $VALUE`: same not equal predicate but using words.
- `WITHIN [$VALUE1, $VALUE2]`: property value is in a list of values.
- `NOT WITHIN [$VALUE1, $VALUE2]`: property value is not in a list of values.
- `ENABLED`: synonym for `== true`.
- `DISABLED`: synonym for `== false`.

These predicates are case-insensitive, and can be prefixed with `IS` to improve readability.

Some predicate examples:

```
my_property == 1
my_property IS EQUAL TO 1
my_property IS NOT EQUAL TO "foo"
my_property WITHIN [1, 2, 3]
my_property IS NOT WITHIN ["foo", "bar"]
my_property IS ENABLED
```

You can concatenate multiple `With` clauses with `And`.

```
...
With my_property IS EQUAL TO 1
And my_other_property IS DISABLED
...
```
 
Finally, when you have the scope of your rule defined, you can then define the assertions.

The `Assert` clause is used to define assertions. An assertion validates whether a resource conforms or not to the rule.
An `Assert` clause has exactly the same syntax that `With` clauses, but starting with `Assert`.

```
...
Assert my_property IS EQUAL TO 1
And my_other_property IS DISABLED
...
```

The different between `With` and `Assert` is that the first is used to narrow down the scope, while the second is to
actually validate the resources in the scope.

You can find more information in the [CloudSpec Reference](/doc/index.md) documentation.

## Build and run CloudSpec

Requirements:

- Maven 3 or higher
- OpenJDK 8 or higher

To build the executable jar, run the following command:

```$bash
mvn clean package
```

Run CloudSpec on your specification file:

```
java -jar runner/target/cloudspec-runner-0.0.1-SNAPSHOT-jar-with-dependencies.jar -s my.cloudspec
```

For more options, check the command's help:

```$bash
java -jar runner/target/cloudspec-runner-0.0.1-SNAPSHOT-jar-with-dependencies.jar -h
```

## Collaborations

For the time being, this is a pet project and, therefore, only some resources in AWS are supported, but I am open to
collaborations. Feel free to fork this project and send pull requests.