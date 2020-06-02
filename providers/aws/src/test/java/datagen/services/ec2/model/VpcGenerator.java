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
import software.amazon.awssdk.services.ec2.model.Tenancy;
import software.amazon.awssdk.services.ec2.model.Vpc;
import software.amazon.awssdk.services.ec2.model.VpcState;

import java.util.List;

public class VpcGenerator extends BaseGenerator {
    public static String vpcId() {
        return String.format("vpc-%s", faker.random().hex(30));
    }

    public static List<Vpc> vpcs() {
        return vpcs(faker.random().nextInt(1, 10));
    }

    public static List<Vpc> vpcs(Integer n) {
        return listGenerator(n, VpcGenerator::vpc);
    }

    public static Vpc vpc() {
        return Vpc.builder()
                  .cidrBlock(faker.internet().ipV4Cidr())
                  .dhcpOptionsId(DhcpOptionsGenerator.dhcpOptionsId())
                  .state(
                          fromArray(VpcState.values())
                  )
                  .vpcId(vpcId())
                  .ownerId(CommonGenerator.accountId())
                  .instanceTenancy(
                          fromArray(Tenancy.values())
                  )
                  .ipv6CidrBlockAssociationSet(
                          listGenerator(VpcIpv6CidrBlockAssociationGenerator::vpcIpv6CidrBlockAssociation)
                  )
                  .cidrBlockAssociationSet(
                          listGenerator(VpcCidrBlockAssociationGenerator::vpcCidrBlockAssociation)
                  )
                  .isDefault(faker.random().nextBoolean())
                  .tags(TagGenerator.tags())
                  .build();
    }
}
