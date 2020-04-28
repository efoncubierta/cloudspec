package cloudspec.aws.ec2;

import cloudspec.aws.AWSResourceDef;
import cloudspec.core.ResourceAttributeDef;
import cloudspec.core.ResourceAttributeType;

public abstract class EC2ResourceDef extends AWSResourceDef {
    public static final String GROUP_NAME = "ec2";

    public static final String ATTR_VPC_ID = "vpc_id";

    public static final ResourceAttributeDef ATTR_DEF_VPC_ID = new ResourceAttributeDef(
            ATTR_VPC_ID,
            "VPC id",
            ResourceAttributeType.STRING, Boolean.FALSE);

    @Override
    public String getGroupName() {
        return GROUP_NAME;
    }
}
