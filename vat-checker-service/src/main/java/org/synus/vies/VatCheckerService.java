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

import org.apache.axis.AxisFault;
import org.apache.axis.holders.DateHolder;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.synus.vies.api.Country;
import org.synus.vies.api.VatChecker;
import org.synus.vies.api.VatCheckerError;
import org.synus.vies.api.VatCheckerResult;
import org.synus.vies.provider.CheckVatPortProvider;

import javax.xml.rpc.holders.BooleanHolder;
import javax.xml.rpc.holders.StringHolder;

@Component(immediate = true, property = {}, service = VatChecker.class)
public class VatCheckerService implements VatChecker {

    private static final Logger LOG = LoggerFactory.getLogger(VatCheckerService.class);

    @Reference
    CheckVatPortProvider checkVatPortProvider;

    @Override
    public VatCheckerResult checkVat(Country country, String vatNumber) {
        StringHolder countryCodeHolder = new StringHolder(country.name());
        StringHolder vatNumberHolder = new StringHolder(vatNumber);
        DateHolder requestDateHolder = new DateHolder();
        BooleanHolder validHolder = new BooleanHolder(false);
        StringHolder nameHolder = new StringHolder();
        StringHolder addressHolder = new StringHolder();
        VatCheckerError error = null;

        try {
            checkVatPortProvider.getCheckVatPort().checkVat(
                    countryCodeHolder,
                    vatNumberHolder,
                    requestDateHolder,
                    validHolder,
                    nameHolder,
                    addressHolder
            );
        } catch (AxisFault e) {
            error = VatCheckerError.valueOfOrDefault(e.getFaultString());
            LOG.warn("Error while checking VAT: {}", e.getFaultString());
        } catch (Exception e) {
            error = VatCheckerError.UNEXPECTED;
            LOG.error("Unexpected exception", e);
        }

        VatCheckerResult vatCheckerResult = new VatCheckerResult(
                Country.valueOf(countryCodeHolder.value),
                vatNumberHolder.value,
                requestDateHolder.value,
                validHolder.value,
                nameHolder.value,
                addressHolder.value,
                error
        );

        return vatCheckerResult;
    }
}
