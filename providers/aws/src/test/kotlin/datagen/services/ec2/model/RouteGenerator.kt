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
import software.amazon.awssdk.services.ec2.model.Route
import software.amazon.awssdk.services.ec2.model.RouteOrigin
import software.amazon.awssdk.services.ec2.model.RouteState

object RouteGenerator : BaseGenerator() {
    fun routeOrigin(): RouteOrigin {
        return valueFromArray(RouteOrigin.values())
    }

    fun routeState(): RouteState {
        return valueFromArray(RouteState.values())
    }

    fun routes(n: Int? = null): List<Route> {
        return listGenerator(n) { route() }
    }

    fun route(): Route {
        return Route.builder()
                .destinationCidrBlock(faker.internet().ipV4Cidr())
                .destinationIpv6CidrBlock(faker.internet().ipV6Cidr())
                .destinationPrefixListId(faker.lorem().word())  // TODO support real value
                .egressOnlyInternetGatewayId(InternetGatewayGenerator.internetGatewayId())
                .gatewayId(InternetGatewayGenerator.internetGatewayId())  // TODO support other types of gateways
                .instanceId(InstanceGenerator.instanceId())
                .instanceOwnerId(CommonGenerator.accountId())
                .natGatewayId(NatGatewayGenerator.natGatewayId())
                .transitGatewayId(TransitGatewayGenerator.transitGatewayId())
                .localGatewayId(LocalGatewayGenerator.localGatewayId())
                .networkInterfaceId(NetworkInterfaceGenerator.networkInterfaceId())
                .origin(routeOrigin())
                .state(routeState())
                .vpcPeeringConnectionId(VpcPeeringConnectionGenerator.vpcPeeringConnectionId())
                .build()
    }
}
