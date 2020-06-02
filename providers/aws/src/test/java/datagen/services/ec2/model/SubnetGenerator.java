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
import software.amazon.awssdk.services.ec2.model.Subnet;
import software.amazon.awssdk.services.ec2.model.SubnetState;

import java.util.List;

public class SubnetGenerator extends BaseGenerator {
    public static String subnetId() {
        return String.format("subnet-%s", faker.lorem().characters(10));
    }

    public static Arn subnetArn() {
        return Arn.builder()
                  .service("ec2")
                  .region(CommonGenerator.region().id())
                  .accountId(CommonGenerator.accountId())
                  .partition("subnet")
                  .resource(subnetId())
                  .build();
    }

    public static SubnetState subnetState() {
        return fromArray(SubnetState.values());
    }

    public static List<Subnet> subnets() {
        return subnets(faker.random().nextInt(1, 10));
    }

    public static List<Subnet> subnets(Integer n) {
        return listGenerator(n, SubnetGenerator::subnet);
    }

    public static Subnet subnet() {
        return Subnet.builder()
                     .availabilityZone(CommonGenerator.availabilityZone())
                     .availabilityZoneId(CommonGenerator.availabilityZone())
                     .availableIpAddressCount(faker.random().nextInt(1, 1024))
                     .assignIpv6AddressOnCreation(faker.random().nextBoolean())
                     .cidrBlock(faker.internet().ipV4Cidr())
                     .defaultForAz(faker.random().nextBoolean())
                     .mapPublicIpOnLaunch(faker.random().nextBoolean())
                     .state(subnetState())
                     .subnetId(SubnetGenerator.subnetId())
                     .vpcId(VpcGenerator.vpcId())
                     .ownerId(CommonGenerator.accountId())
                     .assignIpv6AddressOnCreation(faker.random().nextBoolean())
                     .ipv6CidrBlockAssociationSet(SubnetIpv6CidrBlockAssociationGenerator.subnetIpv6CidrBlockAssociation())
                     .tags(TagGenerator.tags())
                     .subnetArn(subnetArn().toString())
                     .outpostArn(OutpostGenerator.outpostArn().toString())
                     .build();
    }
}
