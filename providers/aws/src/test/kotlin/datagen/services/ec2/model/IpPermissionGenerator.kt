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
import software.amazon.awssdk.services.ec2.model.IpPermission

object IpPermissionGenerator : BaseGenerator() {
    fun ipPermissions(n: Int? = null): List<IpPermission> {
        return listGenerator(n) { ipPermission() }
    }

    fun ipPermission(): IpPermission {
        return IpPermission.builder()
                .fromPort(faker.random().nextInt(1, 65535))
                .ipProtocol(faker.lorem().word())// TODO realistic value
                .ipRanges(IpRangeGenerator.ipRanges())
                .ipv6Ranges(Ipv6RangeGenerator.ipv6Ranges())
                .prefixListIds(PrefixListIdGenerator.prefixListIds())
                .toPort(faker.random().nextInt(1, 65535))
                .userIdGroupPairs(UserIdGroupPairGenerator.userIdGroupPairs())
                .build()
    }
}
