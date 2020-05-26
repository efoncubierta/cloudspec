# CloudSpec Syntax

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

## Spec clause

All `.cloudspec` files must start with a `Spec` declaration:

```
Spec :spec_description
...
```

Where `:spec_description` is a quoted `"string"` describing what the specification is about.

## Group clause

In each specification file there must be at least one `Group` clause.

```
...
(Group :group_description
   ...
)+
...
```

Where `:group_description` is a quoted `"string"` describing what the group is about.

## Rule clause

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

## With clause

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

## Assert clause

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

## Members paths

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
        my_property :predicate
        and my_other_property :predicate
        and > my_association (
            one_property :predicate
            and another (
                nested (
                    property :predicate
                )
            )
        )
        and > my_other_association (
            one_property :predicate
            and > another_association (
                property :predicate
            )
        )
    )
)
```

## Predicates

**For any value:**

- `== :value` or `EQUAL TO :value`: value is equal to another value.
- `!= :value` or `NOT EQUAL TO :value`: value is not equal to another value.
- `WITHIN [:value1, :value2...]`: value is in a list of values.
- `NOT WITHIN [:value1, :value2...]`: value is not in a list of values.

**For number values (i.e. integer or double):**

- `> :number` or `GREATER THAN :number` or `GT :number`: number value is greater than another number.
- `>= :number` or `GREATER THAN OR EQUAL TO :number` or `GTE :number`: number value is greater than or equal to another number.
- `< :number` or `LESS THAN :number` or `LT :number`: number value is less than another number.
- `<= :number` or `LESS THAN OR EQUAL TO :number` or `LTE :number`: number value is less than or equal to another number.
- `BETWEEN :number AND :number`: number value is between two numbers.

**For string values:**

- `STARTING WITH :string`: string value starts with another string.
- `NOT STARTING WITH :string`: string value does not start with another string.
- `ENDING WITH :string`: string value ends with another string.
- `NOT ENDING WITH :string`: string value does not end with another string.
- `CONTAINING :string`: string value contains another string.
- `NOT CONTAINING :string`: string value does not contain another string.

**For string values representing an IP address**

- `> IP :ip_address` or `GREATER THAN IP :ip_address` or `GT IP :ip_address`: ip address is greater than another ip address.
- `>= IP :ip_address` or `GREATER THAN OR EQUAL TO IP :ip_address` or `GTE IP :ip_address`: ip address is greater or equal than another ip address.
- `< IP :ip_address` or `LESS THAN IP :ip_address` or `LT IP :ip_address`: ip address is less than another ip address.
- `<= IP :ip_address` or `LESS THAN OR EQUAL TO IP :ip_address` or `LTE IP :ip_address`: ip address is less or equal than another ip address.
- `WITHIN NETWORK :cidr_block`: ip address is within a network.
- `NOT WITHIN NETWORK :cidr_block`: ip address is not within a network.
- `IPV4`: true if value is a IPv4.
- `IPV6`: true if value is a IPv6.

**For boolean properties:**

- `ENABLED`: synonym for `EQUAL TO true`.
- `DISABLED`: synonym for `EQUAL TO false`.

## Property values

A property can be of one of the following types:

- An integer (e.g `1`).
- A double (e.g. `3.1415`).
- A string (e.g. `"foo"`).
- A boolean (e.g. `true`).
- A key value (e.g. `key="foo", value="bar"`).
- A container for nested properties (e.g. `{prop1=1, prop2="foo"}`).

## Examples

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