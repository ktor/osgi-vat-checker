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

public interface VatChecker {
    /**
     * Sends http request to VIES and gets validation result.
     *
     * @param country one of VIES supported countries
     * @param vatNumber correctly formatted vat number
     * @return 1) VAT valid: `valid=true`
     *         2) VAT invalid: `valid=false` and `error` is empty,
     *         3) VAT checker error: `valid=false` and `error` has enum value
     */
    VatCheckerResult checkVat(Country country, String vatNumber);
}
