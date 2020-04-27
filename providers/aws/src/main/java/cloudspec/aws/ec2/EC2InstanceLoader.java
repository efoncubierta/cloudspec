package cloudspec.aws.ec2;

import cloudspec.aws.AWSProvider;
import cloudspec.model.Resource;
import cloudspec.model.ResourceLoader;
import cloudspec.model.StringResourceAttribute;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.utils.IoUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EC2InstanceLoader implements ResourceLoader {
    private List<Resource> instances;

    public List<Resource> load() {
        if (instances != null) {
            return instances;
        }

        // TODO configure region
        Ec2Client client = getEc2Client(Region.EU_WEST_1);

        try {
            DescribeInstancesResponse response = client.describeInstances();
            instances = response.reservations()
                    .stream()
                    .flatMap(reservation -> reservation.instances().stream())
                    .map(this::mapToResource)
                    .collect(Collectors.toList());
        } finally {
            IoUtils.closeQuietly(client, null);
        }

        return instances;
    }

    private Resource mapToResource(Instance instance) {

        return new Resource(
                Arrays.asList(
                        new StringResourceAttribute(AWSProvider.ATTRIBUTE_INSTANCE_ID, instance.instanceId()),
                        new StringResourceAttribute(AWSProvider.ATTRIBUTE_INSTANCE_TYPE, instance.instanceType().toString())),
                Collections.emptyList()
        );
    }

    private Ec2Client getEc2Client(Region region) {
        return Ec2Client.builder().region(region).build();
    }
}
