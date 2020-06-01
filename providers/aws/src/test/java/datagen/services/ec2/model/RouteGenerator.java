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
package datagen.services.ec2.model;

import datagen.BaseGenerator;
import datagen.CommonGenerator;
import software.amazon.awssdk.services.ec2.model.Route;
import software.amazon.awssdk.services.ec2.model.RouteOrigin;
import software.amazon.awssdk.services.ec2.model.RouteState;

import java.util.List;

public class RouteGenerator extends BaseGenerator {
    public static RouteOrigin routeOrigin() {
        return fromArray(RouteOrigin.values());
    }

    public static RouteState routeState() {
        return fromArray(RouteState.values());
    }

    public static List<Route> routes() {
        return routes(faker.random().nextInt(1, 10));
    }

    public static List<Route> routes(Integer n) {
        return listGenerator(n, RouteGenerator::route);
    }

    public static Route route() {
        return Route.builder()
                    .destinationCidrBlock(faker.internet().ipV4Cidr())
                    .destinationIpv6CidrBlock(faker.internet().ipV6Cidr())
                    // TODO support real value
                    .destinationPrefixListId(faker.lorem().word())
                    .egressOnlyInternetGatewayId(InternetGatewayGenerator.internetGatewayId())
                    // TODO support other types of gateways
                    .gatewayId(InternetGatewayGenerator.internetGatewayId())
                    .instanceId(InstanceGenerator.instanceId())
                    .instanceOwnerId(CommonGenerator.accountId())
                    .natGatewayId(NatGatewayGenerator.natGatewayId())
                    .transitGatewayId(TransitGatewayGenerator.transitGatewayId())
                    .localGatewayId(LocalGatewayGenerator.localGatewayId())
                    .networkInterfaceId(NetworkInterfaceGenerator.networkInterfaceId())
                    .origin(routeOrigin())
                    .state(routeState())
                    .vpcPeeringConnectionId(VpcPeeringConnectionGenerator.vpcPeeringConnectionId())
                    .build();
    }
}
