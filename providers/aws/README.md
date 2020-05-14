# AWS Provisioner for CloudSpec

This module implements a provisioner for Amazon Web Services.

The key name of this provisioner is 'aws'.

## Resources

### EC2

The group name for all EC2 resources is 'ec2'.

#### EC2 Instance

- Reference **aws:ec2:vpc**

Properties:

| Name          | Type   | Multi-valued | Description       | Example   |
| ------------- | ------ | ------------ | ----------------- | --------- |
| region        | String | No           | The AWS region    | eu-west-1 |
| instance_id   | String | No           | The instance ID   | i-12345   |
| instance_type | String | No           | The instance type | m5.large  |

Associations:

#### VPC

- Reference **aws:ec2:vpc**

Properties:

| Name       | Type   | Multi-valued | Description    | Example    |
| ---------- | ------ | ------------ | -------------- | ---------- |
| region     | String | No           | The AWS region | eu-west-1  |
| vpc_id     | String | No           | The vpc ID     | vpc-1234   |
| cidr_block | String | No           | CIDR Block     | 10.0.0.0/8 |
| status     | String | No           | The vpc ID     | eu-west-1  |

#### VPC Subnet
