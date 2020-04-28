package cloudspec.aws.ec2;

import cloudspec.core.Resource;
import cloudspec.core.ResourceLoader;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.DescribeRegionsResponse;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.utils.IoUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EC2InstanceLoader implements ResourceLoader {
    private List<Resource> instances;

    public List<Resource> load() {
        if (instances != null) {
            return instances;
        }

        DescribeRegionsResponse response = getEc2Client(Optional.empty()).describeRegions();
        instances = response
                .regions()
                .stream()
                .flatMap(region -> loadFromRegion(region).stream())
                .collect(Collectors.toList());

        return instances;
    }

    private List<Resource> loadFromRegion(software.amazon.awssdk.services.ec2.model.Region region) {
        Ec2Client client = getEc2Client(Optional.of(region.regionName()));

        try {
            DescribeInstancesResponse response = client.describeInstances();
            return response.reservations()
                    .stream()
                    .flatMap(reservation -> reservation.instances().stream())
                    .map(instance -> mapToResource(region.regionName(), instance))
                    .collect(Collectors.toList());
        } finally {
            IoUtils.closeQuietly(client, null);
        }
    }

    private Resource mapToResource(String regionName, Instance instance) {
        return new EC2InstanceResource(regionName, "", instance.instanceId(), instance.instanceType().toString(), instance.vpcId());
    }

    private Ec2Client getEc2Client(Optional<String> regionOpt) {
        return regionOpt.isPresent() ? Ec2Client.builder().region(Region.of(regionOpt.get())).build() : Ec2Client.create();
    }
}
