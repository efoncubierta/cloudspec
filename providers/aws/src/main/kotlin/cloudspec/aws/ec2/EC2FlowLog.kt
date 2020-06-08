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
package cloudspec.aws.ec2

import cloudspec.annotation.IdDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.model.KeyValue
import java.time.Instant

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "flow_log",
        description = "VPC Flow Log"
)
data class EC2FlowLog(
        @property:PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = "creation_time",
                description = "The date and time the flow log was created"
        )
        val creationTime: Instant?,

        @property:PropertyDefinition(
                name = "deliver_logs_error_message",
                description = "Information about the error that occurred"
        )
        val deliverLogsErrorMessage: String?,

        @property:PropertyDefinition(
                name = "deliver_logs_permission_arn",
                description = "The ARN of the IAM role that posts logs to CloudWatch Logs"
        )
        val deliverLogsPermissionArn: String?,

        @property:PropertyDefinition(
                name = "deliver_logs_status",
                description = "The status of the logs delivery",
                exampleValues = "SUCCESS | FAILED"
        )
        val deliverLogsStatus: String?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = "flow_log_id",
                description = "The flow log ID"
        )
        val flowLogId: String,

        @property:PropertyDefinition(
                name = "flow_log_status",
                description = "The status of the flow log",
                exampleValues = "ACTIVE"
        )
        val flowLogStatus: String?,

        @property:PropertyDefinition(
                name = "log_group_name",
                description = "The name of the flow log group"
        )
        val logGroupName: String?,

        @property:PropertyDefinition(
                name = "resource_id",
                description = "The ID of the resource on which the flow log was created"
        )
        val resourceId: String?,

        @property:PropertyDefinition(
                name = "traffic_type",
                description = "The type of traffic captured for the flow log",
                exampleValues = "ACCEPT | REJECT | ALL"
        )
        val trafficType: String?,

        @property:PropertyDefinition(
                name = "log_destination_type",
                description = "Specifies the type of destination to which the flow log data is published",
                exampleValues = "cloud-watch-logs | s3"
        )
        val logDestinationType: String?,

        @property:PropertyDefinition(
                name = "log_destination",
                description = "Specifies the destination to which the flow log data is published",
                exampleValues = "ARN of CloudWatch Logs Group | ARN of S3 Bucket"
        )
        val logDestination: String?,

        @property:PropertyDefinition(
                name = "log_format",
                description = "The format of the flow log record"
        )
        val logFormat: String?,

        @property:PropertyDefinition(
                name = "tags",
                description = "The tags for the flow log"
        )
        val tags: List<KeyValue>?,

        @property:PropertyDefinition(
                name = "max_aggregation_interval",
                description = "The maximum interval of time, in seconds, during which a flow of packets is captured and aggregated into a flow log record"
        )
        val maxAggregationInterval: Int?
) : EC2Resource(region)
