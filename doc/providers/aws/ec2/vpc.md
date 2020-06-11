# CloudSpec Resource Definition: aws:ec2:vpc


## Properties

* **cidr_block**
(`string`):
The primary IPv4 CIDR block for the VPC.
* **cidr_block_associations**
(`nested[]`):
Information about the IPv4 CIDR blocks associated with the VPC.
    * **cidr_block**
(`string`):
The IPv4 CIDR block.
    * **cidr_block_state**
(`string`):
Information about the state of the CIDR block.
* **instance_tenancy**
(`string`):
The allowed tenancy of instances launched into the VPC.
Example values: `default | dedicated | host`
* **ipv6_cidr_block_associations**
(`nested[]`):
Information about the IPv6 CIDR blocks associated with the VPC.
    * **ipv6_cidr_block**
(`string`):
The IPv6 CIDR block.
    * **ipv6_cidr_block_state**
(`string`):
Information about the state of the CIDR block.
    * **ipv6_pool**
(`string`):
The ID of the IPv6 address pool from which the IPv6 CIDR block is allocated.
    * **network_border_group**
(`string`):
The name of the location from which we advertise the IPV6 CIDR block.
* **is_default**
(`boolean`):
Indicates whether the VPC is the default VPC.
* **owner_id**
(`string`):
The ID of the AWS account that owns the VPC.
* **region**
(`string`):
The AWS region.
Example values: `us-east-1 | eu-west-1`
* **state**
(`string`):
The current state of the VPC.
* **tags**
(`key_value[]`):
Any tags assigned to the VPC.
* **vpc_id**
(`string`):
The ID of the VPC.

## Associations

* **dhcp_options**
(*aws:ec2:dhcp_options*):
The ID of the set of DHCP options you've associated with the VPC
