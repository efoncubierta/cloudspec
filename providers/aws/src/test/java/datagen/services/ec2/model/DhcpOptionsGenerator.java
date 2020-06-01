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
package datagen.services.ec2.model;

import datagen.BaseGenerator;
import datagen.CommonGenerator;
import software.amazon.awssdk.services.ec2.model.AttributeValue;
import software.amazon.awssdk.services.ec2.model.DhcpConfiguration;
import software.amazon.awssdk.services.ec2.model.DhcpOptions;

import java.util.List;

public class DhcpOptionsGenerator extends BaseGenerator {
    public static String dhcpOptionsId() {
        return String.format("dopt-%s", faker.random().hex(10));
    }

    public static DhcpConfiguration dhcpConfiguration() {
        // TODO use real configuration keys and values
        return DhcpConfiguration.builder()
                                .key(faker.lorem().word())
                                .values(
                                        AttributeValue.builder()
                                                      .value(faker.lorem().word())
                                                      .build()
                                )
                                .build();
    }

    public static List<DhcpOptions> dhcpOptionsList() {
        return dhcpOptionsList(faker.random().nextInt(1, 10));
    }

    public static List<DhcpOptions> dhcpOptionsList(Integer n) {
        return listGenerator(n, DhcpOptionsGenerator::dhcpOptions);
    }

    public static DhcpOptions dhcpOptions() {
        return DhcpOptions.builder()
                          .dhcpConfigurations(dhcpConfiguration())
                          .dhcpOptionsId(dhcpOptionsId())
                          .ownerId(CommonGenerator.accountId())
                          .tags(TagGenerator.tags(5))
                          .build();
    }
}
