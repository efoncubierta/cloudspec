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
import software.amazon.awssdk.arns.Arn
import software.amazon.awssdk.services.ec2.model.TransitGateway
import software.amazon.awssdk.services.ec2.model.TransitGatewayState

object TransitGatewayGenerator : BaseGenerator() {
    fun transitGatewayId(): String {
        return "tgw-${faker.random().hex(30)}"
    }

    fun transitGatewayArn(): Arn {
        return Arn.builder()
                .service("ec2")
                .region(CommonGenerator.region().id())
                .accountId(CommonGenerator.accountId())
                .partition("transit-gateway")
                .resource(transitGatewayId())
                .build()
    }

    fun transitGatewayState(): TransitGatewayState {
        return valueFromArray(TransitGatewayState.values())
    }

    fun transitGateways(n: Int? = null): List<TransitGateway> {
        return listGenerator(n) { transitGateway() }
    }

    fun transitGateway(): TransitGateway {
        return TransitGateway.builder()
                .transitGatewayId(transitGatewayId())
                .transitGatewayArn(transitGatewayArn().toString())
                .state(transitGatewayState())
                .ownerId(CommonGenerator.accountId())
                .description(faker.lorem().sentence())
                .creationTime(pastInstant())
                .options(TransitGatewayOptionsGenerator.transitGatewayOptions())
                .tags(TagGenerator.tags())
                .build()
    }
}
