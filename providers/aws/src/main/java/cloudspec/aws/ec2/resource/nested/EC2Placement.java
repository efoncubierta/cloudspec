/*-
 * #%L
 * CloudSpec AWS Provider
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
package cloudspec.aws.ec2.resource.nested;

import cloudspec.annotation.PropertyDefinition;
import software.amazon.awssdk.services.ec2.model.Placement;

import java.util.Objects;

public class EC2Placement {
    @PropertyDefinition(
            name = "availability_zone",
            description = "The Availability Zone of the instance"
    )
    private final String availabilityZone;

    @PropertyDefinition(
            name = "affinity",
            description = "The affinity setting for the instance on the Dedicated Host"
    )
    private final String affinity;

    @PropertyDefinition(
            name = "group_name",
            description = "The name of the placement group the instance is in"
    )
    private final String groupName;

    @PropertyDefinition(
            name = "partition_number",
            description = "The number of the partition the instance is in"
    )
    private final Integer partitionNumber;

//    @AssociationDefinition(
//            name = "host",
//            description = "The ID of the Dedicated Host on which the instance resides"
//    )
//    private final String hostId;

    @PropertyDefinition(
            name = "tenancy",
            description = "The tenancy of the instance (if the instance is running in a VPC)",
            exampleValues = "default | dedicated | host"
    )
    private final String tenancy;

    public EC2Placement(String availabilityZone, String affinity, String groupName,
                        Integer partitionNumber, String tenancy) {
        this.availabilityZone = availabilityZone;
        this.affinity = affinity;
        this.groupName = groupName;
        this.partitionNumber = partitionNumber;
        this.tenancy = tenancy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof Placement))) {
            return false;
        }

        if (o instanceof Placement) {
            return sdkEquals((Placement) o);
        }

        EC2Placement that = (EC2Placement) o;
        return Objects.equals(availabilityZone, that.availabilityZone) &&
                Objects.equals(affinity, that.affinity) &&
                Objects.equals(groupName, that.groupName) &&
                Objects.equals(partitionNumber, that.partitionNumber) &&
                Objects.equals(tenancy, that.tenancy);
    }

    private boolean sdkEquals(Placement that) {
        return Objects.equals(availabilityZone, that.availabilityZone()) &&
                Objects.equals(affinity, that.affinity()) &&
                Objects.equals(groupName, that.groupName()) &&
                Objects.equals(partitionNumber, that.partitionNumber()) &&
                Objects.equals(tenancy, that.tenancyAsString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(availabilityZone, affinity, groupName, partitionNumber, tenancy);
    }

    public static EC2Placement fromSdk(Placement placement) {
        return new EC2Placement(
                placement.availabilityZone(),
                placement.affinity(),
                placement.groupName(),
                placement.partitionNumber(),
                placement.tenancyAsString()
        );
    }
}
