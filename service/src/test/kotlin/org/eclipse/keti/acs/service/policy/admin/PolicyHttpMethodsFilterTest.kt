/*******************************************************************************
 * Copyright 2018 General Electric Company
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 *******************************************************************************/

package org.eclipse.keti.acs.service.policy.admin

import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.net.URI
import java.util.HashSet

private val ALL_HTTP_METHODS = HashSet(listOf(*HttpMethod.values()))

class PolicyHttpMethodsFilterTest {

    @InjectMocks
    private lateinit var policyManagementController: PolicyManagementController

    private var mockMvc: MockMvc? = null

    @BeforeClass
    fun setup() {
        MockitoAnnotations.initMocks(this)
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.policyManagementController)
            .addFilters<StandaloneMockMvcBuilder>(PolicyHttpMethodsFilter()).build()
    }

    @Test(dataProvider = "urisAndTheirAllowedHttpMethods")
    @Throws(Exception::class)
    fun testUriPatternsAndTheirAllowedHttpMethods(
        uri: String,
        allowedHttpMethods: Set<HttpMethod>
    ) {
        val disallowedHttpMethods = HashSet(ALL_HTTP_METHODS)
        disallowedHttpMethods.removeAll(allowedHttpMethods)
        for (disallowedHttpMethod in disallowedHttpMethods) {
            this.mockMvc!!.perform(MockMvcRequestBuilders.request(disallowedHttpMethod, URI.create(uri)))
                .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed)
        }
    }

    @DataProvider
    fun urisAndTheirAllowedHttpMethods(): Array<Array<out Any?>> {
        return arrayOf(
            arrayOf(
                "/v1/policy-set/foo", HashSet(
                    listOf(
                        HttpMethod.PUT, HttpMethod.GET, HttpMethod.DELETE, HttpMethod.HEAD,
                        HttpMethod.OPTIONS
                    )
                )
            ),
            arrayOf("/v1/policy-set", HashSet(listOf(HttpMethod.GET, HttpMethod.HEAD, HttpMethod.OPTIONS)))
        )
    }
}
