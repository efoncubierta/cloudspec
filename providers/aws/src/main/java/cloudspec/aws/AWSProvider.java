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
package cloudspec.aws;

import cloudspec.aws.ec2.EC2InstanceLoader;
import cloudspec.aws.ec2.EC2InstanceResourceDef;
import cloudspec.core.Provider;
import cloudspec.core.ResourceDef;

import java.util.Arrays;
import java.util.List;

public class AWSProvider implements Provider {
    public static final String PROVIDER_NAME = "aws";

    public static final List<ResourceDef> resourceDefs = Arrays.asList(
            new EC2InstanceResourceDef(new EC2InstanceLoader())
    );

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public List<ResourceDef> getResourceDefs() {
        return resourceDefs;
    }
}
