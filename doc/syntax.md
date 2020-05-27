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

Predicates are used to validate a property value. The following predicates are currently supported.
Terms marked with `?` are optional, meaning that can be added only to improve readability of the specification.
For example, both `is > ip address "10.0.0.1"` and `> ip "10.0.0.1"` are the same predicates. Also, predicates
are case-sensitive, meaning that both `equal to` and `EqUaL tO` are valid.

**For any value:**

- `is? null`: value doesn't exist.
- `is? not null`: value exists.
- `is? == :value`: value is equal to another value.
- `is? equal to :value`: synonym of `== :value`.
- `is? != :value`: value is not equal to another value.
- `is? not equal to :value`: synonym of `!= :value`.
- `is? within [:value1, :value2...]`: value is in a list of values.
- `is? not within [:value1, :value2...]`: value is not in a list of values.

**For number values (i.e. integer or double):**

- `is? > :number`: number value is greater than another number.
- `is? greater than :number`: synonym of `> :number`
- `is? gt :number`: synonym of `> :number`
- `is? >= :number`:number value is greater than or equal to another number.
- `is? greater than or equal to :number`: synonym of `>= :number`
- `is? gte :number`: synonym of `>= :number`
- `is? < :number`: number value is less than another number.
- `is? less than :number`: synonym of `> :number`
- `is? lt :number`: synonym of `< :number`
- `is? <= :number`: number value is less than or equal to another number.
- `is? less than or equal to :number`: synonym of `<= :number`
- `is? lte :number`: synonym of `<= :number`
- `is? between :number and :number`: number value is between two numbers.

**For string values:**

- `is? empty`: synonym of `is equal to ""`.
- `is? not empty`: synonym of `is not equal to ""`.
- `is? starting with :string`: string value starts with another string.
- `is? not starting :string`: string value does not start with another string.
- `is? ending with :string`: string value ends with another string.
- `is? not ending with :string`: string value does not end with another string.
- `is? containing :string`: string value contains another string.
- `is? not containing :string`: string value does not contain another string.

**For string values representing an IP address**

- `is? > ip address? :ip_address`: ip address is greater than another ip address.
- `is? greater than ip address? :ip_address`: synonym of `> ip :ip_address`.
- `is? gt ip address? :ip_address`: synonym of `> ip :ip_address`.
- `is? >= ip address? :ip_address`: ip address is greater or equal than another ip address.
- `is? greater than or equal to ip address? :ip_address`: synonym of `>= ip :ip_address`.
- `is? gte ip address? :ip_address`: synonym of `>= ip :ip_address`.
- `is? < id address? :ip_address`: ip address is less than another ip address.
- `is? less than ip address? :ip_address`: synonym of `< ip :ip_address`.
- `is? lt ip address? :ip_address`: synonym of `> ip :ip_address`.
- `is? <= ip address? :ip_address`: ip address is less or equal than another ip address.
- `is? less than or equal to ip address? :ip_address`: synonym of `<= ip :ip_address`.
- `is? lte ip address? :ip_address`: synonym of `<= ip :ip_address`.
- `is? within network cidr? :cidr_block`: ip address is within a network.
- `is? not within network cidr? :cidr_block`: ip address is not within a network.
- `is? ipv4`: value is an IPv4.
- `is? ipv6`: value is an IPv6.

**For boolean properties:**

- `is? true`: synonym of `is equal to true`.
- `is? false`: synonym of `is equal to false`.
- `is? enabled`: synonym of `is equal to true`.
- `is? disabled`: synonym of `is equal to false`.

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