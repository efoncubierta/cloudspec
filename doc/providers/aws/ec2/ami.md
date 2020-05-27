# CloudSpec Resource Definition: aws:ec2:ami


## Properties

* **id**
(`string`):
The ID of the AMI.
Example values: `ami-06ce3edf0cff21f07`
* **region**
(`string`):
The AWS region.
Example values: `us-east-1 | eu-west-1`
* **type**
(`string`):
The type of image.
Example values: `machine | kernel | ramdisk`
* **name**
(`string`):
The name of the AMI that was provided during image creation.
* **location**
(`string`):
The location of the AMI.
* **architecture**
(`string`):
The architecture of the image.
Example values: `i386 | x86_64 | arm64`
* **kernel_id**
(`string`):
The kernel associated with the image, if any.
* **platform**
(`string`):
This value is set to windows for Windows AMIs; otherwise, it is blank.
Example values: `windows`
* **root_device**
(`nested`):
The root device.
    * **name**
(`string`):
The device name of the root device volume.
Example values: `/dev/sda1`
    * **type**
(`string`):
The type of root device used by the AMI.
Example values: `ebs, instance-store`
* **devices**
(`nested[]`):
The devices attached.
    * **name**
(`string`):
The device name.
Example values: `/dev/sdh`
    * **virtual_name**
(`string`):
The virtual device name.
    * **delete_on_termination**
(`boolean`):
Indicates whether the EBS volume is deleted on instance termination.
    * **volume_type**
(`string`):
The volume type.
Example values: `gp2, st1, sc1, standard`
    * **volume_size**
(`integer`):
The size of the volume, in GiB.
    * **encrypted**
(`boolean`):
Indicates whether the encryption state of an EBS volume is changed while being restored from a backing snapshot.
* **ena_support**
(`boolean`):
Flag indicating ENA support.
* **state**
(`string`):
State of the image.
* **product_codes**
(`nested[]`):
Any product codes associated with the AMI.
    * **id**
(`string`):
The product code.
    * **type**
(`string`):
The type of product code.
Example values: `devpay, marketplace`
* **tags**
(`key_value[]`):
Any tags assigned to the image.

