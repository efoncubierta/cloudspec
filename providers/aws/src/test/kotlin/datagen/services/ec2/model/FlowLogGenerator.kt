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
import datagen.services.iam.model.RoleGenerator
import software.amazon.awssdk.services.ec2.model.FlowLog
import software.amazon.awssdk.services.ec2.model.LogDestinationType
import software.amazon.awssdk.services.ec2.model.TrafficType

object FlowLogGenerator : BaseGenerator() {

    fun flowLogId(): String {
        return "fl-${faker.random().hex(30)}"
    }

    fun flowLogStatus(): String {
        // TODO generate real status
        return faker.lorem().word()
    }

    fun trafficType(): TrafficType {
        return valueFromArray(TrafficType.values())
    }

    fun logDestinationType(): LogDestinationType {
        return valueFromArray(LogDestinationType.values())
    }

    fun flowLogs(n: Int? = null): List<FlowLog> {
        return listGenerator(n) { flowLog() }
    }

    fun flowLog(): FlowLog {
        return FlowLog.builder()
                .creationTime(pastInstant())
                .deliverLogsErrorMessage(faker.lorem().sentence())
                .deliverLogsPermissionArn(RoleGenerator.roleArn().toString())
                .flowLogId(flowLogId())
                .flowLogStatus(flowLogStatus())
                .logGroupName(faker.lorem().word())
                .resourceId(faker.lorem().word()) // TODO use real value
                .trafficType(trafficType())
                .logDestinationType(logDestinationType())
                .logDestination(faker.lorem().word()) // TODO use real value
                .logFormat(faker.lorem().word()) // TODO use real value
                .tags(TagGenerator.tags())
                .maxAggregationInterval(faker.random().nextInt(1, 1000))
                .build()
    }
}
