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

package org.synus.vies.provider;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CheckVatPortProviderImplTest {

    CheckVatPortProvider checkVatPortProvider;

    @BeforeEach
    void setup() {
        checkVatPortProvider = new CheckVatPortProviderImpl();
    }

    @Test
    void getCheckVatPortNotNull() {
        assertNotNull(checkVatPortProvider.getCheckVatPort());
    }

    @Test
    void checkNullPointerHandling() throws NoSuchFieldException, IllegalAccessException {
        Field checkVatPortTypeField = CheckVatPortProviderImpl.class.getDeclaredField("checkVatPortType");
        checkVatPortTypeField.setAccessible(true);
        checkVatPortTypeField.set(checkVatPortProvider, null);
        assertNotNull(checkVatPortProvider.getCheckVatPort());
    }

    @Test
    void getCheckVatPortExceptions() {
    }
}
