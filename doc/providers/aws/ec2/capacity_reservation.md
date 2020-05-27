# CloudSpec Resource Definition: aws:ec2:capacity_reservation


## Properties

* **id**
(`string`):
The ID of the Capacity Reservation.
Example values: `snap-1234567890abcdef0`
* **region**
(`string`):
The AWS region.
Example values: `us-east-1 | eu-west-1 ...`
* **availability_zone**
(`string`):
The AWS availability zone.
Example values: `us-east-1a | eu-west-1a ...`
* **available_instance_count**
(`integer`):
Indicates the number of instances that can be launched in the Capacity Reservation.
* **total_instance_count**
(`integer`):
The total number of instances for which the Capacity Reservation reserves capacity.
* **ebs_optimized**
(`boolean`):
Indicates whether the Capacity Reservation supports EBS-optimized instances.
* **support_ephemeral_storage**
(`boolean`):
Indicates whether the Capacity Reservation supports instances with temporary, block-level storage.
* **end_date**
(`date`):
The date and time at which the Capacity Reservation expires.
* **end_date_type**
(`string`):
Indicates the way in which the Capacity Reservation ends.
Example values: `unlimited | limited`
* **instance_platform**
(`string`):
The type of operating system for which the Capacity Reservation reserves capacity.
Example values: `Linux/UNIX | Red Hat Enterprise Linux | Windows ...`
* **instance_type**
(`string`):
The type of instance for which the Capacity Reservation reserves capacity.
Example values: `t2.nano | m5.xlarge ...`
* **tenancy**
(`string`):
Indicates the tenancy of the Capacity Reservation.
Example values: `default | dedicated ...`
* **state**
(`string`):
The Capacity Reservation state.
Example values: `active | expired | cancelled | pending | failed`
* **tags**
(`key_value[]`):
Any tags assigned to the Capacity Reservation.

