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
import software.amazon.awssdk.arns.Arn;
import software.amazon.awssdk.services.ec2.model.TransitGateway;
import software.amazon.awssdk.services.ec2.model.TransitGatewayState;

import java.util.List;

public class TransitGatewayGenerator extends BaseGenerator {
    public static String transitGatewayId() {
        return String.format("tgw-%s", faker.random().hex(30));
    }

    public static Arn transitGatewayArn() {
        return Arn.builder()
                  .service("ec2")
                  .region(CommonGenerator.region().id())
                  .accountId(CommonGenerator.accountId())
                  .partition("transit-gateway")
                  .resource(transitGatewayId())
                  .build();
    }

    public static TransitGatewayState transitGatewayState() {
        return fromArray(TransitGatewayState.values());
    }

    public static List<TransitGateway> transitGateways() {
        return transitGateways(faker.random().nextInt(1, 10));
    }

    public static List<TransitGateway> transitGateways(Integer n) {
        return listGenerator(n, TransitGatewayGenerator::transitGateway);
    }

    public static TransitGateway transitGateway() {
        return TransitGateway.builder()
                             .transitGatewayId(transitGatewayId())
                             .transitGatewayArn(transitGatewayArn().toString())
                             .state(transitGatewayState())
                             .ownerId(CommonGenerator.accountId())
                             .description(faker.lorem().sentence())
                             .creationTime(pastDate().toInstant())
                             .options(TransitGatewayOptionsGenerator.transitGatewayOptions())
                             .tags(TagGenerator.tags())
                             .build();
    }
}
