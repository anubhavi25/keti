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

package org.eclipse.keti.acs.attribute.readers

import org.eclipse.keti.acs.model.Attribute
import org.eclipse.keti.acs.privilege.management.PrivilegeManagementService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.Collections
import java.util.HashSet

@Component
class PrivilegeServiceResourceAttributeReader : ResourceAttributeReader {

    @Autowired
    private lateinit var privilegeManagementService: PrivilegeManagementService

    override fun getAttributes(identifier: String): Set<Attribute> {
        var resourceAttributes = emptySet<Attribute>()
        val resource = this.privilegeManagementService.getByResourceIdentifierWithInheritedAttributes(identifier)
        if (null != resource) {
            resourceAttributes = Collections.unmodifiableSet(HashSet(resource.attributes))
        }
        return resourceAttributes
    }
}
