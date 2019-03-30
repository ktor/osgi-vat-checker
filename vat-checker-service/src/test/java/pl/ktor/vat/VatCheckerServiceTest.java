package pl.ktor.vat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        testErrorCode("201");
        testErrorCode("202");
        testErrorCode("300");
        testErrorCode("301");
        testErrorCode("302");
        testErrorCode("400");
        testErrorCode("401");
        testErrorCode("500");
        testErrorCode("501");
        testErrorCode("600");
        testErrorCode("601");
    }

    private void testErrorCode(String errorCode) {
        pl.ktor.vat.api.VatCheckerResult vatCheckerResult = vatCheckerService.checkVat("PL", errorCode);
        assertFalse(vatCheckerResult.isValid());
        assertEquals(errorCode, vatCheckerResult.getVatNumber());
    }

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field field = VatCheckerService.class.getDeclaredField("checkVatPortProvider");
        field.setAccessible(true);
        field.set(vatCheckerService, new CheckVatPortProviderTestImpl());
    }

    @Test
    void checkInvalidVat() {
        final pl.ktor.vat.api.VatCheckerResult vatCheckerResult = vatCheckerService.checkVat("PL", "200");
        assertFalse(vatCheckerResult.isValid());
    }

    @Test
    void checkValidVat() {
        final pl.ktor.vat.api.VatCheckerResult vatCheckerResult = vatCheckerService.checkVat("PL", "100");
        assertTrue(vatCheckerResult.isValid());
    }
}
