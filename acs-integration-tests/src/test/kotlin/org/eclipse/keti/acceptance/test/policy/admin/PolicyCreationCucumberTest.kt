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

package org.eclipse.keti.acceptance.test.policy.admin

import cucumber.api.CucumberOptions
import cucumber.api.testng.AbstractTestNGCucumberTests
import org.eclipse.keti.test.TestConfig
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

/**
 * @author acs-engineers@ge.com
 */
@Test
@CucumberOptions(tags = ["~@ignore"])
class PolicyCreationCucumberTest : AbstractTestNGCucumberTests() {
    // Used as the entry point for PolicyEvaluation StepsDefinitions

    @BeforeClass
    @Throws(Exception::class)
    fun setup() {
        TestConfig.setupForEclipse() // Starts ACS when running the test in eclipse.
    }
}
