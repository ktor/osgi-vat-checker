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

/**
 * Expected and unexpected VIES API errors.
 */
public enum VatCheckerError {
    INVALID_INPUT,
    INVALID_REQUESTER_INFO,
    SERVICE_UNAVAILABLE,
    MS_UNAVAILABLE,
    TIMEOUT,
    VAT_BLOCKED,
    IP_BLOCKED,
    GLOBAL_MAX_CONCURRENT_REQ,
    GLOBAL_MAX_CONCURRENT_REQ_TIME,
    MS_MAX_CONCURRENT_REQ,
    MS_MAX_CONCURRENT_REQ_TIME,
    UNEXPECTED, // default
    ;

    public static VatCheckerError valueOfOrDefault(String faultString) {
        try {
            return VatCheckerError.valueOf(faultString);
        } catch (IllegalArgumentException e) {
            return UNEXPECTED;
        }
    }
}
