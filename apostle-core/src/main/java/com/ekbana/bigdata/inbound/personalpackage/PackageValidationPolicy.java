package com.ekbana.bigdata.inbound.personalpackage;

import com.ekbana.bigdata.configuration.ApplicationConfiguration;
import com.ekbana.bigdata.entity.emails.Email;
import com.ekbana.bigdata.entity.emails.EmailEntry;
import com.ekbana.bigdata.entity.publickey.ExpiredPublicKey;
import com.ekbana.bigdata.inbound.publicKey.PublicKeyException;
import com.ekbana.bigdata.policy.Policy;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

@com.ekbana.bigdata.annotation.Policy(value = "package validation policy")
public class PackageValidationPolicy extends Policy {

    private final PackageService packageService;
    private final ApplicationConfiguration applicationConfiguration;

    @Autowired
    public PackageValidationPolicy(PackageService packageService, ApplicationConfiguration applicationConfiguration) {
        this.packageService = packageService;
        this.applicationConfiguration = applicationConfiguration;
    }

    @Override
    protected void pre(RequestWrapper requestWrapper) {
        // executes before getting response
        // time validation of package

        if (packageService.isInExpiredKeySpace(requestWrapper.getKeyClientApi().getUniqueId())){// key is in expired key space // will be done in database
            throw new ExpiredKeyException(applicationConfiguration.getEXPIRED_PUBLIC_KEY_MSG(),requestWrapper);
        }
        long timeLeftToExpire = requestWrapper.getPublicKeyAlias().getPublicKey().getExpiresAt() - Instant.now().getEpochSecond() / (24 * 60 * 60);

        if (timeLeftToExpire<0){
            // register the key as expired
            packageService.registerInExpiredKeySpace(new ExpiredPublicKey(
                    requestWrapper.getKeyClientApi().getUniqueId(),
                    Instant.now().getEpochSecond(),
                    "Expiration day reached"
            ));
            // send email to client
            requestWrapper.addEmail(
                    Email.builder()
                            .key(requestWrapper.getKeyClientApi().getUniqueId())
                            .to(requestWrapper.getKeyClientApi().getUserEmail())
                            .subject("Api Key has Expired")
                            .remark("This key has expired")
                            .type(EmailEntry.KEY_EXPIRED)
                            .build()
            );
            throw new PublicKeyException("public key has expired",requestWrapper);
        }else if (timeLeftToExpire < 100){ // remaining days watermark
            // register email and notification to be sent
            // check if given email is already sent or not
            // if not then only then register
        }
    }

}
