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
import software.amazon.awssdk.services.ec2.model.Tenancy
import software.amazon.awssdk.services.ec2.model.Vpc
import software.amazon.awssdk.services.ec2.model.VpcState

object VpcGenerator : BaseGenerator() {
    fun vpcId(): String {
        return "vpc-${faker.random().hex(30)}"
    }

    fun vpcs(n: Int? = null): List<Vpc> {
        return listGenerator(n) { vpc() }
    }

    fun vpc(): Vpc {
        return Vpc.builder()
                .cidrBlock(faker.internet().ipV4Cidr())
                .dhcpOptionsId(DhcpOptionsGenerator.dhcpOptionsId())
                .state(
                        valueFromArray(VpcState.values())
                )
                .vpcId(vpcId())
                .ownerId(CommonGenerator.accountId())
                .instanceTenancy(
                        valueFromArray(Tenancy.values())
                )
                .ipv6CidrBlockAssociationSet(
                        listGenerator { VpcIpv6CidrBlockAssociationGenerator.vpcIpv6CidrBlockAssociation() }
                )
                .cidrBlockAssociationSet(
                        listGenerator { VpcCidrBlockAssociationGenerator.vpcCidrBlockAssociation() }
                )
                .isDefault(faker.random().nextBoolean())
                .tags(TagGenerator.tags())
                .build()
    }
}
