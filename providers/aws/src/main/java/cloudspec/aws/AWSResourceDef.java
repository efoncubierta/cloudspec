package cloudspec.aws;

import cloudspec.core.ResourceAttributeDef;
import cloudspec.core.ResourceAttributeType;
import cloudspec.core.ResourceDef;

public abstract class AWSResourceDef implements ResourceDef {
    public static final String PROVIDER_NAME = "aws";

    public static final String ATTR_REGION = "region";
    public static final String ATTR_AVAILABILITY_ZONE = "availability_zone";

    public static final ResourceAttributeDef ATTR_DEF_REGION = new ResourceAttributeDef(
            ATTR_REGION,
            "AWS Region",
            ResourceAttributeType.STRING,
            Boolean.FALSE
    );

    public static final ResourceAttributeDef ATTR_DEF_AVAILABILITY_ZONE = new ResourceAttributeDef(
            ATTR_AVAILABILITY_ZONE,
            "AWS Availability Zone",
            ResourceAttributeType.STRING,
            Boolean.FALSE
    );

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }
}
