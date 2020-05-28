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
import software.amazon.awssdk.services.ec2.model.BlockDeviceMapping;
import software.amazon.awssdk.services.ec2.model.EbsBlockDevice;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EC2BlockDeviceMapping {
    @PropertyDefinition(
            name = "device_name",
            description = "The device name",
            exampleValues = "/dev/sdh"
    )
    private String deviceName;

    @PropertyDefinition(
            name = "virtual_name",
            description = "The virtual device name"
    )
    private String virtualName;

    @PropertyDefinition(
            name = "ebs",
            description = "Parameters used to automatically set up EBS volumes when the instance is launched"
    )
    private EC2EbsBlockDevice ebs;

    public EC2BlockDeviceMapping(String deviceName, String virtualName, EC2EbsBlockDevice ebs) {
        this.deviceName = deviceName;
        this.virtualName = virtualName;
        this.ebs = ebs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2BlockDeviceMapping that = (EC2BlockDeviceMapping) o;
        return Objects.equals(deviceName, that.deviceName) &&
                Objects.equals(virtualName, that.virtualName) &&
                Objects.equals(ebs, that.ebs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceName, virtualName, ebs);
    }

    public static List<EC2BlockDeviceMapping> fromSdk(List<BlockDeviceMapping> blockDeviceMappings) {
        if (Objects.isNull(blockDeviceMappings)) {
            return Collections.emptyList();
        }

        return blockDeviceMappings.stream()
                .map(EC2BlockDeviceMapping::fromSdk)
                .collect(Collectors.toList());
    }

    public static EC2BlockDeviceMapping fromSdk(BlockDeviceMapping blockDeviceMapping) {
        if (Objects.isNull(blockDeviceMapping)) {
            return null;
        }

        return new EC2BlockDeviceMapping(
                blockDeviceMapping
                        .getValueForField("DeviceName", String.class)
                        .orElse(null),
                blockDeviceMapping
                        .getValueForField("VirtualName", String.class)
                        .orElse(null),
                blockDeviceMapping
                        .getValueForField("Ebs", EbsBlockDevice.class)
                        .map(EC2EbsBlockDevice::fromSdk)
                        .orElse(null)
        );
    }
}
