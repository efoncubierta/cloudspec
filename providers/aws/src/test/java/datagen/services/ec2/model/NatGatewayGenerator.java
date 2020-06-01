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
import software.amazon.awssdk.services.ec2.model.NatGateway;
import software.amazon.awssdk.services.ec2.model.NatGatewayState;

import java.util.List;

public class NatGatewayGenerator extends BaseGenerator {
    public static String natGatewayId() {
        return String.format("nat-%s", faker.random().hex(30));
    }

    public static List<NatGateway> natGateways() {
        return natGateways(faker.random().nextInt(1, 10));
    }

    public static List<NatGateway> natGateways(Integer n) {
        return listGenerator(n, NatGatewayGenerator::natGateway);
    }

    public static NatGatewayState natGatewayState() {
        return fromArray(NatGatewayState.values());
    }

    public static NatGateway natGateway() {
        return NatGateway.builder()
                         .createTime(pastDate().toInstant())
                         .deleteTime(pastDate().toInstant())
                         .failureCode(faker.lorem().word()) // TODO use real value
                         .failureMessage(faker.lorem().sentence()) // TODO use real value
                         .natGatewayAddresses(NatGatewayAddressGenerator.natGatewayAddresses())
                         .natGatewayId(natGatewayId())
                         .provisionedBandwidth(ProvisionedBandwithGenerator.provisionedBandwidth())
                         .state(natGatewayState())
                         .subnetId(SubnetGenerator.subnetId())
                         .vpcId(VpcGenerator.vpcId())
                         .tags(TagGenerator.tags())
                         .build();
    }
}
