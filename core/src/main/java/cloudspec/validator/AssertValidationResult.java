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

import java.util.List;
import java.util.Optional;

public class AssertValidationResult {
    private final List<String> path;
    private final Boolean success;
    private final Optional<AssertValidationError> error;

    public AssertValidationResult(List<String> path, Boolean success) {
        this(path, success, null);
    }

    public AssertValidationResult(List<String> path, Boolean success, AssertValidationError error) {
        this.path = path;
        this.success = success;
        this.error = Optional.ofNullable(error);
    }

    public List<String> getPath() {
        return path;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Optional<AssertValidationError> getError() {
        return error;
    }

    public Boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "AssertValidationResult{" +
                "path='" + path + '\'' +
                ", success=" + success +
                ", message='" + error + '\'' +
                '}';
    }
}
