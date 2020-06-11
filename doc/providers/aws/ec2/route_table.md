# CloudSpec Resource Definition: aws:ec2:route_table


## Properties

* **associations**
(`nested[]`):
The associations between the route table and one or more subnets or a gateway.
    * **state**
(`string`):
The state of the association.
Example values: `associating | associated | disassociating | disassociated | failed`
    * **main**
(`boolean`):
Indicates whether this is the main route table.
    * **main**
(`string`):
The ID of the association.
* **owner_id**
(`string`):
The ID of the AWS account that owns the route table.
* **propagating_gateway**
(`string[]`):
Any virtual private gateway (VGW) propagating routes.
* **region**
(`string`):
The AWS region.
Example values: `us-east-1 | eu-west-1`
* **route_table_id**
(`string`):
The ID of the route table.
* **routes**
(`nested[]`):
The routes in the route table.
    * **destination_cidr_block**
(`string`):
The IPv4 CIDR block used for the destination match.
    * **destination_ipv6_cidr_block**
(`string`):
The IPv6 CIDR block used for the destination match.
    * **destination_prefix_list_id**
(`string`):
The prefix of the AWS service.
    * **instance_owner-Id**
(`string`):
The AWS account ID of the owner of the instance.
    * **origin**
(`string`):
Describes how the route was created.
Example values: `CreateRouteTable | CreateRoute | EnableVgwRoutePropagation`
    * **state**
(`string`):
The state of the route.
Example values: `active | blackhole`
* **tags**
(`key_value[]`):
Any tags assigned to the route table.

## Associations

* **vpc**
(*aws:ec2:vpc*):
The ID of the VPC
