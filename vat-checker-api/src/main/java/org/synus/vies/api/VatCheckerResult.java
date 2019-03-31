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

package org.synus.vies.api;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

/**
 * Immutable and defensive result of VIES VAT validation service.
 */
public class VatCheckerResult {

    private final Country country;
    private final String vatNumber;
    private final Optional<LocalDateTime> requestDate;
    private final boolean valid;
    private final Optional<String> name;
    private final Optional<String> address;
    private final Optional<VatCheckerError> error;

    public VatCheckerResult(Country country, String vatNumber, Date requestDate, boolean valid, String name,
            String address, VatCheckerError error) {
        this.country = country;
        this.vatNumber = vatNumber;
        this.requestDate = requestDate == null
                ? Optional.empty()
                : Optional.of(LocalDateTime.ofInstant(requestDate.toInstant(), ZoneId.systemDefault()));
        this.valid = valid;
        this.name = name == null ? Optional.empty() : Optional.of(name);
        this.address = address == null ? Optional.empty() : Optional.of(address);
        this.error = error == null ? Optional.empty() : Optional.of(error);
    }

    public Optional<VatCheckerError> getError() {
        return error;
    }

    public Country getCountryCode() {
        return country;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public Optional<LocalDateTime> getRequestDate() {
        return requestDate;
    }

    public boolean isValid() {
        return valid;
    }

    public Optional<String> getName() {
        return name;
    }

    public Optional<String> getAddress() {
        return address;
    }
}
