# CloudSpec Resource Definition: aws:ec2:ebs_snapshot


## Properties

* **id**
(`string`):
The ID of the snapshot.
Example values: `snap-1234567890abcdef0`
* **region**
(`string`):
The AWS region.
Example values: `us-east-1, eu-west-1`
* **size**
(`integer`):
The size of the snapshot, in GiBs.
* **encrypted**
(`boolean`):
Indicates whether the snapshot is encrypted.
* **state**
(`string`):
The snapshot state.
Example values: `pending | completed | error`
* **tags**
(`key_value[]`):
Any tags assigned to the snapshot.

## Associations

* **snapshot**
(*aws:ec2:ebs_volume*):
The volume that was used to create the snapshot
