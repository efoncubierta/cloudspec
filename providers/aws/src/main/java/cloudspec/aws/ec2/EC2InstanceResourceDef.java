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

import cloudspec.model.*;

import java.util.Arrays;
import java.util.List;

public class EC2InstanceResourceDef extends EC2ResourceDef {
    public static final String RESOURCE_NAME = "instance";
    public static final ResourceFqn RESOURCE_FQN = new ResourceFqn(PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME);
    public static final String PROP_INSTANCE_ID = "instance_id";
    public static final String PROP_INSTANCE_TYPE = "instance_type";

    public final PropertyDef PROP_DEF_INSTANCE_ID = new PropertyDef(
            PROP_INSTANCE_ID,
            "Instance ID",
            PropertyType.STRING, Boolean.FALSE);

    public final PropertyDef PROP_DEF_INSTANCE_TYPE = new PropertyDef(
            PROP_INSTANCE_TYPE,
            "Instance type",
            PropertyType.STRING, Boolean.FALSE);

    private final EC2InstanceLoader loader;

    public EC2InstanceResourceDef(EC2InstanceLoader loader) {
        this.loader = loader;
    }

    @Override
    public ResourceFqn getFqn() {
        return RESOURCE_FQN;
    }

    @Override
    public List<PropertyDef> getPropertyDefs() {
        return Arrays.asList(
                PROP_DEF_REGION,
                PROP_DEF_AVAILABILITY_ZONE,
                PROP_DEF_INSTANCE_ID,
                PROP_DEF_INSTANCE_TYPE,
                PROP_DEF_VPC_ID
        );
    }

    @Override
    public List<FunctionDef> getFunctionDefs() {
        return Arrays.asList();
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return loader;
    }
}
