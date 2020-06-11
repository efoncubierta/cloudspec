# CloudSpec Resource Definition: aws:ec2:security_group


## Properties

* **group_id**
(`string`):
The ID of the security group.
* **group_name**
(`string`):
The name of the security group.
* **ip_permissions**
(`nested[]`):
The inbound rules associated with the security group.
    * **from_port**
(`number`):
The start of port range for the TCP and UDP protocols, or an ICMP/ICMPv6 type number.
    * **ip_protocol**
(`string`):
The IP protocol name or number.
    * **ip_ranges**
(`nested[]`):
The IPv4 ranges.
        * **cidr_ip**
(`string`):
The IPv4 CIDR range.
    * **ipv6_ranges**
(`nested[]`):
[VPC only] The IPv6 ranges.
        * **cidr_ipv6**
(`string`):
The IPv6 CIDR range.
    * **prefix_list_ids**
(`string[]`):
[VPC only] The prefix list IDs for an AWS service.
    * **to_port**
(`number`):
The end of port range for the TCP and UDP protocols, or an ICMP/ICMPv6 code.
    * **user_groups**
(`nested[]`):
The security group and AWS account ID pairs.
        * **group_name**
(`string`):
The name of the security group.
        * **peering_status**
(`string`):
The status of a VPC peering connection, if applicable.
        * **userId**
(`string`):
The ID of an AWS account.
* **ip_permissions_egress**
(`nested[]`):
[VPC only] The outbound rules associated with the security group.
    * **from_port**
(`number`):
The start of port range for the TCP and UDP protocols, or an ICMP/ICMPv6 type number.
    * **ip_protocol**
(`string`):
The IP protocol name or number.
    * **ip_ranges**
(`nested[]`):
The IPv4 ranges.
        * **cidr_ip**
(`string`):
The IPv4 CIDR range.
    * **ipv6_ranges**
(`nested[]`):
[VPC only] The IPv6 ranges.
        * **cidr_ipv6**
(`string`):
The IPv6 CIDR range.
    * **prefix_list_ids**
(`string[]`):
[VPC only] The prefix list IDs for an AWS service.
    * **to_port**
(`number`):
The end of port range for the TCP and UDP protocols, or an ICMP/ICMPv6 code.
    * **user_groups**
(`nested[]`):
The security group and AWS account ID pairs.
        * **group_name**
(`string`):
The name of the security group.
        * **peering_status**
(`string`):
The status of a VPC peering connection, if applicable.
        * **userId**
(`string`):
The ID of an AWS account.
* **owner_id**
(`string`):
The AWS account ID of the owner of the security group.
* **region**
(`string`):
The AWS region.
Example values: `us-east-1 | eu-west-1`
* **tags**
(`key_value[]`):
Any tags assigned to the security group.

## Associations

* **vpc**
(*aws:ec2:vpc*):
[VPC only] The VPC for the security group
