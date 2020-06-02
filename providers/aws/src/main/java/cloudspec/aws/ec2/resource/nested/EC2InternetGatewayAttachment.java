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
import cloudspec.aws.ec2.resource.EC2VpcResource;
import software.amazon.awssdk.services.ec2.model.InternetGatewayAttachment;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class EC2InternetGatewayAttachment {
    @PropertyDefinition(
            name = "state",
            description = "The current state of the attachment",
            exampleValues = "attaching | attached | detaching | detached"
    )
    private final String state;

    @AssociationDefinition(
            name = "vpc",
            description = "The VPC",
            targetClass = EC2VpcResource.class
    )
    private final String vpcId;

    public EC2InternetGatewayAttachment(String state, String vpcId) {
        this.state = state;
        this.vpcId = vpcId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof InternetGatewayAttachment))) {
            return false;
        }

        if (o instanceof InternetGatewayAttachment) {
            return sdkEquals((InternetGatewayAttachment) o);
        }

        EC2InternetGatewayAttachment that = (EC2InternetGatewayAttachment) o;
        return Objects.equals(state, that.state) &&
                Objects.equals(vpcId, that.vpcId);
    }

    private boolean sdkEquals(InternetGatewayAttachment that) {
        return Objects.equals(state, that.stateAsString()) &&
                Objects.equals(vpcId, that.vpcId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, vpcId);
    }

    public static List<EC2InternetGatewayAttachment> fromSdk(List<InternetGatewayAttachment> internetGatewayAttachments) {
        return Optional.ofNullable(internetGatewayAttachments)
                       .orElse(Collections.emptyList())
                       .stream()
                       .map(EC2InternetGatewayAttachment::fromSdk)
                       .collect(Collectors.toList());
    }

    public static EC2InternetGatewayAttachment fromSdk(InternetGatewayAttachment internetGatewayAttachment) {
        return Optional.ofNullable(internetGatewayAttachment)
                       .map(v ->
                               new EC2InternetGatewayAttachment(
                                       v.stateAsString(),
                                       v.vpcId()
                               )
                       )
                       .orElse(null);
    }
}
