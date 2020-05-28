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
import cloudspec.aws.AWSResource;
import cloudspec.model.KeyValue;
import software.amazon.awssdk.services.ec2.model.Tag;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class EC2Resource extends AWSResource {
    public static final String GROUP_NAME = "ec2";

    @PropertyDefinition(
            name = "region",
            description = "The AWS region",
            exampleValues = "us-east-1 | eu-west-1"
    )
    protected String region;

    public EC2Resource(String ownerId, String region) {
        super(ownerId);
        this.region = region;
    }

    protected static List<KeyValue> tagsFromSdk(List<Tag> tags) {
        if (Objects.isNull(tags)) {
            return Collections.emptyList();
        }

        return tags.stream()
                .map(EC2Resource::tagFromSdk)
                .collect(Collectors.toList());
    }

    protected static KeyValue tagFromSdk(Tag tag) {
        if (Objects.isNull(tag)) {
            return null;
        }

        return new KeyValue(
                tag.key(), tag.value()
        );
    }
}
