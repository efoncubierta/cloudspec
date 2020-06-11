# CloudSpec Resource Definition: aws:ec2:transit_gateway


## Properties

* **creation_time**
(`date`):
The creation time.
* **options**
(`nested`):
The transit gateway options.
    * **auto_accept_shared_attachments**
(`boolean`):
Indicates whether attachment requests are automatically accepted.
    * **default_route_table_association**
(`boolean`):
Indicates whether resource attachments are automatically associated with the default association route table.
    * **default_route_table_propagation**
(`boolean`):
Indicates whether resource attachments automatically propagate routes to the default propagation route table.
    * **dns_support**
(`boolean`):
Indicates whether DNS support is enabled.
    * **multicast_support**
(`boolean`):
Indicates whether multicast is enabled on the transit gateway.
    * **vpn_ecmp_support**
(`boolean`):
Indicates whether Equal Cost Multipath Protocol support is enabled.
* **owner_id**
(`string`):
The ID of the AWS account ID that owns the transit gateway.
* **region**
(`string`):
The AWS region.
Example values: `us-east-1 | eu-west-1`
* **state**
(`string`):
The state of the transit gateway.
Example values: `pending | available | modifying | deleting | deleted`
* **tags**
(`key_value[]`):
The tags for the transit gateway.
* **transit_gateway_arn**
(`string`):
The Amazon Resource Name (ARN) of the transit gateway.
* **transit_gateway_id**
(`string`):
The ID of the transit gateway.

