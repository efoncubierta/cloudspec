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
import java.time.Instant

data class EC2NetworkInterfaceAttachment(
        @PropertyDefinition(
                name = "attach_time",
                description = "The timestamp indicating when the attachment initiated"
        )
        val attachTime: Instant?,

        //    @AssociationDefinition(
        //            name = "attachment_id",
        //            description = ""
        //    )
        //    private final String attachmentId;

        @PropertyDefinition(
                name = "delete_on_termination",
                description = "Indicates whether the network interface is deleted when the instance is terminated"
        )
        val deleteOnTermination: Boolean?,

        @AssociationDefinition(
                name = "instance",
                description = "The instance",
                targetClass = EC2Instance::class
        )
        val instanceId: String?,

        @PropertyDefinition(
                name = "instance_owner_id",
                description = "The AWS account ID of the owner of the instance"
        )
        val instanceOwnerId: String?,

        @PropertyDefinition(
                name = "status",
                description = "The attachment state"
        )
        val status: String?
)
