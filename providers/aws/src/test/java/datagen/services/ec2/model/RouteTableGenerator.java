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
package datagen.services.ec2.model;

import datagen.BaseGenerator;
import datagen.CommonGenerator;
import software.amazon.awssdk.services.ec2.model.RouteTable;

import java.util.List;

public class RouteTableGenerator extends BaseGenerator {
    public static String routeTableId() {
        return String.format("rtb-%s", faker.random().hex(30));
    }

    public static List<RouteTable> routeTables() {
        return routeTables(faker.random().nextInt(1, 10));
    }

    public static List<RouteTable> routeTables(Integer n) {
        return listGenerator(n, RouteTableGenerator::routeTable);
    }

    public static RouteTable routeTable() {
        return RouteTable.builder()
                         .associations(RouteTableAssociationGenerator.routeTableAssociations())
                         .propagatingVgws(PropagatingVgwGenerator.propagatingVgws())
                         .routeTableId(routeTableId())
                         .routes(RouteGenerator.routes())
                         .tags(TagGenerator.tags())
                         .vpcId(VpcGenerator.vpcId())
                         .ownerId(CommonGenerator.accountId())
                         .build();
    }
}
