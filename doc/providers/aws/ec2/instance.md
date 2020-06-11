# CloudSpec Resource Definition: aws:ec2:instance


## Properties

* **architecture**
(`string`):
The architecture of the image.
* **block_device_mappings**
(`nested[]`):
Any block device mapping entries for the instance.
    * **device_name**
(`string`):
The device name.
    * **ebs**
(`nested`):
Parameters used to automatically set up EBS volumes when the instance is launched.
        * **attach_time**
(`date`):
The time stamp when the attachment initiated.
        * **delete_on_termination**
(`boolean`):
Indicates whether the volume is deleted on instance termination.
        * **status**
(`string`):
The attachment state.
Example values: `attaching | attached | detaching | detached`
* **cpu_options**
(`nested`):
The CPU options for the instance.
    * **core_count**
(`number`):
The number of CPU cores for the instance.
    * **threads_per_core**
(`number`):
The number of threads per CPU core.
* **ebs_optimized**
(`boolean`):
Indicates whether the instance is optimized for Amazon EBS I/O.
* **elastic_inference_accelerators**
(`string[]`):
The elastic inference accelerator associated with the instance.
* **ena_support**
(`boolean`):
Specifies whether enhanced networking with ENA is enabled.
* **hibernation_configured**
(`boolean`):
Indicates whether the instance is enabled for hibernation.
* **hypervisor**
(`string`):
The hypervisor type of the instance.
Example values: `ovm | xen`
* **instance_id**
(`string`):
The ID of the instance.
* **instance_lifecycle**
(`string`):
Indicates whether this is a Spot Instance or a Scheduled Instance.
Example values: `spot | scheduled`
* **instance_type**
(`string`):
The instance type.
* **kernel_id**
(`string`):
The kernel associated with this instance, if applicable.
* **key_name**
(`string`):
The name of the key pair, if this instance was launched with an associated key pair.
* **launch_time**
(`date`):
The time the instance was launched.
* **licenses**
(`string[]`):
The license configurations.
* **monitoring**
(`nested`):
The monitoring for the instance.
    * **state**
(`string`):
Indicates whether detailed monitoring is enabled. Otherwise, basic monitoring is enabled.
Example values: `disabled | disabling | enabled | pending`
* **network_interfaces**
(`nested[]`):
[EC2-VPC] The network interfaces for the instance.
    * **interface_type**
(`string`):
Describes the type of network interface.
Example values: `interface | efa`
    * **ipv6_addresses**
(`string[]`):
One or more IPv6 addresses associated with the network interface.
    * **owner_id**
(`string`):
The ID of the AWS account that created the network interface.
    * **private_dns_name**
(`string`):
The private DNS name.
    * **private_ip_address**
(`string`):
The IPv4 address of the network interface within the subnet.
    * **private_ip_addresses**
(`nested[]`):
One or more private IPv4 addresses associated with the network interface.
        * **primary**
(`boolean`):
Indicates whether this IPv4 address is the primary private IP address of the network interface.
        * **private_dns_name**
(`string`):
The private IPv4 DNS name.
        * **private_ip_address**
(`string`):
The private IPv4 address of the network interface.
    * **source_dest_check**
(`boolean`):
Indicates whether to validate network traffic to or from this network interface.
    * **status**
(`string`):
The status of the network interface.
Example values: `available | associated | attached | in-use | detaching`
* **outpost_arn**
(`string`):
The Amazon Resource Name (ARN) of the Outpost.
* **placement**
(`nested`):
The location where the instance launched, if applicable.
    * **affinity**
(`string`):
The affinity setting for the instance on the Dedicated Host.
    * **availability_zone**
(`string`):
The Availability Zone of the instance.
    * **group_name**
(`string`):
The name of the placement group the instance is in.
    * **partition_number**
(`number`):
The number of the partition the instance is in.
    * **tenancy**
(`string`):
The tenancy of the instance (if the instance is running in a VPC).
Example values: `default | dedicated | host`
* **platform**
(`string`):
The value is `Windows` for Windows instances; otherwise blank.
* **private_dns_name**
(`string`):
(IPv4 only) The private DNS hostname name assigned to the instance.
* **private_ip_address**
(`string`):
The private IPv4 address assigned to the instance.
* **product_codes**
(`nested[]`):
The product codes attached to this instance, if applicable.
    * **id**
(`string`):
The product code.
    * **type**
(`string`):
The type of product code.
Example values: `devpay | marketplace`
* **public_dns_name**
(`string`):
(IPv4 only) The public DNS name assigned to the instance.
* **public_ip_address**
(`string`):
The public IPv4 address assigned to the instance, if applicable.
* **region**
(`string`):
The AWS region.
Example values: `us-east-1 | eu-west-1`
* **root_device_name**
(`string`):
The device name of the root device volume.
* **root_device_type**
(`string`):
The root device type used by the AMI.
* **source_dest_check**
(`boolean`):
Specifies whether to enable an instance launched in a VPC to perform NAT.
* **sriov_net_support**
(`string`):
Specifies whether enhanced networking with the Intel 82599 Virtual Function interface is enabled.
* **state**
(`string`):
The current state of the instance.
* **tags**
(`key_value[]`):
Any tags assigned to the instance.
* **virtualization_type**
(`string`):
The virtualization type of the instance.
Example values: `hvm | paravirtual`

## Associations

* **capacity_reservation**
(*aws:ec2:capacity_reservation*):
The Capacity Reservation
* **elastic_gpus**
(*aws:ec2:elastic_gpu*[]):
The Elastic GPU associated with the instance
* **iam_instance_profile**
(*aws:iam:iam_instance_profile*):
The IAM instance profile associated with the instance, if applicable
* **image**
(*aws:ec2:image*):
The AMI used to launch the instance
* **security_groups**
(*aws:ec2:security_group*[]):
The security groups for the instance
* **subnet**
(*aws:ec2:subnet*):
[EC2-VPC] The subnet in which the instance is running
* **vpc**
(*aws:ec2:vpc*):
[EC2-VPC] The VPC in which the instance is running
