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
import software.amazon.awssdk.arns.Arn
import software.amazon.awssdk.services.ec2.model.Subnet
import software.amazon.awssdk.services.ec2.model.SubnetState

object SubnetGenerator : BaseGenerator() {
    @kotlin.jvm.JvmStatic
    fun subnetId(): String {
        return "subnet-${faker.lorem().characters(30)}"
    }

    fun subnetArn(): Arn {
        return Arn.builder()
                .service("ec2")
                .region(CommonGenerator.region().id())
                .accountId(CommonGenerator.accountId())
                .partition("subnet")
                .resource(subnetId())
                .build()
    }

    fun subnetState(): SubnetState {
        return valueFromArray(SubnetState.values())
    }

    fun subnets(n: Int? = null): List<Subnet> {
        return listGenerator(n) { subnet() }
    }

    fun subnet(): Subnet {
        return Subnet.builder()
                .availabilityZone(CommonGenerator.availabilityZone())
                .availabilityZoneId(CommonGenerator.availabilityZone())
                .availableIpAddressCount(faker.random().nextInt(1, 1024))
                .assignIpv6AddressOnCreation(faker.random().nextBoolean())
                .cidrBlock(faker.internet().ipV4Cidr())
                .defaultForAz(faker.random().nextBoolean())
                .mapPublicIpOnLaunch(faker.random().nextBoolean())
                .state(subnetState())
                .subnetId(subnetId())
                .vpcId(VpcGenerator.vpcId())
                .ownerId(CommonGenerator.accountId())
                .assignIpv6AddressOnCreation(faker.random().nextBoolean())
                .ipv6CidrBlockAssociationSet(SubnetIpv6CidrBlockAssociationGenerator.subnetIpv6CidrBlockAssociation())
                .tags(TagGenerator.tags())
                .subnetArn(subnetArn().toString())
                .outpostArn(OutpostGenerator.outpostArn().toString())
                .build()
    }
}
