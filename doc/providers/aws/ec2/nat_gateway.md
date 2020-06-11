# CloudSpec Resource Definition: aws:ec2:nat_gateway


## Properties

* **create_time**
(`date`):
The date and time the NAT gateway was created.
* **delete_time**
(`date`):
The date and time the NAT gateway was deleted, if applicable.
* **failure_code**
(`string`):
If the NAT gateway could not be created, specifies the error code for the failure..
* **nat_gateway_addresses**
(`nested[]`):
Information about the IP addresses and network interface associated with the NAT gateway.
    * **allocation_id**
(`string`):
The allocation ID of the Elastic IP address that's associated with the NAT gateway.
    * **private_ip**
(`string`):
The private IP address associated with the Elastic IP address.
    * **public_ip**
(`string`):
The Elastic IP address associated with the NAT gateway.
* **nat_gateway_id**
(`string`):
The ID of the NAT gateway.
* **region**
(`string`):
The AWS region.
Example values: `us-east-1 | eu-west-1`
* **state**
(`string`):
The state of the NAT gateway.
Example values: `pending | failed | available | deleting | deleted`
* **tags**
(`key_value[]`):
The tags for the NAT gateway.

## Associations

* **subnet**
(*aws:ec2:subnet*):
The subnet in which the NAT gateway is located
* **vpc**
(*aws:ec2:vpc*):
The VPC in which the NAT gateway is located
