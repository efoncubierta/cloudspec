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

import cloudspec.annotation.PropertyDefinition

data class EC2BlockDeviceMapping(
        @property:PropertyDefinition(
                name = "device_name",
                description = "The device name",
                exampleValues = "/dev/sdh"
        )
        val deviceName: String?,

        @property:PropertyDefinition(
                name = "virtual_name",
                description = "The virtual device name"
        )
        val virtualName: String?,

        @property:PropertyDefinition(
                name = "ebs",
                description = "Parameters used to automatically set up EBS volumes when the instance is launched"
        )
        val ebs: EC2EbsBlockDevice?
)
