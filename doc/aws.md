
# CloudSpec Provider: aws

## Resource definitions

* [aws:ec2:ami](aws:ec2:ami): Amazon Machine Image
* [aws:ec2:ebs_volume](aws:ec2:ebs_volume): EBS Volume
* [aws:ec2:instance](aws:ec2:instance): EC2 Instance
* [aws:ec2:network_interface](aws:ec2:network_interface): Network Interface
* [aws:ec2:vpc](aws:ec2:vpc): Virtual Private Cloud
* [aws:ec2:subnet](aws:ec2:subnet): VPC Subnet
* [aws:iam:iam_instance_profile](aws:iam:iam_instance_profile): Amazon Machine Image
* [aws:s3:bucket](aws:s3:bucket): S3 Bucket
* [aws:sqs:queue](aws:sqs:queue): SQS Queue
* [aws:sns:topic](aws:sns:topic): SNS Topic

### Amazon Machine Image

**Properties**

* **id** (*string*): AMI ID
* **region** (*string*): AWS Region
* **type** (*string*): AMI Type
* **name** (*string*): AMI Name
* **location** (*string*): AMI Location
* **architecture** (*string*): Type of architecture
* **kernel_id** (*string*): ID of the Kernel
* **platform** (*string*): OS Platform
* **root_device** (*nested*): Root Device
    * **name** (*string*): Device name
    * **type** (*string*): Device type
* **devices** (*nested[]*): Attached devices
    * **name** (*string*): Device name
    * **virtual_name** (*string*): Device virtual name
    * **delete_on_termination** (*boolean*): Delete on termination
    * **volume_type** (*string*): EBS volume type
    * **volume_size** (*integer*): EBS volume size
    * **encrypted** (*boolean*): Flag indicating if the volume is encrypted
* **ena_support** (*boolean*): Flag indicating ENA support
* **state** (*string*): State of the image
* **product_codes** (*nested[]*): Product codes
    * **id** (*string*): Product code ID
    * **type** (*string*): Product code type
* **tags** (*key_value[]*): List of tags


### EBS Volume

**Properties**

* **id** (*string*): Volume ID
* **region** (*string*): AWS Region
* **type** (*string*): Volume type
* **availability_zone** (*string*): Availability zone
* **size** (*integer*): Volume size in GB
* **iops** (*integer*): Volume IOPs
* **encrypted** (*boolean*): Flag indicating whether the volume is encrypted
* **multi_attach_enabled** (*boolean*): Flag indicated whether the volume has multi-attach enabled
* **state** (*string*): State
* **tags** (*key_value[]*): List of tags


### EC2 Instance

**Properties**

* **id** (*string*): EC2 Instance ID
* **region** (*string*): AWS Region
* **type** (*string*): EC2 Instance Type
* **key_name** (*string*): Name of the key pair
* **architecture** (*string*): Type of architecture
* **kernel_id** (*string*): ID of the Kernel
* **ebs_optimized** (*boolean*): Flag indicating if the instance is optimized for EBS
* **private_ip** (*string*): Private IP address
* **public_ip** (*string*): Public IP address
* **private_dns** (*string*): Private DNS name
* **public_dns** (*string*): Public DNS
* **root_device** (*nested*): Root Device
    * **name** (*string*): Device name
    * **type** (*string*): Device type
* **devices** (*nested[]*): List of devices attached
    * **name** (*string*): Device name
    * **delete_on_termination** (*boolean*): Delete on termination
* **network_interfaces** (*nested[]*): List of network interfaces attached
    * **private_ip** (*string*): Private IP address
    * **private_dns** (*string*): Private DNS address
* **hibernation_configured** (*boolean*): Flag indicating if hibernation is configured
* **tags** (*key_value[]*): List of tags

**Associations**

* **ami** (*aws:ec2:ami*): AMI
* **vpc** (*aws:ec2:vpc*): VPC
* **subnet** (*aws:ec2:subnet*): Subnet

### Network Interface

**Properties**

* **id** (*string*): Interface ID
* **region** (*string*): AWS Region
* **availability_zone** (*string*): Availability zone
* **type** (*string*): Interface type
* **mac_address** (*string*): MAC Address
* **private_dns** (*string*): Private DNS
* **private_ip** (*string*): Private IP address
* **status** (*string*): Status
* **tags** (*key_value[]*): List of tags

**Associations**

* **vpc** (*aws:ec2:vpc*): VPC
* **subnet** (*aws:ec2:subnet*): Subnet

### Virtual Private Cloud

**Properties**

* **id** (*string*): VPC ID
* **region** (*string*): AWS region
* **name** (*string*): VPC Name
* **cidr_block** (*string*): CIDR Block
* **state** (*string*): State
* **default** (*boolean*): Indicate if the VPC is the default
* **dns_hostnames** (*boolean*): Indicate if the the DNS Hostnames flag is enabled
* **dns_support** (*boolean*): Indicate if the the DNS Support flag is enabled
* **tags** (*key_value[]*): List of tags


### VPC Subnet

**Properties**

* **id** (*string*): VPC Subnet ID
* **region** (*string*): AWS Region
* **availability_zone** (*string*): AWS Availability Zone
* **cidr_block** (*string*): CIDR Block
* **state** (*string*): State
* **tags** (*key_value[]*): List of tags

**Associations**

* **vpc** (*aws:ec2:vpc*): VPC

### Amazon Machine Image

**Properties**

* **id** (*string*): Instance profile ID
* **name** (*string*): Instance profile name
* **path** (*string*): Path


### S3 Bucket

**Properties**

* **region** (*string*): AWS Region
* **bucket_name** (*string*): Bucket name
* **encryption** (*nested*): Encryption
    * **enabled** (*boolean*): Encryption is enabled
    * **type** (*string*): Encryption type
* **logging** (*nested*): Logging
    * **enabled** (*boolean*): Logging is enabled


### SQS Queue

**Properties**

* **queue_arn** (*string*): Queue Arn
* **visibility_timeout** (*integer*): Visibility timeout
* **delay_seconds** (*integer*): Delay in seconds
* **message_retention_period** (*integer*): Message retention period


### SNS Topic

**Properties**

* **topic_arn** (*string*): Topic Arn
* **enabled** (*boolean*): Enabled


