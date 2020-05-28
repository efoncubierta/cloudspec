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
import software.amazon.awssdk.services.ec2.model.AttributeValue;
import software.amazon.awssdk.services.ec2.model.DhcpConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EC2DhcpConfiguration {
    @PropertyDefinition(
            name = "key",
            description = "The name of a DHCP option"
    )
    public String key;

    @PropertyDefinition(
            name = "values",
            description = "One or more values for the DHCP option"
    )
    public List<String> values;

    public EC2DhcpConfiguration(String key, List<String> values) {
        this.key = key;
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2DhcpConfiguration that = (EC2DhcpConfiguration) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, values);
    }

    public static List<EC2DhcpConfiguration> fromSdk(List<DhcpConfiguration> dhcpConfigurations) {
        if (Objects.isNull(dhcpConfigurations)) {
            return Collections.emptyList();
        }

        return dhcpConfigurations.stream()
                .map(EC2DhcpConfiguration::fromSdk)
                .collect(Collectors.toList());
    }

    public static EC2DhcpConfiguration fromSdk(DhcpConfiguration dhcpConfiguration) {
        if (Objects.isNull(dhcpConfiguration)) {
            return null;
        }

        return new EC2DhcpConfiguration(
                dhcpConfiguration.key(),
                EC2DhcpConfiguration.valuesFromSdk(dhcpConfiguration.values())
        );
    }

    public static List<String> valuesFromSdk(List<AttributeValue> attributeValues) {
        if (Objects.isNull(attributeValues)) {
            return Collections.emptyList();
        }

        return attributeValues.stream()
                .map(AttributeValue::value)
                .collect(Collectors.toList());
    }
}