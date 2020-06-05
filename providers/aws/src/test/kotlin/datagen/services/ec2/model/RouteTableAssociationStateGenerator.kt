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
import software.amazon.awssdk.services.ec2.model.RouteTableAssociationState
import software.amazon.awssdk.services.ec2.model.RouteTableAssociationStateCode

object RouteTableAssociationStateGenerator : BaseGenerator() {
    fun routeTableAssociationStateCode(): RouteTableAssociationStateCode {
        return valueFromArray(RouteTableAssociationStateCode.values())
    }

    fun routeTableAssociationState(): RouteTableAssociationState {
        return RouteTableAssociationState.builder()
                .state(routeTableAssociationStateCode())
                .statusMessage(faker.lorem().sentence()) // TODO generate real value for message
                .build()
    }
}
