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
import software.amazon.awssdk.services.ec2.model.AttachmentStatus
import software.amazon.awssdk.services.ec2.model.NetworkInterfaceAttachment

object NetworkInterfaceAttachmentGenerator : BaseGenerator() {
    fun networkInterfaceAttachmentId(): String {
        return "eni-attach-${faker.random().hex(30)}"
    }

    fun attachmentStatus(): AttachmentStatus {
        return valueFromArray(AttachmentStatus.values())
    }

    fun networkInterfaceAttachment(): NetworkInterfaceAttachment {
        return NetworkInterfaceAttachment.builder()
                .attachTime(pastInstant())
                .attachmentId(networkInterfaceAttachmentId())
                .deleteOnTermination(faker.random().nextBoolean())
                .deviceIndex(faker.random().nextInt(1, 10))
                .instanceId(InstanceGenerator.instanceId())
                .instanceOwnerId(CommonGenerator.accountId())
                .status(attachmentStatus())
                .build()
    }
}
