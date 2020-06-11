# CloudSpec Resource Definition: aws:ec2:reserved_instances


## Properties

* **availability_zone**
(`string`):
The Availability Zone in which the Reserved Instance can be used.
* **currency_code**
(`string`):
The currency of the Reserved Instance.
Example values: `USD`
* **duration**
(`number`):
The duration of the Reserved Instance, in seconds.
* **end**
(`date`):
The time when the Reserved Instance expires.
* **fixed_price**
(`number`):
The purchase price of the Reserved Instance.
* **instance_count**
(`number`):
The number of reservations purchased.
* **instance_tenancy**
(`string`):
The tenancy of the instance.
Example values: `default | dedicated | host`
* **instance_type**
(`string`):
The instance type on which the Reserved Instance can be used.
* **offering_class**
(`string`):
The offering class of the Reserved Instance.
Example values: `standard | convertible`
* **offering_type**
(`string`):
The Reserved Instance offering type.
Example values: `Heavy Utilization | Medium Utilization | Light Utilization | No Upfront | Partial Upfront | All Upfront`
* **recurring_charges**
(`nested[]`):
The recurring charge tag assigned to the resource.
    * **amount**
(`number`):
The amount of the recurring charge.
    * **frequency**
(`string`):
The frequency of the recurring charge.
Example values: `Hourly`
* **region**
(`string`):
The AWS region.
Example values: `us-east-1 | eu-west-1`
* **reserved_instances_id**
(`string`):
The ID of the Reserved Instance.
* **scope**
(`string`):
The scope of the Reserved Instance.
* **start**
(`date`):
The date and time the Reserved Instance started.
* **state**
(`string`):
The state of the Reserved Instance purchase.
Example values: `payment-pending | active | payment-failed | retired | queued | queue-deleted`
* **tags**
(`key_value[]`):
Any tags assigned to the resource.
* **usage_price**
(`number`):
The usage price of the Reserved Instance, per hour.

