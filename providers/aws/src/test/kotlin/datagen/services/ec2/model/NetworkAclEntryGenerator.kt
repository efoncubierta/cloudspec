/*-
 * #%L
 * CloudSpec AWS Provider
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package datagen.services.ec2.model

import datagen.BaseGenerator
import software.amazon.awssdk.services.ec2.model.NetworkAclEntry
import software.amazon.awssdk.services.ec2.model.RuleAction

object NetworkAclEntryGenerator : BaseGenerator() {
    fun ruleAction(): RuleAction {
        return valueFromArray(RuleAction.values())
    }

    fun networkAclEntryies(n: Int? = null): List<NetworkAclEntry> {
        return listGenerator(n) { networkAclEntry() }
    }

    fun networkAclEntry(): NetworkAclEntry {
        return NetworkAclEntry.builder()
                .cidrBlock(faker.internet().ipV4Cidr())
                .egress(faker.random().nextBoolean())
                .icmpTypeCode(IcmpTypeCodeGenerator.icmpTypeCode())
                .ipv6CidrBlock(faker.internet().ipV6Cidr())
                .portRange(PortRangeGenerator.portRange())
                .protocol(faker.lorem().word()) // TODO use real value
                .ruleAction(ruleAction())
                .ruleNumber(faker.random().nextInt(1, 100))
                .build()
    }
}
