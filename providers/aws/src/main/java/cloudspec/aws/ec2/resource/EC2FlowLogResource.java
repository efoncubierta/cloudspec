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
package cloudspec.aws.ec2.resource;

import cloudspec.annotation.IdDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.annotation.ResourceDefinition;
import cloudspec.aws.AWSProvider;
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;
import software.amazon.awssdk.services.ec2.model.ElasticGpus;
import software.amazon.awssdk.services.ec2.model.FlowLog;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2FlowLogResource.RESOURCE_NAME,
        description = "VPC Flow Log"
)
public class EC2FlowLogResource extends EC2Resource {
    public static final String RESOURCE_NAME = "flow_log";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @PropertyDefinition(
            name = "region",
            description = "The AWS region",
            exampleValues = "us-east-1 | eu-west-1"
    )
    private final String region;

    @PropertyDefinition(
            name = "creation_time",
            description = "The date and time the flow log was created"
    )
    private final Date creationTime;

    @PropertyDefinition(
            name = "deliver_logs_error_message",
            description = "Information about the error that occurred"
    )
    private final String deliverLogsErrorMessage;

    @PropertyDefinition(
            name = "deliver_logs_permission_arn",
            description = "The ARN of the IAM role that posts logs to CloudWatch Logs"
    )
    private final String deliverLogsPermissionArn;

    @PropertyDefinition(
            name = "deliver_logs_status",
            description = "The status of the logs delivery",
            exampleValues = "SUCCESS | FAILED"
    )
    private final String deliverLogsStatus;

    @IdDefinition
    @PropertyDefinition(
            name = "flow_log_id",
            description = "The flow log ID"
    )
    private final String flowLogId;

    @PropertyDefinition(
            name = "flow_log_status",
            description = "The status of the flow log",
            exampleValues = "ACTIVE"
    )
    private final String flowLogStatus;

    @PropertyDefinition(
            name = "log_group_name",
            description = "The name of the flow log group"
    )
    private final String logGroupName;

    @PropertyDefinition(
            name = "resource_id",
            description = "The ID of the resource on which the flow log was created"
    )
    private final String resourceId;

    @PropertyDefinition(
            name = "traffic_type",
            description = "The type of traffic captured for the flow log",
            exampleValues = "ACCEPT | REJECT | ALL"
    )
    private final String trafficType;

    @PropertyDefinition(
            name = "log_destination_type",
            description = "Specifies the type of destination to which the flow log data is published",
            exampleValues = "cloud-watch-logs | s3"
    )
    private final String logDestinationType;

    @PropertyDefinition(
            name = "log_destination",
            description = "Specifies the destination to which the flow log data is published",
            exampleValues = "ARN of CloudWatch Logs Group | ARN of S3 Bucket"
    )
    private final String logDestination;

    @PropertyDefinition(
            name = "log_format",
            description = "The format of the flow log record"
    )
    private final String logFormat;

    @PropertyDefinition(
            name = "tags",
            description = "The tags for the flow log"
    )
    private final List<KeyValue> tags;

    @PropertyDefinition(
            name = "max_aggregation_interval",
            description = "The maximum interval of time, in seconds, during which a flow of packets is captured and aggregated into a flow log record"
    )
    private final Integer maxAggregationInterval;

    public EC2FlowLogResource(String region, Date creationTime, String deliverLogsErrorMessage,
                              String deliverLogsPermissionArn, String deliverLogsStatus, String flowLogId,
                              String flowLogStatus, String logGroupName, String resourceId, String trafficType,
                              String logDestinationType, String logDestination, String logFormat,
                              List<KeyValue> tags, Integer maxAggregationInterval) {
        this.region = region;
        this.creationTime = creationTime;
        this.deliverLogsErrorMessage = deliverLogsErrorMessage;
        this.deliverLogsPermissionArn = deliverLogsPermissionArn;
        this.deliverLogsStatus = deliverLogsStatus;
        this.flowLogId = flowLogId;
        this.flowLogStatus = flowLogStatus;
        this.logGroupName = logGroupName;
        this.resourceId = resourceId;
        this.trafficType = trafficType;
        this.logDestinationType = logDestinationType;
        this.logDestination = logDestination;
        this.logFormat = logFormat;
        this.tags = tags;
        this.maxAggregationInterval = maxAggregationInterval;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof FlowLog))) {
            return false;
        }

        if (o instanceof FlowLog) {
            return sdkEquals((FlowLog) o);
        }

        EC2FlowLogResource that = (EC2FlowLogResource) o;
        return Objects.equals(region, that.region) &&
                Objects.equals(creationTime, that.creationTime) &&
                Objects.equals(deliverLogsErrorMessage, that.deliverLogsErrorMessage) &&
                Objects.equals(deliverLogsPermissionArn, that.deliverLogsPermissionArn) &&
                Objects.equals(deliverLogsStatus, that.deliverLogsStatus) &&
                Objects.equals(flowLogId, that.flowLogId) &&
                Objects.equals(flowLogStatus, that.flowLogStatus) &&
                Objects.equals(logGroupName, that.logGroupName) &&
                Objects.equals(resourceId, that.resourceId) &&
                Objects.equals(trafficType, that.trafficType) &&
                Objects.equals(logDestinationType, that.logDestinationType) &&
                Objects.equals(logDestination, that.logDestination) &&
                Objects.equals(logFormat, that.logFormat) &&
                Objects.equals(tags, that.tags) &&
                Objects.equals(maxAggregationInterval, that.maxAggregationInterval);
    }

    private boolean sdkEquals(FlowLog that) {
        return Objects.equals(creationTime, dateFromSdk(that.creationTime())) &&
                Objects.equals(deliverLogsErrorMessage, that.deliverLogsErrorMessage()) &&
                Objects.equals(deliverLogsPermissionArn, that.deliverLogsPermissionArn()) &&
                Objects.equals(deliverLogsStatus, that.deliverLogsStatus()) &&
                Objects.equals(flowLogId, that.flowLogId()) &&
                Objects.equals(flowLogStatus, that.flowLogStatus()) &&
                Objects.equals(logGroupName, that.logGroupName()) &&
                Objects.equals(resourceId, that.resourceId()) &&
                Objects.equals(trafficType, that.trafficTypeAsString()) &&
                Objects.equals(logDestinationType, that.logDestinationTypeAsString()) &&
                Objects.equals(logDestination, that.logDestination()) &&
                Objects.equals(logFormat, that.logFormat()) &&
                Objects.equals(tags, tagsFromSdk(that.tags())) &&
                Objects.equals(maxAggregationInterval, that.maxAggregationInterval());
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, creationTime, deliverLogsErrorMessage, deliverLogsPermissionArn, deliverLogsStatus,
                flowLogId, flowLogStatus, logGroupName, resourceId, trafficType, logDestinationType, logDestination,
                logFormat, tags, maxAggregationInterval);
    }

    public static EC2FlowLogResource fromSdk(String regionName, FlowLog flowLog) {
        return Optional.ofNullable(flowLog)
                       .map(v -> new EC2FlowLogResource(
                               regionName,
                               dateFromSdk(v.creationTime()),
                               v.deliverLogsErrorMessage(),
                               v.deliverLogsPermissionArn(),
                               v.deliverLogsStatus(),
                               v.flowLogId(),
                               v.flowLogStatus(),
                               v.logGroupName(),
                               v.resourceId(),
                               v.trafficTypeAsString(),
                               v.logDestinationTypeAsString(),
                               v.logDestination(),
                               v.logFormat(),
                               tagsFromSdk(v.tags()),
                               v.maxAggregationInterval()
                       ))
                       .orElse(null);
    }
}
