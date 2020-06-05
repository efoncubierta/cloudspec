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
