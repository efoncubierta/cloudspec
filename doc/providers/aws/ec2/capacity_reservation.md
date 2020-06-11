# CloudSpec Resource Definition: aws:ec2:capacity_reservation


## Properties

* **availability_zone**
(`string`):
The Availability Zone in which the capacity is reserved.
* **available_instance_count**
(`number`):
The remaining capacity. Indicates the number of instances that can be launched in the Capacity Reservation.
* **capacity_reservation_arn**
(`string`):
The Amazon Resource Name (ARN) of the Capacity Reservation.
* **capacity_reservation_id**
(`string`):
The ID of the Capacity Reservation.
* **create_date**
(`date`):
The date and time at which the Capacity Reservation was created.
* **ebs_optimized**
(`boolean`):
Indicates whether the Capacity Reservation supports EBS-optimized instances.
* **end_date**
(`date`):
The date and time at which the Capacity Reservation expires.
* **end_date_type**
(`string`):
Indicates the way in which the Capacity Reservation ends.
Example values: `unlimited | limited`
* **ephemeral_storage**
(`boolean`):
Indicates whether the Capacity Reservation supports instances with temporary, block-level storage..
* **instance_match_criteria**
(`string`):
Indicates the type of instance launches that the Capacity Reservation accepts.
Example values: `open | targeted`
* **instance_platform**
(`string`):
The type of operating system for which the Capacity Reservation reserves capacity.
* **instance_type**
(`string`):
The type of instance for which the Capacity Reservation reserves capacity.
* **owner_id**
(`string`):
The ID of the AWS account that owns the DHCP options set.
* **region**
(`string`):
The AWS region.
Example values: `us-east-1 | eu-west-1`
* **state**
(`string`):
The current state of the Capacity Reservation.
Example values: `active | expired | cancelled | pending | failed`
* **tags**
(`key_value[]`):
Any tags assigned to the Capacity Reservation.
* **tenancy**
(`string`):
Indicates the tenancy of the Capacity Reservation.
Example values: `default | dedicated`
* **total_instance_count**
(`number`):
The total number of instances for which the Capacity Reservation reserves capacity.

