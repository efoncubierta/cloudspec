package cloudspec.aws.ec2;

import cloudspec.core.ResourceAttributeDef;
import cloudspec.core.ResourceAttributeType;
import cloudspec.core.ResourceFunctionDef;
import cloudspec.core.ResourceLoader;

import java.util.Arrays;
import java.util.List;

public class EC2InstanceResourceDef extends EC2ResourceDef {
    public static final String RESOURCE_NAME = "instance";
    public static final String ATTR_INSTANCE_ID = "instance_id";
    public static final String ATTR_INSTANCE_TYPE = "instance_type";

    public final ResourceAttributeDef ATTR_DEF_INSTANCE_ID = new ResourceAttributeDef(
            ATTR_INSTANCE_ID,
            "Instance ID",
            ResourceAttributeType.STRING, Boolean.FALSE);

    public final ResourceAttributeDef ATTR_DEF_INSTANCE_TYPE = new ResourceAttributeDef(
            ATTR_INSTANCE_TYPE,
            "Instance type",
            ResourceAttributeType.STRING, Boolean.FALSE);

    private final EC2InstanceLoader loader;

    public EC2InstanceResourceDef(EC2InstanceLoader loader) {
        this.loader = loader;
    }

    @Override
    public String getResourceName() {
        return RESOURCE_NAME;
    }

    @Override
    public List<ResourceAttributeDef> getAttributesDefinitions() {
        return Arrays.asList(
                ATTR_DEF_REGION,
                ATTR_DEF_AVAILABILITY_ZONE,
                ATTR_DEF_INSTANCE_ID,
                ATTR_DEF_INSTANCE_TYPE,
                ATTR_DEF_VPC_ID
        );
    }

    @Override
    public List<ResourceFunctionDef> getFunctionsDefinitions() {
        return Arrays.asList();
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return loader;
    }
}
