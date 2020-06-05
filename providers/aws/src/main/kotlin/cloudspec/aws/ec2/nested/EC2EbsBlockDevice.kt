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
import cloudspec.aws.ec2.EC2Snapshot

data class EC2EbsBlockDevice(
        @PropertyDefinition(
                name = "delete_on_termination",
                description = "Indicates whether the EBS volume is deleted on instance termination"
        )
        val deleteOnTermination: Boolean?,

        @PropertyDefinition(
                name = "iops",
                description = "The number of I/O operations per second (IOPS) that the volume supports"
        )
        val iops: Int?,

        @AssociationDefinition(
                name = "snapshot",
                description = "The ID of the snapshot",
                targetClass = EC2Snapshot::class
        )
        val snapshotId: String?,

        @PropertyDefinition(
                name = "volume_size",
                description = "The size of the volume, in GiB"
        )
        val volumeSize: Int?,

        @PropertyDefinition(
                name = "volume_type",
                description = "The volume type",
                exampleValues = "gp2, st1, sc1, standard"
        )
        val volumeType: String?,

        @PropertyDefinition(
                name = "encrypted",
                description = "Indicates whether the encryption state of an EBS volume is changed while being restored from a backing snapshot"
        )
        val encrypted: Boolean?
)
