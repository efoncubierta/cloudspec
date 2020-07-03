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
package cloudspec.aws

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.getOrElse
import arrow.core.toOption
import cloudspec.aws.acm.ACMGroup
import cloudspec.aws.apigateway.APIGatewayGroup
import cloudspec.aws.cloudfront.CloudFrontGroup
import cloudspec.aws.cloudtrail.CloudTrailGroup
import cloudspec.aws.cloudwatch.CloudWatchGroup
import cloudspec.aws.cognito.CognitoGroup
import cloudspec.aws.documentdb.DocumentDBGroup
import cloudspec.aws.dynamodb.DDBGroup
import cloudspec.aws.ec2.EC2Group
import cloudspec.aws.ecr.ECRGroup
import cloudspec.aws.ecs.ECSGroup
import cloudspec.aws.efs.EFSGroup
import cloudspec.aws.eks.EKSGroup
import cloudspec.aws.elasticache.ElastiCacheGroup
import cloudspec.aws.es.ESGroup
import cloudspec.aws.elb.ELBGroup
import cloudspec.aws.iam.IAMGroup
import cloudspec.aws.kinesis.KinesisGroup
import cloudspec.aws.kms.KMSGroup
import cloudspec.aws.lambda.LambdaGroup
import cloudspec.aws.mq.MQGroup
import cloudspec.aws.rds.RDSGroup
import cloudspec.aws.redshift.RedshiftGroup
import cloudspec.aws.route53.Route53Group
import cloudspec.aws.s3.S3Group
import cloudspec.aws.sns.SNSGroup
import cloudspec.aws.sqs.SQSGroup
import cloudspec.model.*

class AWSProvider(clientsProvider: IAWSClientsProvider) : Provider() {
    private val groups: Map<String, AWSGroup> = mapOf(
            ACMGroup.GROUP_NAME to ACMGroup(clientsProvider),
            APIGatewayGroup.GROUP_NAME to APIGatewayGroup(clientsProvider),
            CloudFrontGroup.GROUP_NAME to CloudFrontGroup(clientsProvider),
            CloudTrailGroup.GROUP_NAME to CloudTrailGroup(clientsProvider),
            CloudWatchGroup.GROUP_NAME to CloudWatchGroup(clientsProvider),
            CognitoGroup.GROUP_NAME to CognitoGroup(clientsProvider),
            DocumentDBGroup.GROUP_NAME to DocumentDBGroup(clientsProvider),
            DDBGroup.GROUP_NAME to DDBGroup(clientsProvider),
            EC2Group.GROUP_NAME to EC2Group(clientsProvider),
            ECRGroup.GROUP_NAME to ECRGroup(clientsProvider),
            ECSGroup.GROUP_NAME to ECSGroup(clientsProvider),
            EFSGroup.GROUP_NAME to EFSGroup(clientsProvider),
            EKSGroup.GROUP_NAME to EKSGroup(clientsProvider),
            ElastiCacheGroup.GROUP_NAME to ElastiCacheGroup(clientsProvider),
            ESGroup.GROUP_NAME to ESGroup(clientsProvider),
            ELBGroup.GROUP_NAME to ELBGroup(clientsProvider),
            IAMGroup.GROUP_NAME to IAMGroup(clientsProvider),
            KinesisGroup.GROUP_NAME to KinesisGroup(clientsProvider),
            KMSGroup.GROUP_NAME to KMSGroup(clientsProvider),
            LambdaGroup.GROUP_NAME to LambdaGroup(clientsProvider),
            MQGroup.GROUP_NAME to MQGroup(clientsProvider),
            RDSGroup.GROUP_NAME to RDSGroup(clientsProvider),
            RedshiftGroup.GROUP_NAME to RedshiftGroup(clientsProvider),
            Route53Group.GROUP_NAME to Route53Group(clientsProvider),
            S3Group.GROUP_NAME to S3Group(clientsProvider),
            SNSGroup.GROUP_NAME to SNSGroup(clientsProvider),
            SQSGroup.GROUP_NAME to SQSGroup(clientsProvider)
    )

    override val name
        get() = PROVIDER_NAME

    override val description
        get() = PROVIDER_DESCRIPTION

    override val groupDefs
        get() = groups.values.map { it.definition }

    override val resourceDefs: ResourceDefs
        get() = groupDefs.flatMap { it.resourceDefs }

    override val configDefs: ConfigDefs
        get() = AWSConfig.CONFIG_DEFS

    override fun resourcesByDef(sets: SetValues, defRef: ResourceDefRef): List<Resource> {
        return getGroup(defRef).map { group ->
            group.resourcesByRef(sets, defRef)
        }.getOrElse { emptyList() }
    }

    override fun resource(sets: SetValues, ref: ResourceRef): Option<Resource> {
        return Option.fx {
            val (group) = getGroup(ref.defRef)
            val (resource) = group.resource(sets, ref)

            resource
        }
    }

    private fun getGroup(resourceDefRef: ResourceDefRef): Option<AWSGroup> {
        return groups[resourceDefRef.groupName].toOption()
    }

    companion object {
        const val PROVIDER_NAME = "aws"
        const val PROVIDER_DESCRIPTION = "Amazon Web Services"
    }
}

