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
package cloudspec.aws.ec2

import cloudspec.aws.AWSResourceLoader
import cloudspec.aws.IAWSClientsProvider
import software.amazon.awssdk.services.ec2.model.Region

abstract class EC2ResourceLoader<T : EC2Resource>(protected val clientsProvider: IAWSClientsProvider) : AWSResourceLoader<T> {
    override fun byId(id: String): T? {
        return getResources(listOf(id)).firstOrNull()
    }

    override val all: List<T>
        get() {
            return getResources(emptyList())
        }

    private fun getResources(ids: List<String>): List<T> {
        return availableRegions
                .flatMap { region ->
                    getResourcesInRegion(
                            region,
                            ids
                    )
                }
    }

    abstract fun getResourcesInRegion(region: String, ids: List<String>): List<T>

    protected val availableRegions: List<String>
        get() {
            clientsProvider.ec2Client.use { ec2Client ->
                return ec2Client.describeRegions()
                        .regions()
                        .map { obj: Region -> obj.regionName() }
            }
        }
}
