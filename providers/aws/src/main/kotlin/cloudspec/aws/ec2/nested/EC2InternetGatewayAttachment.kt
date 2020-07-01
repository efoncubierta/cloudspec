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
package cloudspec.aws.ec2.nested

import cloudspec.annotation.AssociationDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.aws.ec2.EC2Vpc

data class EC2InternetGatewayAttachment(
        @property:PropertyDefinition(
                name = PROP_STATE,
                description = PROP_STATE_D,
                exampleValues = "attaching | attached | detaching | detached"
        )
        val state: String?,

        @property:AssociationDefinition(
                name = ASSOC_VPC,
                description = ASSOC_VPC_D,
                targetClass = EC2Vpc::class
        )
        val vpcId: String?
) {
    companion object {
        const val PROP_STATE = "state"
        const val PROP_STATE_D = "The current state of the attachment"
        const val ASSOC_VPC = "vpc"
        const val ASSOC_VPC_D = "The VPC"
    }
}
