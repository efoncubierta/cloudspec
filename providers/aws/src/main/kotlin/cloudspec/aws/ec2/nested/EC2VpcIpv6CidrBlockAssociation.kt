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
package cloudspec.aws.ec2.nested

import cloudspec.annotation.PropertyDefinition

data class EC2VpcIpv6CidrBlockAssociation(
//    @AssociationDefinition(
        //            name = "association",
        //            description = "The association for the IPv6 CIDR block"
        //    )
        //    private final String associationId;
        @PropertyDefinition(
                name = "ipv6_cidr_block",
                description = "The IPv6 CIDR block"
        )
        val ipv6CidrBlock: String?,

        @PropertyDefinition(
                name = "ipv6_cidr_block_state",
                description = "Information about the state of the CIDR block"
        )
        val ipv6CidrBlockState: String?,

        @PropertyDefinition(
                name = "network_border_group",
                description = "The name of the location from which we advertise the IPV6 CIDR block"
        )
        val networkBorderGroup: String?,

        @PropertyDefinition(
                name = "ipv6_pool",
                description = "The ID of the IPv6 address pool from which the IPv6 CIDR block is allocated"
        )
        val ipv6Pool: String?
)
