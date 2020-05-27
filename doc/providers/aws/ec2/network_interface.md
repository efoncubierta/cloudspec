# CloudSpec Resource Definition: aws:ec2:network_interface


## Properties

* **id**
(`string`):
Interface ID.
* **region**
(`string`):
AWS Region.
* **availability_zone**
(`string`):
Availability zone.
* **type**
(`string`):
Interface type.
* **mac_address**
(`string`):
MAC Address.
* **private_dns**
(`string`):
Private DNS.
* **private_ip**
(`string`):
Private IP address.
* **status**
(`string`):
Status.
* **tags**
(`key_value[]`):
List of tags.

## Associations

* **vpc**
(*aws:ec2:vpc*):
VPC
* **subnet**
(*aws:ec2:subnet*):
Subnet
