# CloudSpec Resource Definition: aws:ec2:internet_gateway


## Properties

* **attachments**
(`nested[]`):
Any VPCs attached to the internet gateway.
    * **state**
(`string`):
The current state of the attachment.
Example values: `attaching | attached | detaching | detached`
* **internet_gateway_id**
(`string`):
The ID of the internet gateway.
* **owner_id**
(`string`):
The ID of the AWS account that owns the internet gateway.
* **region**
(`string`):
The AWS region.
Example values: `us-east-1 | eu-west-1`
* **tags**
(`key_value[]`):
Any tags assigned to the internet gateway.

