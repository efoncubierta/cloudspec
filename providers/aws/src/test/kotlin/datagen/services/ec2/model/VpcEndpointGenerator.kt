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
import software.amazon.awssdk.services.ec2.model.State
import software.amazon.awssdk.services.ec2.model.VpcEndpoint
import software.amazon.awssdk.services.ec2.model.VpcEndpointType

object VpcEndpointGenerator : BaseGenerator() {
    fun vpcEndpointId(): String {
        return "vpce-${faker.random().hex(30)}"
    }

    fun vpcEndpoints(n: Int? = null): List<VpcEndpoint> {
        return listGenerator(n) { vpcEndpoint() }
    }

    fun vpcEndpoint(): VpcEndpoint {
        return VpcEndpoint.builder()
                .vpcEndpointId(vpcEndpointId())
                .vpcEndpointType(valueFromArray(VpcEndpointType.values()))
                .vpcId(VpcGenerator.vpcId())
                .serviceName(faker.lorem().word()) // TODO realistic value
                .state(valueFromArray(State.values()))
                .policyDocument(faker.lorem().word()) // TODO realistic value
                .routeTableIds(
                        listGenerator { RouteTableGenerator.routeTableId() }
                )
                .subnetIds(
                        listGenerator { SubnetGenerator.subnetId() }
                )
                .groups(
                        listGenerator { SecurityGroupIdentifierGenerator.securityGroupIdentifier() }
                )
                .privateDnsEnabled(faker.random().nextBoolean())
                .requesterManaged(faker.random().nextBoolean())
                .networkInterfaceIds(
                        listGenerator { NetworkInterfaceGenerator.networkInterfaceId() }
                )
                .dnsEntries(
                        listGenerator { DnsEntryGenerator.dnsEntry() }
                )
                .creationTimestamp(pastInstant())
                .tags(TagGenerator.tags())
                .ownerId(CommonGenerator.accountId())
                .lastError(LastErrorGenerator.lastError())
                .build()
    }
}
