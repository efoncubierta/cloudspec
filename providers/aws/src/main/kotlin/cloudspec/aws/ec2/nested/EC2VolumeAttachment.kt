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

import cloudspec.annotation.AssociationDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.aws.ec2.EC2Instance
import cloudspec.aws.ec2.EC2Volume
import java.time.Instant

data class EC2VolumeAttachment(
        @PropertyDefinition(
                name = "attach_time",
                description = "The time stamp when the attachment initiated"
        )
        val attachTime: Instant?,

        @PropertyDefinition(
                name = "device",
                description = "The device name"
        )
        val device: String?,

        @AssociationDefinition(
                name = "instance",
                description = "The instance",
                targetClass = EC2Instance::class
        )
        val instanceId: String?,

        @PropertyDefinition(
                name = "state",
                description = "The attachment state of the volume",
                exampleValues = "attaching | attached | detaching | detached | busy"
        )
        val state: String?,

        @AssociationDefinition(
                name = "volume",
                description = "The volume",
                targetClass = EC2Volume::class
        )
        val volumeId: String?,

        @PropertyDefinition(
                name = "delete_on_termination",
                description = "Indicates whether the EBS volume is deleted on instance termination"
        )
        val deleteOnTermination: Boolean?
)
