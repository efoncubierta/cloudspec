package cloudspec.aws;

import cloudspec.aws.ec2.EC2InstanceLoader;
import cloudspec.model.Provider;
import cloudspec.model.ResourceAttributeDef;
import cloudspec.model.ResourceAttributeType;
import cloudspec.model.ResourceDef;

import java.util.Arrays;

public class AWSProvider {
    public static final String PROVIDER_NAME = "aws";

    public static final String GROUP_EC2 = "ec2";
    public static final String TYPE_EC2_INSTANCE = "instance";
    public static final String ATTRIBUTE_INSTANCE_ID = "instance_id";
    public static final String ATTRIBUTE_INSTANCE_TYPE = "instance_type";

    public static Provider provider() {
        return new Provider(
                PROVIDER_NAME,
                Arrays.asList(
                        // EC2 Instance
                        new ResourceDef(
                                PROVIDER_NAME,
                                GROUP_EC2,
                                TYPE_EC2_INSTANCE,
                                Arrays.asList(
                                        new ResourceAttributeDef(
                                                ATTRIBUTE_INSTANCE_ID,
                                                "Instance ID",
                                                ResourceAttributeType.STRING, Boolean.FALSE),
                                        new ResourceAttributeDef(
                                                ATTRIBUTE_INSTANCE_TYPE,
                                                "Instance type",
                                                ResourceAttributeType.STRING, Boolean.FALSE)
                                ),
                                Arrays.asList(),
                                new EC2InstanceLoader())
                ));
    }
}
