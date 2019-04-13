/*
 * Copyright (c) 2019. Pawel Kruszewski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.synus.vies;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.Date;

import checkVat.services.vies.taxud.eu.europa.ec.CheckVatPortType;
import checkVat.services.vies.taxud.eu.europa.ec.CheckVatTestServiceLocator;
import org.apache.axis.AxisFault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.synus.vies.api.Country;
import org.synus.vies.api.VatCheckerError;
import org.synus.vies.api.VatCheckerResult;
import org.synus.vies.provider.CheckVatPortProvider;

import javax.xml.rpc.ServiceException;

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
@ExtendWith(MockitoExtension.class)
class VatCheckerServiceTest {

    private VatCheckerService vatCheckerService= new VatCheckerService();
    private CheckVatTestServiceLocator checkVatServiceLocator = new CheckVatTestServiceLocator();
    final CheckVatPortProvider checkVatPortProviderMock = Mockito.mock(CheckVatPortProvider.class);

    VatCheckerServiceTest() {
        vatCheckerService.checkVatPortProvider = checkVatPortProviderMock;
    }

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

    @Test
    void checkUnexpectedException() {
        Mockito.when(vatCheckerService.checkVatPortProvider.getCheckVatPort())
                .thenThrow(new IllegalStateException("mock unexpected exception"));
        testErrorCode("007", VatCheckerError.UNEXPECTED);
    }

    @Test
    void checkUnexpectedErrorCode() throws RemoteException {
        final CheckVatPortType checkVatPortTypeMock = Mockito.mock(CheckVatPortType.class);
        Mockito.when(vatCheckerService.checkVatPortProvider.getCheckVatPort()).thenReturn(checkVatPortTypeMock);

        final AxisFault axisFault = new AxisFault();
        axisFault.setFaultString("NEW_UNSUPPORTED_FAULT_CODE");
        Mockito.doThrow(axisFault).when(checkVatPortTypeMock).checkVat(any(), any(), any(), any(), any(), any());

        testErrorCode("007", VatCheckerError.UNEXPECTED);
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
    void setUp() throws ServiceException {
        Mockito.when(vatCheckerService.checkVatPortProvider.getCheckVatPort()).thenReturn(checkVatServiceLocator.getcheckVatPort());
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

        assertTrue(vatCheckerResult.getAddress().isPresent());
        assertEquals("123 Main St, Anytown, UK", vatCheckerResult.getAddress().get());

        assertTrue(vatCheckerResult.getName().isPresent());
        assertEquals("John Doe", vatCheckerResult.getName().get());

        assertTrue(vatCheckerResult.getRequestDate().isPresent());
        assertEquals(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0), vatCheckerResult.getRequestDate().get());
    }
}
