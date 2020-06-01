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
import software.amazon.awssdk.services.ec2.model.GroupIdentifier;
import software.amazon.awssdk.services.ec2.model.SecurityGroup;
import software.amazon.awssdk.services.ec2.model.SecurityGroupIdentifier;

import java.util.List;

public class SecurityGroupGenerator extends BaseGenerator {
    public static String securityGroupId() {
        return String.format("sg-%s", faker.random().hex(10));
    }

    public static String securityGroupName() {
        return faker.lorem().word();
    }

    public static List<SecurityGroupIdentifier> securityGroupIdentifiers() {
        return securityGroupIdentifiers(faker.random().nextInt(1, 10));
    }

    public static List<SecurityGroupIdentifier> securityGroupIdentifiers(Integer n) {
        return listGenerator(n, SecurityGroupGenerator::securityGroupIdentifier);
    }

    public static SecurityGroupIdentifier securityGroupIdentifier() {
        return SecurityGroupIdentifier.builder()
                                      .groupId(securityGroupId())
                                      .groupName(securityGroupName())
                                      .build();
    }

    public static List<GroupIdentifier> groupIdentifiers() {
        return groupIdentifiers(faker.random().nextInt(1, 10));
    }

    public static List<GroupIdentifier> groupIdentifiers(Integer n) {
        return listGenerator(n, SecurityGroupGenerator::groupIdentifier);
    }

    public static GroupIdentifier groupIdentifier() {
        return GroupIdentifier.builder()
                              .groupId(securityGroupId())
                              .groupName(securityGroupName())
                              .build();
    }

    public static List<SecurityGroup> securityGroups() {
        return securityGroups(faker.random().nextInt(1, 10));
    }

    public static List<SecurityGroup> securityGroups(Integer n) {
        return listGenerator(n, SecurityGroupGenerator::securityGroup);
    }

    public static SecurityGroup securityGroup() {
        return SecurityGroup.builder()
                            .description(faker.lorem().sentence())
                            .groupName(faker.lorem().word())
                            .ipPermissions(IpPermissionGenerator.ipPermissions())
                            .ownerId(CommonGenerator.accountId())
                            .groupId(securityGroupId())
                            .ipPermissionsEgress(IpPermissionGenerator.ipPermissions())
                            .tags(TagGenerator.tags())
                            .vpcId(VpcGenerator.vpcId())
                            .build();
    }
}
