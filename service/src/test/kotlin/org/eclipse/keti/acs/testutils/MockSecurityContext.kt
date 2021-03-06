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

package org.eclipse.keti.acs.testutils

import com.ge.predix.uaa.token.lib.ZoneOAuth2Authentication
import org.eclipse.keti.acs.rest.Zone
import org.mockito.Mockito
import org.springframework.security.core.context.SecurityContextHolder

/**
 * @author acs-engineers@ge.com
 */

fun mockSecurityContext(zone: Zone?) {
    val acsZoneOAuth = Mockito.mock(ZoneOAuth2Authentication::class.java)
    if (zone != null) {
        Mockito.`when`(acsZoneOAuth.zoneId).thenReturn(zone.subdomain)
    }
    SecurityContextHolder.getContext().authentication = acsZoneOAuth
}
