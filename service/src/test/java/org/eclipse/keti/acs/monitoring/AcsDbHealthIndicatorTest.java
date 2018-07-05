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

package org.eclipse.keti.acs.monitoring;

import static org.eclipse.keti.acs.monitoring.AcsDbHealthIndicatorKt.DB_DESCRIPTION;
import static org.eclipse.keti.acs.monitoring.AcsMonitoringUtilitiesKt.CODE_KEY;
import static org.eclipse.keti.acs.monitoring.AcsMonitoringUtilitiesKt.DESCRIPTION_KEY;

import org.eclipse.keti.acs.privilege.management.dao.GraphMigrationManager;
import org.mockito.Mockito;
import org.springframework.boot.actuate.health.Status;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AcsDbHealthIndicatorTest {

    private static final String IS_MIGRATION_COMPLETE_FIELD_NAME = "isMigrationComplete";

    @Test(dataProvider = "statuses")
    public void testHealth(final AcsMonitoringRepository acsMonitoringRepository, final Status status,
            final HealthCode healthCode, final GraphMigrationManager graphMigrationManager)
            throws Exception {
        AcsDbHealthIndicator acsDbHealthIndicator = new AcsDbHealthIndicator(acsMonitoringRepository);
        acsDbHealthIndicator.setMigrationManager(graphMigrationManager);
        Assert.assertEquals(status, acsDbHealthIndicator.health().getStatus());
        Assert.assertEquals(DB_DESCRIPTION,
                            acsDbHealthIndicator.health().getDetails().get(
                                    DESCRIPTION_KEY));
        if (healthCode == HealthCode.AVAILABLE) {
            Assert.assertFalse(acsDbHealthIndicator.health().getDetails().containsKey(
                    CODE_KEY));
        } else {
            Assert.assertEquals(healthCode,
                    acsDbHealthIndicator.health().getDetails().get(CODE_KEY));
        }
    }

    @DataProvider
    public Object[][] statuses() {
        GraphMigrationManager happyMigrationManager = new GraphMigrationManager();
        GraphMigrationManager sadMigrationManager = new GraphMigrationManager();
        ReflectionTestUtils.setField(happyMigrationManager, IS_MIGRATION_COMPLETE_FIELD_NAME, true);
        ReflectionTestUtils.setField(sadMigrationManager, IS_MIGRATION_COMPLETE_FIELD_NAME, false);

        return new Object[][] { new Object[] { mockDbWithUp(), Status.UP, HealthCode.AVAILABLE,
                happyMigrationManager },

                { mockDbWithException(new TransientDataAccessResourceException("")), Status.DOWN,
                        HealthCode.UNAVAILABLE, happyMigrationManager },

                { mockDbWithException(new QueryTimeoutException("")), Status.DOWN,
                        HealthCode.UNAVAILABLE, happyMigrationManager },

                { mockDbWithException(new DataSourceLookupFailureException("")), Status.DOWN,
                        HealthCode.UNREACHABLE, happyMigrationManager },

                { mockDbWithException(new PermissionDeniedDataAccessException("", null)), Status.DOWN,
                        HealthCode.MISCONFIGURATION, happyMigrationManager },

                { mockDbWithException(new ConcurrencyFailureException("")), Status.DOWN,
                        HealthCode.ERROR, happyMigrationManager },

                { mockDbWithUp(), Status.DOWN, HealthCode.MIGRATION_INCOMPLETE,
                        sadMigrationManager }, };
    }

    private AcsMonitoringRepository mockDbWithUp() {
        AcsMonitoringRepository acsMonitoringRepository = Mockito.mock(AcsMonitoringRepository.class);
        Mockito.doNothing().when(acsMonitoringRepository).queryPolicySetTable();
        return acsMonitoringRepository;
    }

    private AcsMonitoringRepository mockDbWithException(final Exception e) {
        AcsMonitoringRepository acsMonitoringRepository = Mockito.mock(AcsMonitoringRepository.class);
        Mockito.doAnswer(invocation -> {
            throw e;
        }).when(acsMonitoringRepository).queryPolicySetTable();
        return acsMonitoringRepository;
    }
}
