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

package org.synus.vies.test.util;

import checkVat.services.vies.taxud.eu.europa.ec.CheckVatPortType;
import checkVat.services.vies.taxud.eu.europa.ec.CheckVatTestServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.synus.vies.provider.CheckVatPortProvider;

import javax.xml.rpc.ServiceException;

public class CheckVatPortProviderTestImpl implements CheckVatPortProvider {

    public static final Logger LOG = LoggerFactory.getLogger(CheckVatPortProviderTestImpl.class);

    private CheckVatTestServiceLocator checkVatServiceLocator = new CheckVatTestServiceLocator();
    private CheckVatPortType checkVatPortType = null;

    public CheckVatPortProviderTestImpl() {
        try {
            checkVatPortType = checkVatServiceLocator.getcheckVatPort();
        } catch (ServiceException e) {
            LOG.error("Unable to initialize VAT checker", e);
        }
    }

    public CheckVatPortType getCheckVatPort() {
        if (checkVatPortType == null) {
            try {
                checkVatPortType = checkVatServiceLocator.getcheckVatPort();
            } catch (ServiceException e) {
                LOG.error("Unable to initialize VAT checker", e);
            }
        }
        return checkVatPortType;
    }
}
