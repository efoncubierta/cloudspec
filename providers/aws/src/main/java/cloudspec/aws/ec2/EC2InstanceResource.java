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
package cloudspec.aws.ec2;

import cloudspec.aws.AWSResourceDef;
import cloudspec.model.ResourceAttribute;
import cloudspec.model.ResourceFunction;
import cloudspec.model.StringResourceAttribute;

import java.util.ArrayList;
import java.util.List;

public class EC2InstanceResource extends EC2Resource {
    private final List<ResourceAttribute> attributes = new ArrayList<>();
    private final List<ResourceFunction> functions = new ArrayList<>();

    public EC2InstanceResource(String region, String availabilityZone, String instanceId, String instanceType, String vpcId) {
        // TODO manage null values

        attributes.add(
                new StringResourceAttribute(
                        AWSResourceDef.ATTR_REGION,
                        region
                )
        );

        attributes.add(
                new StringResourceAttribute(
                        AWSResourceDef.ATTR_AVAILABILITY_ZONE,
                        availabilityZone
                )
        );

        attributes.add(
                new StringResourceAttribute(
                        EC2InstanceResourceDef.ATTR_INSTANCE_ID,
                        instanceId
                )
        );

        attributes.add(
                new StringResourceAttribute(
                        EC2InstanceResourceDef.ATTR_INSTANCE_TYPE,
                        instanceType
                )
        );

        attributes.add(
                new StringResourceAttribute(
                        EC2InstanceResourceDef.ATTR_VPC_ID,
                        vpcId
                )
        );
    }

    @Override
    public List<ResourceAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public List<ResourceFunction> getFunctions() {
        return functions;
    }
}
