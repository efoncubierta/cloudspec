# CloudSpec Resource Definition: aws:ec2:instance


## Properties

* **id**
(`string`):
The ID of the instance.
Example values: `i-00370a471026eb7f8`
* **region**
(`string`):
The AWS region.
Example values: `us-east-1 | eu-west-1 ...`
* **type**
(`string`):
The instance type.
Example values: `t2.nano | m5.xlarge ...`
* **key_name**
(`string`):
The name of the key pair, if this instance was launched with an associated key pair.
* **architecture**
(`string`):
The architecture of the image.
Example values: `i386 | x86_64 | arm64`
* **kernel_id**
(`string`):
The kernel associated with this instance, if applicable.
* **ebs_optimized**
(`boolean`):
Indicates whether the instance is optimized for Amazon EBS I/O.
* **private_ip**
(`string`):
The private IPv4 address assigned to the instance.
Example values: `172.31.40.126`
* **public_ip**
(`string`):
The public IPv4 address assigned to the instance.
Example values: `34.241.109.11`
* **private_dns**
(`string`):
The private DNS hostname name assigned to the instance.
Example values: `ip-172-31-40-126.eu-west-1.compute.internal`
* **public_dns**
(`string`):
The public DNS hostname name assigned to the instance.
Example values: `ec2-34-241-109-11.eu-west-1.compute.amazonaws.com`
* **root_device**
(`nested`):
The root device attached to the instance.
    * **name**
(`string`):
The device name of the root device volume.
    * **type**
(`string`):
The root device type used by the AMI.
* **devices**
(`nested[]`):
The devices attached to the instance.
    * **name**
(`string`):
The device name.
    * **delete_on_termination**
(`boolean`):
Indicates whether the volume is deleted on instance termination.
* **network_interfaces**
(`nested[]`):
The network interfaces attached to the instance.
    * **private_ip**
(`string`):
The IPv4 address of the network interface within the subnet.
Example values: `172.31.40.126`
    * **private_dns**
(`string`):
The private DNS name.
Example values: `ec2-34-241-109-11.eu-west-1.compute.amazonaws.com`
    * **status**
(`string`):
The status of the network interface.
Example values: `available | associated | attaching | in-use | detaching`
* **hibernation_configured**
(`boolean`):
Indicates whether the instance is enabled for hibernation.
* **tags**
(`key_value[]`):
Any tags assigned to the instance.

## Associations

* **ami**
(*aws:ec2:ami*):
The AMI used to launch the instance
* **vpc**
(*aws:ec2:vpc*):
The VPC in which the instance is running
* **subnet**
(*aws:ec2:subnet*):
The subnet in which the instance is running
