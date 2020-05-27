# CloudSpec Resource Definition: aws:ec2:ebs_volume


## Properties

* **id**
(`string`):
The ID of the volume.
Example values: `vol-08695a8113376379a`
* **type**
(`string`):
The volume type.
Example values: `gp2, io2, sc1, st1, standard`
* **region**
(`string`):
The AWS region.
Example values: `us-east-1, eu-west-1`
* **availability_zone**
(`string`):
The AWS availability zone.
Example values: `us-east-1a, eu-west-1a`
* **size**
(`integer`):
The size of the volume, in GiBs.
* **iops**
(`integer`):
The number of I/O operations per second (IOPS) that the volume supports.
* **encrypted**
(`boolean`):
Indicates whether the volume is encrypted.
* **multi_attach_enabled**
(`boolean`):
Indicates whether Amazon EBS Multi-Attach is enabled.
* **attachments**
(`nested[]`):
Information about the volume attachments.
    * **name**
(`string`):
The device name.
Example values: `/dev/sda1`
    * **delete_on_termination**
(`boolean`):
Indicates whether the EBS volume is deleted on instance termination.
    * **state**
(`string`):
The attachment state of the volume.
Example values: `attaching, attached, detaching, detached, busy`
* **state**
(`string`):
The volume state.
Example values: `creating, available, in-use, deleting, deleted, error`
* **tags**
(`key_value[]`):
Any tags assigned to the volume.

## Associations

* **snapshot**
(*aws:ec2:ebs_snapshot*):
The snapshot from which the volume was created, if applicable
