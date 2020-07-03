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
import cloudspec.aws.AWSProvider
import cloudspec.model.KeyValue
import cloudspec.model.ResourceDefRef
import software.amazon.awssdk.services.ec2.model.LogDestinationType
import software.amazon.awssdk.services.ec2.model.TrafficType
import java.time.Instant

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Group.GROUP_NAME,
        name = EC2FlowLog.RESOURCE_NAME,
        description = EC2FlowLog.RESOURCE_DESCRIPTION
)
data class EC2FlowLog(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = PROP_CREATION_TIME,
                description = PROP_CREATION_TIME_D
        )
        val creationTime: Instant?,

        @property:PropertyDefinition(
                name = PROP_DELIVER_LOGS_ERROR_MESSAGE,
                description = PROP_DELIVER_LOGS_ERROR_MESSAGE_D
        )
        val deliverLogsErrorMessage: String?,

        @property:PropertyDefinition(
                name = PROP_DELIVER_LOGS_PERMISSION_ARN,
                description = PROP_DELIVER_LOGS_PERMISSION_ARN_D
        )
        val deliverLogsPermissionArn: String?,

        @property:PropertyDefinition(
                name = PROP_DELIVER_LOGS_STATUS,
                description = PROP_DELIVER_LOGS_STATUS_D
        )
        val deliverLogsStatus: String?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = PROP_FLOW_LOG_ID,
                description = PROP_FLOW_LOG_ID_D
        )
        val flowLogId: String,

        @property:PropertyDefinition(
                name = PROP_FLOW_LOG_STATUS,
                description = PROP_FLOW_LOG_STATUS_D
        )
        val flowLogStatus: String?,

        @property:PropertyDefinition(
                name = PROP_LOG_GROUP_NAME,
                description = PROP_LOG_GROUP_NAME_D
        )
        val logGroupName: String?,

        @property:PropertyDefinition(
                name = PROP_RESOURCE_ID,
                description = PROP_RESOURCE_ID_D
        )
        val resourceId: String?,

        @property:PropertyDefinition(
                name = PROP_TRAFFIC_TYPE,
                description = PROP_TRAFFIC_TYPE_D
        )
        val trafficType: TrafficType?,

        @property:PropertyDefinition(
                name = PROP_LOG_DESTINATION_TYPE,
                description = PROP_LOG_DESTINATION_TYPE_D
        )
        val logDestinationType: LogDestinationType?,

        @property:PropertyDefinition(
                name = PROP_LOG_DESTINATION,
                description = PROP_LOG_DESTINATION_D
        )
        val logDestination: String?,

        @property:PropertyDefinition(
                name = PROP_LOG_FORMAT,
                description = PROP_LOG_FORMAT_D
        )
        val logFormat: String?,

        @property:PropertyDefinition(
                name = PROP_TAGS,
                description = PROP_TAGS_D
        )
        val tags: List<KeyValue>?,

        @property:PropertyDefinition(
                name = PROP_MAX_AGGREGATION_INTERVAL,
                description = PROP_MAX_AGGREGATION_INTERVAL_D
        )
        val maxAggregationInterval: Int?
) : EC2Resource(region) {
    companion object {
        const val RESOURCE_NAME = "flow_log"
        const val RESOURCE_DESCRIPTION = "VPC Flow Log"
        val RESOURCE_DEF = ResourceDefRef(AWSProvider.PROVIDER_NAME,
                                          EC2Group.GROUP_NAME,
                                          RESOURCE_NAME)

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_CREATION_TIME = "creation_time"
        const val PROP_CREATION_TIME_D = "The date and time the flow log was created"
        const val PROP_DELIVER_LOGS_ERROR_MESSAGE = "deliver_logs_error_message"
        const val PROP_DELIVER_LOGS_ERROR_MESSAGE_D = "Information about the error that occurred"
        const val PROP_DELIVER_LOGS_PERMISSION_ARN = "deliver_logs_permission_arn"
        const val PROP_DELIVER_LOGS_PERMISSION_ARN_D = "The ARN of the IAM role that posts logs to CloudWatch Logs"
        const val PROP_DELIVER_LOGS_STATUS = "deliver_logs_status"
        const val PROP_DELIVER_LOGS_STATUS_D = "The status of the logs delivery"
        const val PROP_FLOW_LOG_ID = "flow_log_id"
        const val PROP_FLOW_LOG_ID_D = "The flow log ID"
        const val PROP_FLOW_LOG_STATUS = "flow_log_status"
        const val PROP_FLOW_LOG_STATUS_D = "The status of the flow log"
        const val PROP_LOG_GROUP_NAME = "log_group_name"
        const val PROP_LOG_GROUP_NAME_D = "The name of the flow log group"
        const val PROP_RESOURCE_ID = "resource_id"
        const val PROP_RESOURCE_ID_D = "The ID of the resource on which the flow log was created"
        const val PROP_TRAFFIC_TYPE = "traffic_type"
        const val PROP_TRAFFIC_TYPE_D = "The type of traffic captured for the flow log"
        const val PROP_LOG_DESTINATION_TYPE = "log_destination_type"
        const val PROP_LOG_DESTINATION_TYPE_D = "Specifies the type of destination to which the flow log data is published"
        const val PROP_LOG_DESTINATION = "log_destination"
        const val PROP_LOG_DESTINATION_D = "Specifies the destination to which the flow log data is published"
        const val PROP_LOG_FORMAT = "log_format"
        const val PROP_LOG_FORMAT_D = "The format of the flow log record"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "The tags for the flow log"
        const val PROP_MAX_AGGREGATION_INTERVAL = "max_aggregation_interval"
        const val PROP_MAX_AGGREGATION_INTERVAL_D = "The maximum interval of time, in seconds, during which a flow of packets is captured and aggregated into a flow log record"
    }
}
