package pl.ktor.vat.provider;

import checkVat.services.vies.taxud.eu.europa.ec.CheckVatPortType;

public interface CheckVatPortProvider {
    CheckVatPortType getCheckVatPort();
}
