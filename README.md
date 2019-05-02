[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![Build Status](https://travis-ci.org/synus-org/osgi-vat-checker.svg?branch=master)](https://travis-ci.org/synus-org/osgi-vat-checker)
[![codecov](https://codecov.io/gh/synus-org/osgi-vat-checker/branch/master/graph/badge.svg)](https://codecov.io/gh/synus-org/osgi-vat-checker)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=osgi-vat-checker&metric=alert_status)](https://sonarcloud.io/dashboard?id=osgi-vat-checker)
[![DepShield Badge](https://depshield.sonatype.org/badges/synus-org/osgi-vat-checker/depshield.svg)](https://depshield.github.io)
# OSGi VIES VAT number validation
## About
The project contains two OSGi bundles that provide online EU VAT number validation with VAT Information Exchange System ([VIES][1]).

1. `vat-checker-api.jar` - Java model for VIES API
1. `vat-checker-service.jar` - service client that uses classes generated from [WSDL published][2] by VIES

## API
Bundle offers simple Java service interface:
```java
package org.synus.vies.api;
public interface VatChecker {
    VatCheckerResult checkVat(Country country, String vatNumber);
```
The implementation catches all exceptions (logs them with slf4j) and always returns a result instance of type:
```java
// ...
public class VatCheckerResult {

    private final Country country;
    private final String vatNumber;
    private final Optional<LocalDateTime> requestDate;
    private final boolean valid;
    private final Optional<String> name;
    private final Optional<String> address;
    private final Optional<VatCheckerError> error;
// ...
```

## Result and error handling
Returned `VatCheckerResult` instance can be in following states:
1. VAT valid: `valid=true`
1. VAT invalid: `valid=false` and `error` is empty
1. VAT checker error: `valid=false` and `error` has enum value 
    ```java
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
        UNEXPECTED,
    }
    ```

# Installation
Build and install both api and implementation jars on your osgi container. 

```bash
$ ./gradlew build # to build both jars jar
$ cp vat-checker-api/build/libs/vat-checker-api-1.0.0.jar $MY_OSGI_CONTAINER
$ cp vat-checker-service/build/libs/vat-checker-service-1.0.0.jar $MY_OSGI_CONTAINER
```
## Usage
### Gradle project
`build.gradle`:
```gradle
dependencies {
    compileOnly 'org.synus.vies:vat-checker-api:1.0.0'
}
```
### Example: Liferay's gogo shell command
```java
@Component(
        property = {"osgi.command.function=checkvat", "osgi.command.scope=blade"},
        service = Object.class
)
public class ExampleCommand {

    @Reference
    private volatile VatChecker vatChecker;

    public void checkvat(String country, String vatNumber) {
        System.out.println(vatChecker.checkVat(Country.valueOf(country), vatNumber).isValid());
    }

}
```

```bash
$ telnet localhost 11311
Welcome to Apache Felix Gogo

g! checkvat "SK" "2020216748"
true
```

## Contributing

Want to hack on vat-checker? See [CONTRIBUTING.md](CONTRIBUTING.md) for information on building, testing and contributing changes. 

They are probably not perfect, please let me know if anything feels wrong or incomplete.

## License

The contents of this repository are made available to the public under the terms of the [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
Bundles may depend on non Apache Licensed code.

---
[1]:http://ec.europa.eu/taxation_customs/vies/
[2]:http://ec.europa.eu/taxation_customs/vies/technicalInformation.html
