# CloudSpec Resource Definition: aws:ec2:snapshot


## Properties

* **encrypted**
(`boolean`):
Indicates whether the snapshot is encrypted.
* **progress**
(`string`):
The progress of the snapshot, as a percentage.
* **snapshotId**
(`string`):
The ID of the snapshot. Each snapshot receives a unique identifier when it is created.
* **start_time**
(`date`):
The time stamp when the snapshot was initiated.
* **state**
(`string`):
The snapshot state.
* **volume_size**
(`integer`):
 The size of the volume, in GiB.
* **tags**
(`key_value[]`):
Any tags assigned to the snapshot.

## Associations

* **volume**
(*aws:ec2:volume*):
The volume that was used to create the snapshot
