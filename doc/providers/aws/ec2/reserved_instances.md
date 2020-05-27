# CloudSpec Resource Definition: aws:ec2:reserved_instances


## Properties

* **id**
(`string`):
The ID of the Reserved Instance.
Example values: `f127bd27-edb7-44c9-a0eb-0d7e09259af0`
* **region**
(`string`):
The AWS region.
Example values: `us-east-1, eu-west-1`
* **availability_zone**
(`string`):
The AWS availability zone.
Example values: `us-east-1a, eu-west-1a`
* **scope**
(`string`):
The scope of the Reserved Instance.
Example values: `Availability Zone | Region`
* **start**
(`date`):
The date and time the Reserved Instance started.
* **end**
(`date`):
The time when the Reserved Instance expires.
* **fixed_price**
(`double`):
The purchase price of the Reserved Instance.
* **instance_count**
(`integer`):
The number of reservations purchased.
* **instance_tenancy**
(`string`):
The tenancy of the instance.
* **instance_type**
(`string`):
The instance type on which the Reserved Instance can be used.
Example values: `t2.nano | m5.xlarge`
* **offering_class**
(`string`):
The offering class of the Reserved Instance.
Example values: `standard | convertible`
* **offering_type**
(`string`):
The Reserved Instance offering type.
Example values: `Heavy Utilization | Medium Utilization...`
* **product_description**
(`string`):
The Reserved Instance product platform description.
Example values: `Linux/UNIX | Linux/UNIX (Amazon VPC) | Windows...`
* **usage_price**
(`double`):
The Reserved Instance product platform description.
* **state**
(`string`):
The snapshot state.
Example values: `payment-pending | active | payment-failed | retired | queued | queued-deleted`
* **tags**
(`key_value[]`):
Any tags assigned to the Reserve Instance..

