package pl.ktor.vat.provider;

import checkVat.services.vies.taxud.eu.europa.ec.CheckVatPortType;
import checkVat.services.vies.taxud.eu.europa.ec.CheckVatServiceLocator;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.rpc.ServiceException;

@Component(immediate = true, property = {}, service = CheckVatPortProvider.class)
public class CheckVatPortProviderImpl implements CheckVatPortProvider {

    public static final Logger LOG = LoggerFactory.getLogger(CheckVatPortProviderImpl.class);

    private CheckVatServiceLocator checkVatServiceLocator = new CheckVatServiceLocator();
    private CheckVatPortType checkVatPortType = null;

    public CheckVatPortProviderImpl() {
        try {
            checkVatPortType = checkVatServiceLocator.getcheckVatPort();
        } catch (ServiceException e) {
            LOG.error("Unable to initialize VAT checker", e);
        }
    }

    @Override
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
