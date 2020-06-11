# CloudSpec Resource Definition: aws:ec2:snapshot


## Properties

* **encrypted**
(`boolean`):
Indicates whether the snapshot is encrypted.
* **owner_id**
(`string`):
The AWS account ID of the EBS snapshot owner.
* **progress**
(`string`):
The progress of the snapshot, as a percentage.
* **region**
(`string`):
The AWS region.
Example values: `us-east-1 | eu-west-1`
* **snapshotId**
(`string`):
The ID of the snapshot. Each snapshot receives a unique identifier when it is created.
* **start_time**
(`date`):
The time stamp when the snapshot was initiated.
* **state**
(`string`):
The snapshot state.
* **tags**
(`key_value[]`):
Any tags assigned to the snapshot.
* **volume_size**
(`number`):
 The size of the volume, in GiB.

## Associations

* **volume**
(*aws:ec2:volume*):
The volume that was used to create the snapshot
