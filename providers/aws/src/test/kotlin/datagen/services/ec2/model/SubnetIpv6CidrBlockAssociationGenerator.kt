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
import software.amazon.awssdk.services.ec2.model.SubnetIpv6CidrBlockAssociation
import java.util.*

object SubnetIpv6CidrBlockAssociationGenerator : BaseGenerator() {
    fun associationId(): String {
        // TODO realistic value
        return UUID.randomUUID().toString()
    }

    @JvmOverloads
    fun subnetIpv6CidrBlockAssociations(n: Int? = null): List<SubnetIpv6CidrBlockAssociation> {
        return listGenerator(n) { subnetIpv6CidrBlockAssociation() }
    }

    fun subnetIpv6CidrBlockAssociation(): SubnetIpv6CidrBlockAssociation {
        return SubnetIpv6CidrBlockAssociation.builder()
                .associationId(associationId())
                .ipv6CidrBlock(faker.internet().ipV6Cidr())
                .ipv6CidrBlockState(SubnetCidrBlockStateGenerator.subnetCidrBlockState())
                .build()
    }
}
