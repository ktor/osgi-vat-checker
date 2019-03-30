[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![Build Status](https://travis-ci.org/ktor/osgi-vat-checker.svg?branch=master)](https://travis-ci.org/ktor/osgi-vat-checker)

# OSGI VIES VAT number validation
The project contains two osgi bundler API/Implementation that allow online EU VAT number validation.

Implementation uses API for service available on [EU VIES pages][1]. Current version of the API and documentation is available at [technical information pages][2].

# Installation
Build and install both api and implementation jars on your osgi container. 

## Usage
### Gradle project
`build.gradle`:
```gradle
dependencies {
    compileOnly 'pl.ktor.vat:vat-checker-api:1.0.0'
}
```
```java
@Component(
        property = {"osgi.command.function=checkvat", "osgi.command.scope=custom"},
        service = Object.class
)
public class LukreoCommand {

    @Reference
    private volatile VatChecker vatChecker;

    public void checkvat(String country, String vatNumber) {
        System.out.println(vatChecker.checkVat(country, vatNumber).isValid());
    }

}
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
