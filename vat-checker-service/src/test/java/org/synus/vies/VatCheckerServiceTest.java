/*
 * Copyright (c) 2019. Pawel Kruszewski
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
 */

package org.synus.vies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.synus.vies.api.Country;
import org.synus.vies.api.VatCheckerError;
import org.synus.vies.api.VatCheckerResult;
import org.synus.vies.test.util.CheckVatPortProviderTestImpl;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 100 = Valid request with Valid VAT Number
 * 200 = Valid request with an Invalid VAT Number
 * 201 = Error : INVALID_INPUT
 * 202 = Error : INVALID_REQUESTER_INFO
 * 300 = Error : SERVICE_UNAVAILABLE
 * 301 = Error : MS_UNAVAILABLE
 * 302 = Error : TIMEOUT
 * 400 = Error : VAT_BLOCKED
 * 401 = Error : IP_BLOCKED
 * 500 = Error : GLOBAL_MAX_CONCURRENT_REQ
 * 501 = Error : GLOBAL_MAX_CONCURRENT_REQ_TIME
 * 600 = Error : MS_MAX_CONCURRENT_REQ
 * 601 = Error : MS_MAX_CONCURRENT_REQ_TIME
 */
class VatCheckerServiceTest {

    private VatCheckerService vatCheckerService = new VatCheckerService();

    @Test
    void checkErrorCodes() {
        testErrorCode("201", VatCheckerError.INVALID_INPUT);
        testErrorCode("202", VatCheckerError.INVALID_REQUESTER_INFO);
        testErrorCode("300", VatCheckerError.SERVICE_UNAVAILABLE);
        testErrorCode("301", VatCheckerError.MS_UNAVAILABLE);
        testErrorCode("302", VatCheckerError.TIMEOUT);
        testErrorCode("400", VatCheckerError.VAT_BLOCKED);
        testErrorCode("401", VatCheckerError.IP_BLOCKED);
        testErrorCode("500", VatCheckerError.GLOBAL_MAX_CONCURRENT_REQ);
        testErrorCode("501", VatCheckerError.GLOBAL_MAX_CONCURRENT_REQ_TIME);
        testErrorCode("600", VatCheckerError.MS_MAX_CONCURRENT_REQ);
        testErrorCode("601", VatCheckerError.MS_MAX_CONCURRENT_REQ_TIME);
    }

    private void testErrorCode(String wrongVatNumber, VatCheckerError error) {
        VatCheckerResult vatCheckerResult = vatCheckerService.checkVat(Country.PL, wrongVatNumber);
        assertFalse(vatCheckerResult.isValid());
        assertEquals(wrongVatNumber, vatCheckerResult.getVatNumber());
        assertEquals(Country.PL, vatCheckerResult.getCountryCode());
        assertTrue(vatCheckerResult.getError().isPresent());
        assertEquals(error, vatCheckerResult.getError().get());
    }

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field field = VatCheckerService.class.getDeclaredField("checkVatPortProvider");
        field.setAccessible(true);
        field.set(vatCheckerService, new CheckVatPortProviderTestImpl());
    }

    @Test
    void checkInvalidVat() {
        final VatCheckerResult vatCheckerResult = vatCheckerService.checkVat(Country.PL, "200");
        assertFalse(vatCheckerResult.isValid());
    }

    @Test
    void checkValidVat() {
        final VatCheckerResult vatCheckerResult = vatCheckerService.checkVat(Country.PL, "100");
        assertTrue(vatCheckerResult.isValid());
    }
}
