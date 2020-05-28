# CloudSpec Resource Definition: aws:ec2:capacity_reservation


## Properties

* **capacity_reservation_id**
(`string`):
The ID of the Capacity Reservation.
* **capacity_reservation_arn**
(`string`):
The Amazon Resource Name (ARN) of the Capacity Reservation.
* **instance_type**
(`string`):
The type of instance for which the Capacity Reservation reserves capacity.
* **instance_platform**
(`string`):
The type of operating system for which the Capacity Reservation reserves capacity.
* **availability_zone**
(`string`):
The Availability Zone in which the capacity is reserved.
* **tenancy**
(`string`):
Indicates the tenancy of the Capacity Reservation.
Example values: `default | dedicated`
* **total_instance_count**
(`integer`):
The total number of instances for which the Capacity Reservation reserves capacity.
* **available_instance_count**
(`integer`):
The remaining capacity. Indicates the number of instances that can be launched in the Capacity Reservation.
* **ebs_optimized**
(`boolean`):
Indicates whether the Capacity Reservation supports EBS-optimized instances.
* **ephemeral_storage**
(`boolean`):
Indicates whether the Capacity Reservation supports instances with temporary, block-level storage..
* **state**
(`string`):
The current state of the Capacity Reservation.
Example values: `active | expired | cancelled | pending | failed`
* **end_date**
(`date`):
The date and time at which the Capacity Reservation expires.
* **end_date_type**
(`string`):
Indicates the way in which the Capacity Reservation ends.
Example values: `unlimited | limited`
* **instance_match_criteria**
(`string`):
Indicates the type of instance launches that the Capacity Reservation accepts.
Example values: `open | targeted`
* **create_date**
(`date`):
The date and time at which the Capacity Reservation was created.
* **tags**
(`key_value[]`):
Any tags assigned to the Capacity Reservation.

