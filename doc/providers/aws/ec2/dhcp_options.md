# CloudSpec Resource Definition: aws:ec2:dhcp_options


## Properties

* **dhcp_configurations**
(`nested[]`):
One or more DHCP options in the set.
    * **key**
(`string`):
The name of a DHCP option.
    * **values**
(`string[]`):
One or more values for the DHCP option.
* **dhcp_options_id**
(`string`):
The ID of the set of DHCP options.
* **owner_id**
(`string`):
The ID of the AWS account that owns the Capacity Reservation.
* **region**
(`string`):
The AWS region.
Example values: `us-east-1 | eu-west-1`
* **tags**
(`key_value[]`):
Any tags assigned to the DHCP options set.

