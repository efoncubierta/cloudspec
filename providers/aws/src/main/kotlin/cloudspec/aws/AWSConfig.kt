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

import cloudspec.model.ConfigDef
import cloudspec.model.ConfigRef
import cloudspec.model.ConfigValueType

class AWSConfig {
    companion object {
        val REGIONS_REF = ConfigRef("aws", "regions")
        val ACCESS_KEY_ID_REF = ConfigRef("aws", "access_key_id")
        val SECRET_ACCESS_KEY_REF = ConfigRef("aws", "secret_access_key")

        val CONFIG_DEFS = setOf(
                ConfigDef(REGIONS_REF,
                          "AWS Regions",
                          ConfigValueType.STRING,
                          true),
                ConfigDef(ACCESS_KEY_ID_REF,
                          "AWS Access Key ID",
                          ConfigValueType.STRING,
                          false),
                ConfigDef(SECRET_ACCESS_KEY_REF,
                          "AWS Secret Access Key",
                          ConfigValueType.STRING,
                          false)
        )
    }
}
