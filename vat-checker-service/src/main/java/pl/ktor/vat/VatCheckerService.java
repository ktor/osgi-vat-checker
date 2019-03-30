package pl.ktor.vat;

import org.apache.axis.AxisFault;
import org.apache.axis.holders.DateHolder;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.ktor.vat.api.VatChecker;
import pl.ktor.vat.api.VatCheckerResult;
import pl.ktor.vat.provider.CheckVatPortProvider;

import javax.xml.rpc.holders.BooleanHolder;
import javax.xml.rpc.holders.StringHolder;
import java.rmi.RemoteException;

@Component(immediate = true, property = {}, service = VatChecker.class)
public class VatCheckerService implements VatChecker {

    private static final Logger LOG = LoggerFactory.getLogger(VatCheckerService.class);

    @Reference
    CheckVatPortProvider checkVatPortProvider;

    @Override
    public VatCheckerResult checkVat(String country, String vatNumber) {
        StringHolder countryCodeHolder = new StringHolder(country);
        StringHolder vatNumberHolder = new StringHolder(vatNumber);
        DateHolder requestDateHolder = new DateHolder();
        BooleanHolder validHolder = new BooleanHolder();
        StringHolder nameHolder = new StringHolder();
        StringHolder addressHolder = new StringHolder();

        try {
            checkVatPortProvider.getCheckVatPort().checkVat(
                    countryCodeHolder,
                    vatNumberHolder,
                    requestDateHolder,
                    validHolder,
                    nameHolder,
                    addressHolder
            );
        } catch (AxisFault e) {
            LOG.error("Error while checking VAT: {}", e.getFaultString());
        } catch (RemoteException e) {
            LOG.error("Unexpected remote exception", e);
        }

        VatCheckerResult vatCheckerResult = new VatCheckerResult(
                countryCodeHolder.value,
                vatNumberHolder.value,
                requestDateHolder.value,
                validHolder.value,
                nameHolder.value,
                addressHolder.value
        );

        return vatCheckerResult;
    }
}
