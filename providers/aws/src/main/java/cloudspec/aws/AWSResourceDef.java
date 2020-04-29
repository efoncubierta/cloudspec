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

import cloudspec.model.PropertyDef;
import cloudspec.model.PropertyType;
import cloudspec.model.ResourceDef;

public abstract class AWSResourceDef implements ResourceDef {
    public static final String PROVIDER_NAME = "aws";

    public static final String PROP_REGION = "region";
    public static final String PROP_AVAILABILITY_ZONE = "availability_zone";

    public static final PropertyDef PROP_DEF_REGION = new PropertyDef(
            PROP_REGION,
            "AWS Region",
            PropertyType.STRING,
            Boolean.FALSE
    );

    public static final PropertyDef PROP_DEF_AVAILABILITY_ZONE = new PropertyDef(
            PROP_AVAILABILITY_ZONE,
            "AWS Availability Zone",
            PropertyType.STRING,
            Boolean.FALSE
    );
}
