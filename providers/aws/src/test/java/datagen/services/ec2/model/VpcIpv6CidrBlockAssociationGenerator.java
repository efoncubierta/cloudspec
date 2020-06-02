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
import software.amazon.awssdk.services.ec2.model.VpcIpv6CidrBlockAssociation;

public class VpcIpv6CidrBlockAssociationGenerator extends BaseGenerator {
    public static String associationId() {
        return String.format("vpc-cidr-assoc-%s", faker.random().hex(30));
    }

    public static VpcIpv6CidrBlockAssociation vpcIpv6CidrBlockAssociation() {
        return VpcIpv6CidrBlockAssociation.builder()
                                          .associationId(associationId())
                                          .ipv6CidrBlock(faker.internet().ipV6Cidr())
                                          .ipv6CidrBlockState(VpcCidrBlockStateGenerator.vpcCidrBlockState())
                                          .networkBorderGroup(faker.lorem().word()) // TODO realistic value
                                          .ipv6Pool(faker.lorem().word()) // TODO realistic value
                                          .build();
    }
}
