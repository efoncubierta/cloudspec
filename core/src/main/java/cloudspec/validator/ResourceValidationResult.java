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
package cloudspec.validator;

import cloudspec.model.ResourceDefRef;

import java.util.List;

public class ResourceValidationResult {
    private final ResourceDefRef resourceDefRef;
    private final String resourceId;
    private final List<AssertValidationResult> assertResults;

    public ResourceValidationResult(ResourceDefRef resourceDefRef,
                                    String resourceId,
                                    List<AssertValidationResult> assertResults) {
        this.resourceDefRef = resourceDefRef;
        this.resourceId = resourceId;
        this.assertResults = assertResults;
    }

    public ResourceDefRef getResourceDefRef() {
        return resourceDefRef;
    }

    public String getResourceId() {
        return resourceId;
    }

    public List<AssertValidationResult> getAssertResults() {
        return assertResults;
    }

    public Boolean isSuccess() {
        return assertResults.stream().allMatch(AssertValidationResult::isSuccess);
    }

    @Override
    public String toString() {
        return "ResourceValidationResult{" +
                "resourceDefRef=" + resourceDefRef +
                ", resourceId='" + resourceId + '\'' +
                ", assertResults=" + assertResults +
                '}';
    }
}
