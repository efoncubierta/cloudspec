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
package cloudspec.aws.sns

import arrow.core.Option
import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.syntax.collections.flatten
import cloudspec.aws.IAWSClientsProvider
import software.amazon.awssdk.arns.Arn
import software.amazon.awssdk.regions.Region

class SNSTopicLoader(clientsProvider: IAWSClientsProvider) :
        SNSResourceLoader<SNSTopic>(clientsProvider) {
    override fun resourceByArn(arn: Arn): IO<Option<SNSTopic>> {
        val region = Region.of(arn.region().get())
        return IO.fx {
            val (res) = requestInRegion(region) { client ->
                // https://docs.aws.amazon.com/sns/latest/api/API_GetTopicAttributes.html
                client.getTopicAttributes { builder -> builder.topicArn(arn.toString()) }
            }
            val (tags) = listTags(region, arn.toString())
            Option.fromNullable(res.attributes().toSNSTopic(region, tags))
        }
    }

    override fun resourcesByRegion(region: Region): IO<List<SNSTopic>> {
        return IO.fx {
            val (topics) = requestInRegion(region) { client ->
                // https://docs.aws.amazon.com/sns/latest/api/API_ListTopics.html
                client.listTopics().topics()
            }

            val (tables) = topics.map { resourceByArn(Arn.fromString(it.topicArn())) }.parSequence()
            tables.flatten()
        }
    }
}
