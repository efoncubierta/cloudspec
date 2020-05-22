# CloudSpec Reference

_This documentation has been generated with the `cloudspec-codgen` tool._

**THIS PROJECT IS STILL IN ITS EARLY DAYS. PLEASE CHECK THIS REFERENCE DOCUMENT OFTEN.**

This is the reference documentation for CloudSpec. If you need an introduction to CloudSpec, read the
[README file](https://github.com/efoncubierta/cloudspec) at the root of this repository.

## Table of contents

1. [What is CloudSpec](#what-is-cloudspec)
2. [CloudSpec syntax](#cloudspec-syntax)
3. [CloudSpec providers](#cloudspec-providers)

## What is CloudSpec

CloudSpec is an open source tool for validating your resources in your cloud providers using a logical language that
everybody can understand. With its reasonably simple syntax, you can validate the configuration of your cloud resources,
avoiding mistakes that can lead to availability or confidentiality issues.

With CloudSpec you validate resources in your cloud provider. A resource can be anything, from an EC2 Instance to an SES
rule. Anything that a CloudSpec provider implements.

## CloudSpec Syntax

CloudSpec definition files are written in natural English language syntax. Validations of cloud resources are defined
with rules. Each rule defines a set of widths and assert statements for filtering and asserting the resources. Similar
rules can also be grouped. The full CloudSpec syntax is defined as follows. Please don't feel too overwhelmed. It is
simpler than you think.

```
# :comment
Spec :spec_description

(Group :group_description
    (Rule :rule_description
        On :resource_reference
        (With :member_path :predicate
            (And :member_path :predicate)*
        )?
        Assert :member_path :predicate
        (And :member_path :predicate)*
    )+
)+
```

### Spec clause

All `.cloudspec` files must start with a `Spec` declaration:

```
Spec :spec_description
...
```

Where `:spec_description` is a quoted `"string"` describing what the specification is about.

### Group clause

In each specification file there must be at least one `Group` clause.

```
...
(Group :group_description
   ...
)+
...
```

Where `:group_description` is a quoted `"string"` describing what the group is about.

### Rule clause

```
...
    (Rule :rule_description
        On :resource_reference
        ...
    )+
...
```

Where `:rule_description` is a quote `"string"` describing what the rule is for, and `:resource_reference` is the unique
resource definition reference (e.g. aws:ec2:instance).

The `:resource_reference` is used to define the scope of the rule to only one kind of resource.

### With clause

A `With` clause narrows down the scope of the rule. It is similar to the `WHERE` clause in SQL. `With` clause is
optional, but if the `With` clause is missing, the scope of the rule would be all resources of a kind.

```
...
        (With :member_path :predicate
            (And :member_path :predicate)*
        )?
...
```

Multiple `With` clauses can be concatenated with `And`.

### Assert clause

An `Assert` clause does the actual validation of the resources in the scope. Its syntax is similar to the `With`
clause, but its purpose is completely different. While the `With` clause is used to narrow down the scope, the `Assert`
clause validates whether a resource in the scope is properly configured. `Assert` clause is mandatory on each rule.

```
...
        Assert :member_path :predicate
        (And :member_path :predicate)*
...
```

Multiple `Assert` clauses can be concatenated with `And`.

### Members paths

A resource have two types of members: property and association. A property has a value, while an association is a
reference to another resource. Both `With` and `Assert` clauses works on properties and associations. A property
can also hold nested properties. So `:member_path` can have different shapes, but it must always end with a property
as it is its value what gets validated against the predicate.

**Single property**

`my_property :predicate`

**Nested property**

```
my (
    nested (
        property :predicate
    )
)
```

**Properties in an associated resource**

```
> my_association (
    my_property :predicate
)
```

**Nested property in an associated resource**

```
> my_association (
    my (
        nested (
            property :predicate
        )
    )
)
```

**Property in an associated resource in an associated resource**

```
> my_association (
    > another_association (
        my_property :predicate
    )
)
```

**Property in a associated resource in a nested property**

```
my (
    nested (
        > association (
            my_property :predicate
        )
    )
)
```

**Complex nested properties with associations**

```
my (
    nested (
        my_property :predicate,
        my_other_property :predicate,
        > my_association (
            one_property :predicate,
            another (
                nested (
                    property :predicate
                )
            )
        ),
        > my_other_association (
            one_property :predicate,
            > another_association (
                property :predicate
            )
        )
    )
)
```

### Predicates

- `== :value`: property value is equal to a value.
- `EQUAL TO :value`: same equal predicate but using words.
- `!= :value`: property value is not equal to a value.
- `NOT EQUAL TO :value`: same not equal predicate but using words.
- `WITHIN [:value1, :value2...]`: property value is in a list of values.
- `NOT WITHIN [:value1, :value2...]`: property value is not in a list of values.
- `ENABLED`: synonym for `== true`.
- `DISABLED`: synonym for `== false`.

### Property values

A property can be of one of the following types:

- An integer (e.g `1`).
- A double (e.g. `3.1415`).
- A string (e.g. `"foo"`).
- A boolean (e.g. `true`).
- A key value (e.g. `key="foo", value="bar"`).
- A container for nested properties (e.g. `{prop1=1, prop2="foo"}`).

### Examples

```
Spec "Production environment"

Group "S3 validations"
  Rule "S3 buckets must have access logs and versioning enabled"
    On aws:s3:bucket
    Assert
      access_logs_enabled is true
    And
      versioning_enabled is true
```

## CloudSpec providers

CloudSpec supports the following providers:

<#list providers as provider>
* [${provider.name}](${provider.name}): ${provider.description}
</#list>
