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
import cloudspec.model.Function;
import cloudspec.model.Property;
import cloudspec.model.ResourceFqn;
import cloudspec.model.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class EC2InstanceResource extends EC2Resource {
    private final List<Property> properties = new ArrayList<>();
    private final List<Function> functions = new ArrayList<>();

    public EC2InstanceResource(String region, String availabilityZone, String instanceId, String instanceType, String vpcId) {
        // TODO manage null values

        properties.add(
                new StringProperty(
                        AWSResourceDef.PROP_REGION,
                        region
                )
        );

        properties.add(
                new StringProperty(
                        AWSResourceDef.PROP_AVAILABILITY_ZONE,
                        availabilityZone
                )
        );

        properties.add(
                new StringProperty(
                        EC2InstanceResourceDef.PROP_INSTANCE_ID,
                        instanceId
                )
        );

        properties.add(
                new StringProperty(
                        EC2InstanceResourceDef.PROP_INSTANCE_TYPE,
                        instanceType
                )
        );

        properties.add(
                new StringProperty(
                        EC2InstanceResourceDef.PROP_VPC_ID,
                        vpcId
                )
        );
    }

    @Override
    public ResourceFqn getResourceFqn() {
        return EC2InstanceResourceDef.RESOURCE_FQN;
    }

    @Override
    public List<Property> getProperties() {
        return properties;
    }

    @Override
    public List<Function> getFunctions() {
        return functions;
    }
}
