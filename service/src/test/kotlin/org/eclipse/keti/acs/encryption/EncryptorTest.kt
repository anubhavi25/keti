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

package org.eclipse.keti.acs.encryption

import org.testng.Assert
import org.testng.annotations.Test

private const val VALUE_TO_ENCRYPT = "testValue"

class EncryptorTest {

    @Test
    fun testEncryptCompleteFlow() {
        val encryption = Encryptor()
        encryption.setEncryptionKey("FooBarFooBarFooB")
        Assert.assertNotEquals(encryption.encrypt(VALUE_TO_ENCRYPT), VALUE_TO_ENCRYPT)
        Assert.assertEquals(encryption.decrypt(encryption.encrypt(VALUE_TO_ENCRYPT)), VALUE_TO_ENCRYPT)
    }

    @Test(expectedExceptions = [(SymmetricKeyValidationException::class)])
    fun testCreateEncryptionWithTooShortOfAKey() {
        val encryption = Encryptor()
        encryption.setEncryptionKey("Too_short")
    }

    @Test
    fun testCreateEncryptionWithTooLongOfAKey() {
        try {
            val encryption = Encryptor()
            encryption.setEncryptionKey("Toooooooooo_loooooooooong")
        } catch (e: Throwable) {
            Assert.fail()
        }
    }
}
