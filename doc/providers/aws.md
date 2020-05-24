
# CloudSpec Provider: Amazon Web Services

## Resource definitions

* [aws:ec2:ami](#amazon-machine-image): Amazon Machine Image
* [aws:ec2:ebs_volume](#ebs-volume): EBS Volume
* [aws:ec2:instance](#ec2-instance): EC2 Instance
* [aws:ec2:network_interface](#network-interface): Network Interface
* [aws:ec2:vpc](#virtual-private-cloud): Virtual Private Cloud
* [aws:ec2:subnet](#vpc-subnet): VPC Subnet
* [aws:iam:iam_instance_profile](#amazon-machine-image): Amazon Machine Image
* [aws:s3:bucket](#s3-bucket): S3 Bucket
* [aws:sqs:queue](#sqs-queue): SQS Queue
* [aws:sns:topic](#sns-topic): SNS Topic

### Amazon Machine Image

**Properties**

* **id** (*string*):
AMI ID
* **region** (*string*):
AWS Region
* **type** (*string*):
AMI Type
* **name** (*string*):
AMI Name
* **location** (*string*):
AMI Location
* **architecture** (*string*):
Type of architecture
* **kernel_id** (*string*):
ID of the Kernel
* **platform** (*string*):
OS Platform
* **root_device** (*nested*):
Root Device
    * **name** (*string*):
Device name
    * **type** (*string*):
Device type
* **devices** (*nested[]*):
Attached devices
    * **name** (*string*):
Device name
    * **virtual_name** (*string*):
Device virtual name
    * **delete_on_termination** (*boolean*):
Delete on termination
    * **volume_type** (*string*):
EBS volume type
    * **volume_size** (*integer*):
EBS volume size
    * **encrypted** (*boolean*):
Flag indicating if the volume is encrypted
* **ena_support** (*boolean*):
Flag indicating ENA support
* **state** (*string*):
State of the image
* **product_codes** (*nested[]*):
Product codes
    * **id** (*string*):
Product code ID
    * **type** (*string*):
Product code type
* **tags** (*key_value[]*):
List of tags


### EBS Volume

**Properties**

* **id** (*string*):
Volume ID
* **region** (*string*):
AWS Region
* **type** (*string*):
Volume type
* **availability_zone** (*string*):
Availability zone
* **size** (*integer*):
Volume size in GB
* **iops** (*integer*):
Volume IOPs
* **encrypted** (*boolean*):
Flag indicating whether the volume is encrypted
* **multi_attach_enabled** (*boolean*):
Flag indicated whether the volume has multi-attach enabled
* **state** (*string*):
State
* **tags** (*key_value[]*):
List of tags


### EC2 Instance

**Properties**

* **id** (*string*):
The ID of the instance. *(Example: i-00370a471026eb7f8)
* **region** (*string*):
The AWS region *(Example: us-east-1, eu-west-1)
* **type** (*string*):
The instance type *(Example: t2.nano, m5.xlarge)
* **key_name** (*string*):
The name of the key pair, if this instance was launched with an associated key pair
* **architecture** (*string*):
The architecture of the image *(Example: i386, x86_64, arm64)
* **kernel_id** (*string*):
The kernel associated with this instance, if applicable
* **ebs_optimized** (*boolean*):
Indicates whether the instance is optimized for Amazon EBS I/O
* **private_ip** (*string*):
The private IPv4 address assigned to the instance *(Example: 172.31.40.126)
* **public_ip** (*string*):
The public IPv4 address assigned to the instance *(Example: 34.241.109.11)
* **private_dns** (*string*):
The private DNS hostname name assigned to the instance *(Example: ip-172-31-40-126.eu-west-1.compute.internal)
* **public_dns** (*string*):
The public DNS hostname name assigned to the instance *(Example: ec2-34-241-109-11.eu-west-1.compute.amazonaws.com)
* **root_device** (*nested*):
The root device attached to the instance
    * **name** (*string*):
The device name of the root device volume
    * **type** (*string*):
The root device type used by the AMI
* **devices** (*nested[]*):
The devices attached to the instance
    * **name** (*string*):
Device name
    * **delete_on_termination** (*boolean*):
Delete on termination
* **network_interfaces** (*nested[]*):
The network interfaces attached to the instance
    * **private_ip** (*string*):
Private IP address
    * **private_dns** (*string*):
Private DNS address
* **hibernation_configured** (*boolean*):
Indicates whether the instance is enabled for hibernation
* **tags** (*key_value[]*):
Any tags assigned to the instance *(Example: tags["my_key"] = "my_value")

**Associations**

* **ami** (*aws:ec2:ami*): The AMI used to launch the instance
* **vpc** (*aws:ec2:vpc*): The VPC in which the instance is running
* **subnet** (*aws:ec2:subnet*): The subnet in which the instance is running

### Network Interface

**Properties**

* **id** (*string*):
Interface ID
* **region** (*string*):
AWS Region
* **availability_zone** (*string*):
Availability zone
* **type** (*string*):
Interface type
* **mac_address** (*string*):
MAC Address
* **private_dns** (*string*):
Private DNS
* **private_ip** (*string*):
Private IP address
* **status** (*string*):
Status
* **tags** (*key_value[]*):
List of tags

**Associations**

* **vpc** (*aws:ec2:vpc*): VPC
* **subnet** (*aws:ec2:subnet*): Subnet

### Virtual Private Cloud

**Properties**

* **id** (*string*):
VPC ID
* **region** (*string*):
AWS region
* **name** (*string*):
VPC Name
* **cidr_block** (*string*):
CIDR Block
* **state** (*string*):
State
* **default** (*boolean*):
Indicate if the VPC is the default
* **dns_hostnames** (*boolean*):
Indicate if the the DNS Hostnames flag is enabled
* **dns_support** (*boolean*):
Indicate if the the DNS Support flag is enabled
* **tags** (*key_value[]*):
List of tags


### VPC Subnet

**Properties**

* **id** (*string*):
VPC Subnet ID
* **region** (*string*):
AWS Region
* **availability_zone** (*string*):
AWS Availability Zone
* **cidr_block** (*string*):
CIDR Block
* **state** (*string*):
State
* **tags** (*key_value[]*):
List of tags

**Associations**

* **vpc** (*aws:ec2:vpc*): VPC

### Amazon Machine Image

**Properties**

* **id** (*string*):
Instance profile ID
* **name** (*string*):
Instance profile name
* **path** (*string*):
Path


### S3 Bucket

**Properties**

* **region** (*string*):
AWS Region
* **bucket_name** (*string*):
Bucket name
* **encryption** (*nested*):
Encryption
    * **enabled** (*boolean*):
Encryption is enabled
    * **type** (*string*):
Encryption type
* **logging** (*nested*):
Logging
    * **enabled** (*boolean*):
Logging is enabled


### SQS Queue

**Properties**

* **queue_arn** (*string*):
Queue Arn
* **visibility_timeout** (*integer*):
Visibility timeout
* **delay_seconds** (*integer*):
Delay in seconds
* **message_retention_period** (*integer*):
Message retention period


### SNS Topic

**Properties**

* **topic_arn** (*string*):
Topic Arn
* **enabled** (*boolean*):
Enabled


