package pl.ktor.vat;

import checkVat.services.vies.taxud.eu.europa.ec.CheckVatPortType;
import checkVat.services.vies.taxud.eu.europa.ec.CheckVatTestServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.ktor.vat.provider.CheckVatPortProvider;

import javax.xml.rpc.ServiceException;

class CheckVatPortProviderTestImpl implements CheckVatPortProvider {

    public static final Logger LOG = LoggerFactory.getLogger(CheckVatPortProviderTestImpl.class);

    private CheckVatTestServiceLocator checkVatServiceLocator = new CheckVatTestServiceLocator();
    private CheckVatPortType checkVatPortType = null;

    public CheckVatPortProviderTestImpl() {
        try {
            checkVatPortType = checkVatServiceLocator.getcheckVatPort();
        } catch (ServiceException e) {
            LOG.error("Unable to initialize VAT checker", e);
        }
    }

    public CheckVatPortType getCheckVatPort() {
        if (checkVatPortType == null) {
            try {
                checkVatPortType = checkVatServiceLocator.getcheckVatPort();
            } catch (ServiceException e) {
                LOG.error("Unable to initialize VAT checker", e);
            }
        }
        return checkVatPortType;
    }
}
