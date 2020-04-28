package cloudspec.aws.ec2;

import cloudspec.aws.AWSResourceDef;
import cloudspec.core.ResourceAttribute;
import cloudspec.core.ResourceFunction;
import cloudspec.core.StringResourceAttribute;

import java.util.ArrayList;
import java.util.List;

public class EC2InstanceResource extends EC2Resource {
    private final List<ResourceAttribute> attributes = new ArrayList<>();
    private final List<ResourceFunction> functions = new ArrayList<>();

    public EC2InstanceResource(String region, String availabilityZone, String instanceId, String instanceType, String vpcId) {
        // TODO manage null values

        attributes.add(
                new StringResourceAttribute(
                        AWSResourceDef.ATTR_REGION,
                        region
                )
        );

        attributes.add(
                new StringResourceAttribute(
                        AWSResourceDef.ATTR_AVAILABILITY_ZONE,
                        availabilityZone
                )
        );

        attributes.add(
                new StringResourceAttribute(
                        EC2InstanceResourceDef.ATTR_INSTANCE_ID,
                        instanceId
                )
        );

        attributes.add(
                new StringResourceAttribute(
                        EC2InstanceResourceDef.ATTR_INSTANCE_TYPE,
                        instanceType
                )
        );

        attributes.add(
                new StringResourceAttribute(
                        EC2InstanceResourceDef.ATTR_VPC_ID,
                        vpcId
                )
        );
    }

    @Override
    public List<ResourceAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public List<ResourceFunction> getFunctions() {
        return functions;
    }
}
