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

import datagen.services.ec2.model.SecurityGroupGenerator;
import org.junit.Test;
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupsResponse;
import software.amazon.awssdk.services.ec2.model.SecurityGroup;

import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;

public class EC2SecurityGroupLoaderTest extends EC2LoaderTest {
    private final EC2SecurityGroupLoader loader = new EC2SecurityGroupLoader(getClientsProvider());

    @Test
    public void shouldLoadSecurityGroups() {
        var securityGroups = SecurityGroupGenerator.securityGroups();
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeSecurityGroupsRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeSecurityGroupsRequest.builder();
            builderLambda.accept(builder);
            assertTrue(builder.build().filters().isEmpty());
            return DescribeSecurityGroupsResponse.builder()
                                                 .securityGroups(securityGroups.toArray(new SecurityGroup[0]))
                                                 .build();
        }).when(getEc2Client()).describeSecurityGroups(any(Consumer.class));

        var resources = loader.getAll();

        assertNotNull(resources);
        assertEquals(securityGroups.size(), resources.size());
    }

    @Test
    public void shouldNotLoadSecurityGroupByRandomId() {
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeSecurityGroupsRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeSecurityGroupsRequest.builder();
            builderLambda.accept(builder);
            assertFalse(builder.build().filters().isEmpty());
            return DescribeSecurityGroupsResponse.builder()
                                                 .build();
        }).when(getEc2Client()).describeSecurityGroups(any(Consumer.class));

        var resource = loader.byId(SecurityGroupGenerator.securityGroupId());

        assertNotNull(resource);
    }

    @Test
    public void shouldLoadSecurityGroupById() {
        var securityGroups = SecurityGroupGenerator.securityGroups(1);
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeSecurityGroupsRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeSecurityGroupsRequest.builder();
            builderLambda.accept(builder);
            assertFalse(builder.build().filters().isEmpty());
            return DescribeSecurityGroupsResponse.builder()
                                                 .securityGroups(securityGroups.toArray(new SecurityGroup[0]))
                                                 .build();
        }).when(getEc2Client()).describeSecurityGroups(any(Consumer.class));

        var securityGroup = securityGroups.get(0);
        var resource = loader.byId(securityGroup.groupId());
        assertNotNull(resource);
    }
}
