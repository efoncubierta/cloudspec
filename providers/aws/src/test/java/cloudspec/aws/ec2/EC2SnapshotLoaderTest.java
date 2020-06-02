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

import cloudspec.aws.ec2.loader.EC2SnapshotLoader;
import datagen.services.ec2.model.SnapshotGenerator;
import org.junit.Test;
import software.amazon.awssdk.services.ec2.model.DescribeSnapshotsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeSnapshotsResponse;
import software.amazon.awssdk.services.ec2.model.Snapshot;

import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;

public class EC2SnapshotLoaderTest extends EC2LoaderTest {
    private final EC2SnapshotLoader loader = new EC2SnapshotLoader(clientsProvider);

    @Test
    public void shouldLoadSnapshots() {
        var snapshots = SnapshotGenerator.snapshots();
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeSnapshotsRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeSnapshotsRequest.builder();
            builderLambda.accept(builder);
            assertTrue(builder.build().filters().isEmpty());
            return DescribeSnapshotsResponse.builder()
                                            .snapshots(snapshots.toArray(new Snapshot[0]))
                                            .build();
        }).when(ec2Client).describeSnapshots(any(Consumer.class));

        var resources = loader.getAll();

        assertNotNull(resources);
        assertEquals(snapshots.size(), resources.size());
        assertTrue(snapshots.containsAll(resources));
    }

    @Test
    public void shouldNotLoadSnapshotByRandomId() {
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeSnapshotsRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeSnapshotsRequest.builder();
            builderLambda.accept(builder);
            assertFalse(builder.build().filters().isEmpty());
            return DescribeSnapshotsResponse.builder()
                                            .build();
        }).when(ec2Client).describeSnapshots(any(Consumer.class));

        var resourceOpt = loader.getById(SnapshotGenerator.snapshotId());

        assertNotNull(resourceOpt);
        assertTrue(resourceOpt.isEmpty());
    }

    @Test
    public void shouldLoadSnapshotById() {
        var snapshots = SnapshotGenerator.snapshots(1);
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeSnapshotsRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeSnapshotsRequest.builder();
            builderLambda.accept(builder);
            assertFalse(builder.build().filters().isEmpty());
            return DescribeSnapshotsResponse.builder()
                                            .snapshots(snapshots.toArray(new Snapshot[0]))
                                            .build();
        }).when(ec2Client).describeSnapshots(any(Consumer.class));

        var snapshot = snapshots.get(0);
        var resourceOpt = loader.getById(snapshot.snapshotId());
        assertNotNull(resourceOpt);
        assertTrue(resourceOpt.isPresent());
        assertEquals(resourceOpt.get(), snapshot);
    }
}
