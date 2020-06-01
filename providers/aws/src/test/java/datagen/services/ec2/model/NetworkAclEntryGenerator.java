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
import software.amazon.awssdk.services.ec2.model.NetworkAclEntry;
import software.amazon.awssdk.services.ec2.model.RuleAction;

import java.util.List;

public class NetworkAclEntryGenerator extends BaseGenerator {
    public static RuleAction ruleAction() {
        return fromArray(RuleAction.values());
    }

    public static List<NetworkAclEntry> networkAclEntries() {
        return networkAclEntryies(faker.random().nextInt(1, 10));
    }

    public static List<NetworkAclEntry> networkAclEntryies(Integer n) {
        return listGenerator(n, NetworkAclEntryGenerator::networkAclEntry);
    }

    public static NetworkAclEntry networkAclEntry() {
        return NetworkAclEntry.builder()
                              .cidrBlock(faker.internet().ipV4Cidr())
                              .egress(faker.random().nextBoolean())
                              .icmpTypeCode(IcmpTypeCodeGenerator.icmpTypeCode())
                              .ipv6CidrBlock(faker.internet().ipV6Cidr())
                              .portRange(PortRangeGenerator.portRange())
                              .protocol(faker.lorem().word()) // TODO use real value
                              .ruleAction(ruleAction())
                              .ruleNumber(faker.random().nextInt(1, 100))
                              .build();
    }
}
