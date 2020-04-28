package cloudspec.aws;

import cloudspec.aws.ec2.EC2InstanceLoader;
import cloudspec.aws.ec2.EC2InstanceResourceDef;
import cloudspec.core.Provider;
import cloudspec.core.ResourceDef;

import java.util.Arrays;
import java.util.List;

public class AWSProvider implements Provider {
    public static final String PROVIDER_NAME = "aws";

    public static final List<ResourceDef> resourceDefs = Arrays.asList(
            new EC2InstanceResourceDef(new EC2InstanceLoader())
    );

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public List<ResourceDef> getResourceDefs() {
        return resourceDefs;
    }
}
