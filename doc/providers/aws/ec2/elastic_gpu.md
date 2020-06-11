# CloudSpec Resource Definition: aws:ec2:elastic_gpu


## Properties

* **availability_zone**
(`string`):
The Availability Zone in the which the Elastic Graphics accelerator resides.
* **elastic_gpu_health**
(`string`):
The status of the Elastic Graphics accelerator.
Example values: `OK | IMPAIRED`
* **elastic_gpu_id**
(`string`):
The ID of the Elastic Graphics accelerator.
* **elastic_gpu_state**
(`string`):
The state of the Elastic Graphics accelerator.
Example values: `ATTACHED`
* **elastic_gpu_type**
(`string`):
The type of Elastic Graphics accelerator.
* **region**
(`string`):
The AWS region.
Example values: `us-east-1 | eu-west-1`
* **tags**
(`key_value[]`):
The tags assigned to the Elastic Graphics accelerator.

## Associations

* **instance**
(*aws:ec2:instance*):
The instance to which the Elastic Graphics accelerator is attached
