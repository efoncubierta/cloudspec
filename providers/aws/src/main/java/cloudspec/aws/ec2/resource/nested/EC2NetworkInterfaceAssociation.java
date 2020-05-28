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
import software.amazon.awssdk.services.ec2.model.NetworkInterfaceAssociation;

import java.util.Objects;

public class EC2NetworkInterfaceAssociation {

//    @AssociationDefinition(
//            name = "allocation",
//            description = "The allocation"
//    )
//    private final String allocationId;

//    @AssociationDefinition(
//            name = "association",
//            description = "The association"
//    )
//    private final String associationId;

    @PropertyDefinition(
            name = "ip_owner_id",
            description = "The ID of the Elastic IP address owner"
    )
    private final String ipOwnerId;

    @PropertyDefinition(
            name = "public_dns_name",
            description = "The public DNS name"
    )
    private final String publicDnsName;

    @PropertyDefinition(
            name = "public_ip",
            description = "The address of the Elastic IP address bound to the network interface"
    )
    private final String publicIp;

    public EC2NetworkInterfaceAssociation(String ipOwnerId, String publicDnsName, String publicIp) {
        this.ipOwnerId = ipOwnerId;
        this.publicDnsName = publicDnsName;
        this.publicIp = publicIp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2NetworkInterfaceAssociation that = (EC2NetworkInterfaceAssociation) o;
        return Objects.equals(ipOwnerId, that.ipOwnerId) &&
                Objects.equals(publicDnsName, that.publicDnsName) &&
                Objects.equals(publicIp, that.publicIp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipOwnerId, publicDnsName, publicIp);
    }

    public static EC2NetworkInterfaceAssociation fromSdk(NetworkInterfaceAssociation networkInterfaceAssociation) {
        if (Objects.isNull(networkInterfaceAssociation)) {
            return null;
        }

        return new EC2NetworkInterfaceAssociation(
                networkInterfaceAssociation.ipOwnerId(),
                networkInterfaceAssociation.publicDnsName(),
                networkInterfaceAssociation.publicIp()
        );
    }
}
