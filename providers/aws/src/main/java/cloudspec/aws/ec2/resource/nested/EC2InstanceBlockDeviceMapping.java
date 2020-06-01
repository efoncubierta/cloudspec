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
import software.amazon.awssdk.services.ec2.model.InstanceBlockDeviceMapping;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class EC2InstanceBlockDeviceMapping {
    @PropertyDefinition(
            name = "device_name",
            description = ""
    )
    private final String deviceName;

    @PropertyDefinition(
            name = "ebs",
            description = ""
    )
    private final EC2EbsInstanceBlockDevice ebs;

    public EC2InstanceBlockDeviceMapping(String deviceName, EC2EbsInstanceBlockDevice ebs) {
        this.deviceName = deviceName;
        this.ebs = ebs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof InstanceBlockDeviceMapping))) {
            return false;
        }

        if (o instanceof InstanceBlockDeviceMapping) {
            return sdkEquals((InstanceBlockDeviceMapping) o);
        }

        EC2InstanceBlockDeviceMapping that = (EC2InstanceBlockDeviceMapping) o;
        return Objects.equals(deviceName, that.deviceName) &&
                Objects.equals(ebs, that.ebs);
    }

    private boolean sdkEquals(InstanceBlockDeviceMapping that) {
        return Objects.equals(deviceName, that.deviceName()) &&
                Objects.equals(ebs, that.ebs());
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceName, ebs);
    }

    public static List<EC2InstanceBlockDeviceMapping> fromSdk(List<InstanceBlockDeviceMapping> instanceBlockDeviceMappings) {
        return Optional.ofNullable(instanceBlockDeviceMappings)
                       .orElse(Collections.emptyList())
                       .stream()
                       .map(EC2InstanceBlockDeviceMapping::fromSdk)
                       .collect(Collectors.toList());
    }

    public static EC2InstanceBlockDeviceMapping fromSdk(InstanceBlockDeviceMapping instanceBlockDeviceMapping) {
        return Optional.ofNullable(instanceBlockDeviceMapping)
                       .map(v -> new EC2InstanceBlockDeviceMapping(
                                       v.deviceName(),
                                       EC2EbsInstanceBlockDevice.fromSdk(v.ebs())
                               )
                       )
                       .orElse(null);
    }
}
