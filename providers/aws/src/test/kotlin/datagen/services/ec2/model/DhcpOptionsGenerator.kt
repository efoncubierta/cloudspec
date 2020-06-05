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
import datagen.CommonGenerator
import software.amazon.awssdk.services.ec2.model.AttributeValue
import software.amazon.awssdk.services.ec2.model.DhcpConfiguration
import software.amazon.awssdk.services.ec2.model.DhcpOptions

object DhcpOptionsGenerator : BaseGenerator() {
    fun dhcpOptionsId(): String {
        return "dopt-${faker.random().hex(30)}"
    }

    fun dhcpConfiguration(): DhcpConfiguration {
        // TODO use real configuration keys and values
        return DhcpConfiguration.builder()
                .key(faker.lorem().word())
                .values(
                        AttributeValue.builder()
                                .value(faker.lorem().word())
                                .build()
                )
                .build()
    }

    fun dhcpOptionsList(n: Int? = null): List<DhcpOptions> {
        return listGenerator(n) { dhcpOptions() }
    }

    fun dhcpOptions(): DhcpOptions {
        return DhcpOptions.builder()
                .dhcpConfigurations(dhcpConfiguration())
                .dhcpOptionsId(dhcpOptionsId())
                .ownerId(CommonGenerator.accountId())
                .tags(TagGenerator.tags())
                .build()
    }
}
