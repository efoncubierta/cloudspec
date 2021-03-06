/*-
 * #%L
 * CloudSpec Core Library
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
package cloudspec.model

/**
 * Define a CloudSpec resource.
 *
 * A resource is anything that can be evaluated. It can be an EC2 instance, an S3 bucket, an entire service, etc.
 * Resources have members, that can be either properties or associations.
 *
 * Resources are provided by the providers.
 */
data class Resource(
        val ref: ResourceRef,
        override val properties: Properties = emptySet(),
        override val associations: Associations = emptySet()
) : MembersContainer
