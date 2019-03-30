package pl.ktor.vat.api;

public interface VatChecker {
    /**
     * @param country
     * @param vatNumber
     * @return
     */
    public VatCheckerResult checkVat(String country, String vatNumber);
}
