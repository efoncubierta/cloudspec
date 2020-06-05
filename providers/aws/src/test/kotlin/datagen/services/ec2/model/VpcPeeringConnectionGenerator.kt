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
import software.amazon.awssdk.services.ec2.model.VpcPeeringConnection

object VpcPeeringConnectionGenerator : BaseGenerator() {
    fun vpcPeeringConnectionId(): String {
        return "pcx-${faker.random().hex(30)}"
    }

    fun vpcPeeringConnections(n: Int? = null): List<VpcPeeringConnection> {
        return listGenerator(n) { vpcPeeringConnection() }
    }

    fun vpcPeeringConnection(): VpcPeeringConnection {
        return VpcPeeringConnection.builder()
                .accepterVpcInfo(VpcPeeringConnectionVpcInfoGenerator.vpcPeeringConnectionVpcInfo())
                .expirationTime(futureInstant())
                .requesterVpcInfo(VpcPeeringConnectionVpcInfoGenerator.vpcPeeringConnectionVpcInfo())
                .status(VpcPeeringConnectionStateReasonGenerator.vpcPeeringConnectionStateReason())
                .vpcPeeringConnectionId(vpcPeeringConnectionId())
                .build()
    }
}
