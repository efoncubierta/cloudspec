# CloudSpec Resource Definition: aws:ec2:vpc_peering_connection


## Properties

* **accepter_vpc_info**
(`nested`):
Information about the accepter VP.
    * **cidr_block**
(`string`):
The IPv4 CIDR block for the VPC.
    * **cidr_blocks**
(`string[]`):
Information about the IPv4 CIDR blocks for the VPC.
    * **ipv6_cidr_blocks**
(`string[]`):
The IPv6 CIDR block for the VPC.
    * **owner_id**
(`string`):
The AWS account ID of the VPC owner.
    * **peering_options**
(`nested`):
Information about the VPC peering connection options for the accepter or requester VPC.
        * **allow_dns_resolution_from_remote_vpc**
(`boolean`):
Indicates whether a local VPC can resolve public DNS hostnames to private IP addresses when queried from instances in a peer VPC.
        * **allow_egress_from_local_classic_link_to_remote_vpc**
(`boolean`):
Indicates whether a local ClassicLink connection can communicate with the peer VPC over the VPC peering connection.
        * **allow_egress_from_local_vpc_to_remote_classic_link**
(`boolean`):
Indicates whether a local VPC can communicate with a ClassicLink connection in the peer VPC over the VPC peering connection.
    * **region**
(`string`):
The Region in which the VPC is located.
* **expiration_time**
(`date`):
The time that an unaccepted VPC peering connection will expire.
* **region**
(`string`):
The AWS region.
Example values: `us-east-1 | eu-west-1`
* **requester_vpc_info**
(`nested`):
Information about the requester VPC.
    * **cidr_block**
(`string`):
The IPv4 CIDR block for the VPC.
    * **cidr_blocks**
(`string[]`):
Information about the IPv4 CIDR blocks for the VPC.
    * **ipv6_cidr_blocks**
(`string[]`):
The IPv6 CIDR block for the VPC.
    * **owner_id**
(`string`):
The AWS account ID of the VPC owner.
    * **peering_options**
(`nested`):
Information about the VPC peering connection options for the accepter or requester VPC.
        * **allow_dns_resolution_from_remote_vpc**
(`boolean`):
Indicates whether a local VPC can resolve public DNS hostnames to private IP addresses when queried from instances in a peer VPC.
        * **allow_egress_from_local_classic_link_to_remote_vpc**
(`boolean`):
Indicates whether a local ClassicLink connection can communicate with the peer VPC over the VPC peering connection.
        * **allow_egress_from_local_vpc_to_remote_classic_link**
(`boolean`):
Indicates whether a local VPC can communicate with a ClassicLink connection in the peer VPC over the VPC peering connection.
    * **region**
(`string`):
The Region in which the VPC is located.
* **status**
(`string`):
The status of the VPC peering connection.
Example values: `initiating-request | pending-acceptance | active | deleted | rejected | failed | expired | provisioning | deleting`
* **tags**
(`key_value[]`):
Any tags assigned to the resource.
* **vpc_peering_connection_id**
(`string`):
The ID of the VPC peering connection.

