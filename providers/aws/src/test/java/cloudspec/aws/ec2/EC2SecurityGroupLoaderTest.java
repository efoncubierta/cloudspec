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
package cloudspec.aws.ec2;

import cloudspec.aws.ec2.loader.EC2SecurityGroupLoader;
import datagen.services.ec2.model.SecurityGroupGenerator;
import org.junit.Test;
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupsResponse;
import software.amazon.awssdk.services.ec2.model.SecurityGroup;

import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class EC2SecurityGroupLoaderTest extends EC2LoaderTest {
    private final EC2SecurityGroupLoader loader = new EC2SecurityGroupLoader(clientsProvider);

    @Test
    public void shouldLoadSecurityGroups() {
        var securityGroups = SecurityGroupGenerator.securityGroups();
        when(ec2Client.describeSecurityGroups(any(Consumer.class))).thenReturn(
                DescribeSecurityGroupsResponse.builder()
                                              .securityGroups(securityGroups.toArray(new SecurityGroup[0]))
                                              .build()
        );

        var resources = loader.getAll();

        assertNotNull(resources);
        assertEquals(securityGroups.size(), resources.size());
        assertTrue(securityGroups.containsAll(resources));
    }

    @Test
    public void shouldNotLoadSecurityGroupByRandomId() {
        when(ec2Client.describeSecurityGroups(any(Consumer.class))).thenReturn(
                DescribeSecurityGroupsResponse.builder()
                                              .build()
        );

        var resourceOpt = loader.getById(SecurityGroupGenerator.securityGroupId());

        assertNotNull(resourceOpt);
        assertTrue(resourceOpt.isEmpty());
    }

    @Test
    public void shouldLoadSecurityGroupById() {
        var securityGroups = SecurityGroupGenerator.securityGroups(1);
        when(ec2Client.describeSecurityGroups(any(Consumer.class))).thenReturn(
                DescribeSecurityGroupsResponse.builder()
                                              .securityGroups(securityGroups.toArray(new SecurityGroup[0]))
                                              .build()
        );

        var securityGroup = securityGroups.get(0);
        var resourceOpt = loader.getById(securityGroup.groupId());
        assertNotNull(resourceOpt);
        assertTrue(resourceOpt.isPresent());
        assertEquals(resourceOpt.get(), securityGroup);
    }
}
