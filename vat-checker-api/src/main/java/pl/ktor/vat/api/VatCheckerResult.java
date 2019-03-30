package pl.ktor.vat.api;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class VatCheckerResult {

    private final String countryCode;
    private final String vatNumber;
    private final Optional<LocalDateTime> requestDate;
    private final boolean valid;
    private final Optional<String> name;
    private final Optional<String> address;

    public VatCheckerResult(String countryCode, String vatNumber, Date requestDate, boolean valid,
                                String name, String address) {
        this.countryCode = countryCode;
        this.vatNumber = vatNumber;
        this.requestDate = requestDate == null ? Optional.empty() : Optional.of(LocalDateTime.ofInstant(requestDate.toInstant(), ZoneId.systemDefault()));
        this.valid = valid;
        this.name = name == null ? Optional.empty() : Optional.of(name);
        this.address = address == null ? Optional.empty() : Optional.of(address);
    }

    public String getCountryCode() {
        return countryCode;
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
