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
import software.amazon.awssdk.services.ec2.model.*

object TransitGatewayOptionsGenerator : BaseGenerator() {
    fun transitGatewayOptions(): TransitGatewayOptions {
        return TransitGatewayOptions.builder()
                .amazonSideAsn(faker.random().nextLong())
                .autoAcceptSharedAttachments(
                        valueFromArray(AutoAcceptSharedAttachmentsValue.values())
                )
                .defaultRouteTableAssociation(
                        valueFromArray(DefaultRouteTableAssociationValue.values())
                )
                .associationDefaultRouteTableId(
                        RouteTableGenerator.routeTableId()
                )
                .defaultRouteTablePropagation(
                        valueFromArray(DefaultRouteTablePropagationValue.values())
                )
                .propagationDefaultRouteTableId(
                        RouteTableGenerator.routeTableId()
                )
                .vpnEcmpSupport(
                        valueFromArray(VpnEcmpSupportValue.values())
                )
                .dnsSupport(
                        valueFromArray(DnsSupportValue.values())
                )
                .multicastSupport(
                        valueFromArray(MulticastSupportValue.values())
                )
                .build()
    }
}
