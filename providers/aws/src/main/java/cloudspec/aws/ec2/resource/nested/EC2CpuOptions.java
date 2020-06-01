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
package cloudspec.aws.ec2.resource.nested;

import cloudspec.annotation.PropertyDefinition;
import software.amazon.awssdk.services.ec2.model.CpuOptions;

import java.util.Objects;
import java.util.Optional;

public class EC2CpuOptions {
    @PropertyDefinition(
            name = "core_count",
            description = "The number of CPU cores for the instance"
    )
    private final Integer coreCount;

    @PropertyDefinition(
            name = "threads_per_core",
            description = "The number of threads per CPU core"
    )
    private final Integer threadsPerCore;

    public EC2CpuOptions(Integer coreCount, Integer threadsPerCore) {
        this.coreCount = coreCount;
        this.threadsPerCore = threadsPerCore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof CpuOptions))) {
            return false;
        }

        if (o instanceof CpuOptions) {
            return sdkEquals((CpuOptions) o);
        }

        EC2CpuOptions that = (EC2CpuOptions) o;
        return Objects.equals(coreCount, that.coreCount) &&
                Objects.equals(threadsPerCore, that.threadsPerCore);
    }

    private boolean sdkEquals(CpuOptions that) {
        return Objects.equals(coreCount, that.coreCount()) &&
                Objects.equals(threadsPerCore, that.threadsPerCore());
    }

    @Override
    public int hashCode() {
        return Objects.hash(coreCount, threadsPerCore);
    }

    public static EC2CpuOptions fromSdk(CpuOptions cpuOptions) {
        return Optional.ofNullable(cpuOptions)
                       .map(v -> new EC2CpuOptions(
                                       v.coreCount(),
                                       v.threadsPerCore()
                               )
                       )
                       .orElse(null);
    }
}
