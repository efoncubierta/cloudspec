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

import cloudspec.annotation.AssociationDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.aws.ec2.resource.EC2InstanceResource;
import software.amazon.awssdk.services.ec2.model.NetworkInterfaceAttachment;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public class EC2NetworkInterfaceAttachment {
    @PropertyDefinition(
            name = "attach_time",
            description = "The timestamp indicating when the attachment initiated"
    )
    private final Instant attachTime;

//    @AssociationDefinition(
//            name = "attachment_id",
//            description = ""
//    )
//    private final String attachmentId;

    @PropertyDefinition(
            name = "delete_on_termination",
            description = "Indicates whether the network interface is deleted when the instance is terminated"
    )
    private final Boolean deleteOnTermination;

    @AssociationDefinition(
            name = "instance",
            description = "The instance",
            targetClass = EC2InstanceResource.class
    )
    private final String instanceId;

    @PropertyDefinition(
            name = "instance_owner_id",
            description = "The AWS account ID of the owner of the instance"
    )
    private final String instanceOwnerId;

    @PropertyDefinition(
            name = "status",
            description = "The attachment state"
    )
    private final String status;

    public EC2NetworkInterfaceAttachment(Instant attachTime, Boolean deleteOnTermination, String instanceId,
                                         String instanceOwnerId, String status) {
        this.attachTime = attachTime;
        this.deleteOnTermination = deleteOnTermination;
        this.instanceId = instanceId;
        this.instanceOwnerId = instanceOwnerId;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof NetworkInterfaceAttachment))) {
            return false;
        }

        if (o instanceof NetworkInterfaceAttachment) {
            return sdkEquals((NetworkInterfaceAttachment) o);
        }

        EC2NetworkInterfaceAttachment that = (EC2NetworkInterfaceAttachment) o;
        return Objects.equals(attachTime, that.attachTime) &&
                Objects.equals(deleteOnTermination, that.deleteOnTermination) &&
                Objects.equals(instanceId, that.instanceId) &&
                Objects.equals(instanceOwnerId, that.instanceOwnerId) &&
                Objects.equals(status, that.status);
    }

    private boolean sdkEquals(NetworkInterfaceAttachment that) {
        return Objects.equals(attachTime, that.attachTime()) &&
                Objects.equals(deleteOnTermination, that.deleteOnTermination()) &&
                Objects.equals(instanceId, that.instanceId()) &&
                Objects.equals(instanceOwnerId, that.instanceOwnerId()) &&
                Objects.equals(status, that.statusAsString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(attachTime, deleteOnTermination, instanceId, instanceOwnerId, status);
    }

    public static EC2NetworkInterfaceAttachment fromSdk(NetworkInterfaceAttachment networkInterfaceAttachment) {
        return Optional.ofNullable(networkInterfaceAttachment)
                       .map(v -> new EC2NetworkInterfaceAttachment(
                                       v.attachTime(),
                                       v.deleteOnTermination(),
                                       v.instanceId(),
                                       v.instanceOwnerId(),
                                       v.statusAsString()
                               )
                       )
                       .orElse(null);
    }
}
