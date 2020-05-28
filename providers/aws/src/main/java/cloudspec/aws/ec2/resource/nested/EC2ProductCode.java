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
import software.amazon.awssdk.services.ec2.model.ProductCode;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EC2ProductCode {
    @PropertyDefinition(
            name = "id",
            description = "The product code"
    )
    private String productCodeId;

    @PropertyDefinition(
            name = "type",
            description = "The type of product code",
            exampleValues = "devpay | marketplace"
    )
    private String productCodeType;

    public EC2ProductCode(String productCodeId, String productCodeType) {
        this.productCodeId = productCodeId;
        this.productCodeType = productCodeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2ProductCode that = (EC2ProductCode) o;
        return Objects.equals(productCodeId, that.productCodeId) &&
                Objects.equals(productCodeType, that.productCodeType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCodeId, productCodeType);
    }

    public static List<EC2ProductCode> fromSdk(List<ProductCode> productCodes) {
        if (Objects.isNull(productCodes)) {
            return Collections.emptyList();
        }

        return productCodes.stream()
                .map(EC2ProductCode::fromSdk)
                .collect(Collectors.toList());
    }

    public static EC2ProductCode fromSdk(ProductCode productCode) {
        if (Objects.isNull(productCode)) {
            return null;
        }

        return new EC2ProductCode(
                productCode
                        .getValueForField("ProductCodeId", String.class)
                        .orElse(null),
                productCode
                        .getValueForField("ProductCodeType", String.class)
                        .orElse(null)
        );
    }
}
