# CloudSpec Syntax

At a high-level, it all starts with a `plan`. A `plan` contains one or more `module`, `group` or `rule`. Each `module` 
contains one or more `group` or `rule`. Each `group` contains one or more validation `rule`. `plan` and `module` can 
define `input` variables. `module`, `group` and `rule` can be imported from other files (i.e. `use (module|group|rule)`)
,or defined in-line. `plan`, `module` and `group` can `set` configuration parameters. `rule` defines a validation scope 
using `on` and `with`. `rule` defines validations using `assert`.

All keywords are case-insensitive (e.g. both `plan` and `PLAN` are valid).

## `plan` declaration

To validate your resources you need one `plan`, and only one `plan`. Everything in CloudSpec is declared within a 
`plan`. A `plan` is declared in a file with `.csp` extension. 

```
plan :plan_name
    (input ...)*
    (set ...)*
    (use module|group|rule ...)*
    (group ...)*
    (rule ...)*
end plan
```

Where `:plan_name` is a quoted `"string"`. For example, `"My plan"`.

Within a `plan` you can declare the following:

- Zero or more `input`: an input variable (see: [input declaration](#input-declaration))
- Zero or more `set`: set a global configuration (see: [set declaration](#set-declaration))
- Zero or more `use module|group|rule`: import a module, group or rule from a file (see: [use declaration](#use-declaration))
- Zero or more `group`: a group of rules (see: [group declaration](#group-declaration))
- Zero or more `rule`: a validation rule (see: [rule declaration](#rule-declaration))

A plan must have at least one `rule` declared either in-line, within a group, within a module or imported.

## `module` declaration

Modules are like plan, but reusable. Different plans can import the same module. A `module` is declared in a file
with the `.csm` extension.

```
module :module_name
    (input ...)*
    (set ...)*
    (use module|group|rule ...)*
    (group ...)*
    (rule ...)*
end module
```

Where `:module_name` is a quoted `"string"`. For example, `"My module"`.

Within a `module` you can declare the following:

- Zero or more `input`: an input variable (see: [input declaration](#input-declaration))
- Zero or more `set`: set a global configuration (see: [set declaration](#set-declaration))
- Zero or more `use module|group|rule`: import a module|group|rule from a file (see: [use declaration](#use-declaration))
- Zero or more `group`: a group of rules (see: [group declaration](#group-declaration))
- Zero or more `rule`: a validation rule (see: [rule declaration](#rule-declaration))

A module must have at least one `rule` declared either in-line, within a group, within another module or imported.

## `group` declaration

Rules that are alike or share the same configuration parameters can be grouped. A `group` can be declared in-line or its
own file with the `.csg` extension.

```
group :group_name
    (set ...)*
    (use rule ...)*
    (rule ...)*
end group
```

Where `:module_name` is a quoted `"string"`. For example, `"My group"`.

Within a `group` you can declare the following:

- Zero or more `set`: set a global configuration (see: [set declaration](#set-declaration))
- Zero or more `use rule`: import a rule from a file (see: [use declaration](#use-declaration))
- Zero or more `rule`: a validation rule (see: [rule declaration](#rule-declaration))

A group must have at least one `rule` declared either in-line or imported.

## `rule` declaration

Validation rules are the essential ingredient of CloudSpec. At least one rule must exist in a `plan`, `module` or
`group`. Without rules there are not validations. A `rule` can be declared in-line or its own file with the `.csr`
extension.

```
rule :rule_name
    on :resource_reference
    (with ...)?
    assert ...
end rule
```

Where `:rule_name` is a quoted `"string"`. for example `"My rule"`, and `:resource_reference` is a unique
resource definition reference (e.g. aws:ec2:instance).

Rules are defined within a scope. The scope is the set of resources that will be validated. A scope if defined as
follows:

- The `on` statement selects all resources of a kind. For example, all EC2 instances.
- The `with` statements narrow down the selected resources. For example, all EC2 instances of type m5.large.

`with` statements are optional. If absent, all resources of a kind will be validated. At least one `assert` must be
declared on each rule.

Within a `rule` you can declare the following:

- Zero or more `with`: filters to narrow down the scope (see: [with declaration](#with-declaration))
- Zero or more `assert`: validations (see: [assert declaration](#assert-declaration))

## `with` declaration

A `with` statement narrows down the scope of the rule. It is similar in essence to the `WHERE` clause in SQL.

```
with :member_path :predicate
(and :member_path :predicate)*
```

Where `:member_path` (see [member paths](#members-paths)) is a path to a property in the resource, and `:predicate` 
(see [predicates](#predicates)) is the actual validation upon that property. If all `with` statements were true on a
resource, the resource would be added to the validation scope.

Multiple `with` declarations can be concatenated with `and`.

## `assert` declaration

An `assert` statement does the actual validation of the resources in the scope. Its syntax is similar to `with`, but its
purpose is completely different. While `with` narrows down the validation scope, `assert` validates that the resources 
in the validation scope are correctly configured, or it will produce an error.

```
assert :member_path :predicate
(and :member_path :predicate)*
```

Where `:member_path` (see [member paths](#members-paths)) is a path to a property in the resource, and `:predicate` 
(see [predicates](#predicates)) is the actual validation upon that property. If any `assert` statement was true on a
resource, an error would be produced.

Multiple `assert` declarations can be concatenated with `and`.

## Members paths

A resource have two types of members: property and association. A property has a value, while an association is a
reference to another resource. Both `with` and `assert` statements works on properties and associations. A property
can also hold nested properties. So `:member_path` can have different shapes, but it must always end with a property
as it is its value what gets validated against the predicate.

**Single property**

`my_property :predicate`

**Key-Value property**

`my_property["my_key"] :predicate`

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

**Property in an associated resource in a nested property**

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
            one_property["key"] :predicate
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

Predicates validate a property value. CloudSpec currently support the following predicates.
Terms marked with `?` are optional, and can be used improve readability making it feel more natural.
For example, both `is > ip address "10.0.0.1"` and `> ip "10.0.0.1"` are the same predicates.

**For any value:**

- `is? null`: value doesn't exist.
- `is? not null`: value exists.
- `is? == :value`: value is equal to another value.
- `is? equal to :value`: synonym of `== :value`.
- `is? != :value`: value is not equal to another value.
- `is? not equal to :value`: synonym of `!= :value`.
- `is? within [:value1, :value2...]`: value is within a list of values.
- `is? not within [:value1, :value2...]`: value is not within a list of values.

**For numeric values:**

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

**For date properties:**

- `is? before :date`: date value is before another date.
- `is? not before :date`: date value is not before another date.
- `is? after :date`: date value is after another date.
- `is? not after :date`: date value is not after another date.

## Property values

A property can be of one of the following types:

- An integer (e.g `1`).
- A double (e.g. `3.1415`).
- A string (e.g. `"foo"`).
- A boolean (e.g. `true`).
- A date in ISO-8601 format (e.g. `"2020-05-27"`)
- A date time in ISO-8601 format (e.g. `"2020-05-27 00:00:00"`)
- A key value (e.g. `key="foo", value="bar"`).
- A container for nested properties (e.g. `{prop1=1, prop2="foo"}`).

## `use` declaration

TBC

```
use (module|group|rule) :file_path
```

## `input` declaration

*Not yet supported.*

```
input :input_name as :input_def
```

## `set` declaration

TBC

```
set :config_ref = :value
```

## Examples

```
plan "Production environment"

group "S3 validations"
  rule "Buckets must have access logs enabled"
  on aws:s3:bucket
  assert access_logs is enabled

group "EC2 validations"
  rule "Instances must use 'gp2' volumes and be at least 50GiBs large."
    on aws:ec2:instance
    with tags["environment"] equal to "production"
    assert devices (
        > volume (
            type equal to "gp2" and
            size gte 50
        )
    )
```