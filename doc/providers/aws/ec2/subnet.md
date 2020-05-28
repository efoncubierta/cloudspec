# CloudSpec Resource Definition: aws:ec2:subnet


## Properties

* **availability_zone**
(`string`):
The Availability Zone of the subnet.
* **available_ip_address_count**
(`integer`):
The number of unused private IPv4 addresses in the subnet.
* **cidr_block**
(`string`):
The IPv4 CIDR block assigned to the subnet.
* **default_for_az**
(`boolean`):
Indicates whether this is the default subnet for the Availability Zone.
* **map_public_ip_on_launch**
(`boolean`):
Indicates whether instances launched in this subnet receive a public IPv4 address.
* **state**
(`string`):
The current state of the subnet.
Example values: `pending | available`
* **subnet_id**
(`string`):
The ID of the subnet.
* **assign_ipv6_address_on_creation**
(`boolean`):
Indicates whether a network interface created in this subnet receives an IPv6 address..
* **ipv6_cidr_block_associations**
(`nested[]`):
Information about the IPv6 CIDR blocks associated with the subnet.
    * **ipv6_cidr_block**
(`string`):
The IPv6 CIDR block.
    * **ipv6_cidr_block_state**
(`string`):
Information about the state of the CIDR block.
Example values: `associating | associated | disassociating | disassociated | failing | failed`
* **tags**
(`key_value[]`):
Any tags assigned to the subnet.
* **subnet_arn**
(`string`):
The Amazon Resource Name (ARN) of the subnet.
* **outpost_arn**
(`string`):
The Amazon Resource Name (ARN) of the Outpost.

## Associations

* **vpc**
(*aws:ec2:vpc*):
The ID of the VPC the subnet is in
