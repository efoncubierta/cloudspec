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
