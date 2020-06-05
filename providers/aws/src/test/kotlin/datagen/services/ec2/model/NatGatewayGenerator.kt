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
import software.amazon.awssdk.services.ec2.model.NatGateway
import software.amazon.awssdk.services.ec2.model.NatGatewayState

object NatGatewayGenerator : BaseGenerator() {
    fun natGatewayId(): String {
        return "nat-${faker.random().hex(30)}"
    }

    fun natGateways(n: Int? = null): List<NatGateway> {
        return listGenerator(n) { natGateway() }
    }

    fun natGatewayState(): NatGatewayState {
        return valueFromArray(NatGatewayState.values())
    }

    fun natGateway(): NatGateway {
        return NatGateway.builder()
                .createTime(pastInstant())
                .deleteTime(pastInstant())
                .failureCode(faker.lorem().word()) // TODO use real value
                .failureMessage(faker.lorem().sentence()) // TODO use real value
                .natGatewayAddresses(NatGatewayAddressGenerator.natGatewayAddresses())
                .natGatewayId(natGatewayId())
                .provisionedBandwidth(ProvisionedBandwithGenerator.provisionedBandwidth())
                .state(natGatewayState())
                .subnetId(SubnetGenerator.subnetId())
                .vpcId(VpcGenerator.vpcId())
                .tags(TagGenerator.tags())
                .build()
    }
}
