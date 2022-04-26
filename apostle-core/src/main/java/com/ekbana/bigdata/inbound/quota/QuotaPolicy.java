package com.ekbana.bigdata.inbound.quota;

import com.ekbana.bigdata.configuration.ApplicationConfiguration;
import com.ekbana.bigdata.entity.calls.Grace;
import com.ekbana.bigdata.entity.calls.Paid;
import com.ekbana.bigdata.entity.emails.Email;
import com.ekbana.bigdata.entity.emails.EmailEntry;
import com.ekbana.bigdata.entity.publickey.ExpiredPublicKey;
import com.ekbana.bigdata.policy.Policy;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import com.ekbana.bigdata.wrapper.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Arrays;

@com.ekbana.bigdata.annotation.Policy(value = "quota policy")
public class QuotaPolicy extends Policy {

    private final ApplicationConfiguration applicationConfiguration;
    private final QuotaService quotaService;

    @Autowired
    public QuotaPolicy(ApplicationConfiguration applicationConfiguration, QuotaService quotaService) {
        this.applicationConfiguration = applicationConfiguration;
        this.quotaService = quotaService;
    }

    @Override
    protected void post(RequestWrapper requestWrapper, ResponseWrapper responseWrapper) {
        Long totalCalls = requestWrapper.getKeyClientApi().getPersonalPackage().getFunnel().getTotalCalls();

        // get used calls from paid key space from redis
        Paid paid = quotaService.isInPaidKeySpace(requestWrapper.getKeyClientApi().getUniqueId()) ?
                quotaService.getFromPaidKeySpace(requestWrapper.getKeyClientApi().getUniqueId()) :
                new Paid(requestWrapper.getKeyClientApi().getUniqueId(), 0L, requestWrapper.getKeyClientApi().getPersonalPackage().getFunnel().getTotalCalls());

        if (paid.getAgreed_calls() != totalCalls) paid.setAgreed_calls(totalCalls);

        if (paid.getUsed_calls() >= paid.getAgreed_calls()) {
            // get used calls from grace call key space
            Grace grace = this.quotaService.isInGraceKeySpace(paid.getUuid()) ?
                    this.quotaService.getFromGraceKeySpace(paid.getUuid()) :
                    new Grace(paid.getUuid(), 0L, requestWrapper.getKeyClientApi().getPersonalPackage().getGraceCall());

            if (grace.getAgreed_grace_calls()==0){
                quotaService.registerInExpireKeySpace(new ExpiredPublicKey(requestWrapper.getKeyClientApi().getUniqueId(),Instant.now().getEpochSecond(),"Paid Calls Quota finished !!1"));
                requestWrapper.addEmail(
                        Email.builder()
                                .key(grace.getUuid())
                                .to(requestWrapper.getKeyClientApi().getUserEmail())
                                .subject("key expired. Paid Calls Quota finished")
                                .remark("This key has expired")
                                .type(EmailEntry.KEY_EXPIRED)
                                .build()
                );
                return;
            }else if (grace.getUsed_grace_calls() == 0) {
                requestWrapper.addEmail(
                        Email.builder()
                                .key(grace.getUuid())
                                .to(requestWrapper.getKeyClientApi().getUserEmail())
                                .subject("Grace calls")
                                .remark("Grace calls started")
                                .type(EmailEntry.GRACE_CALLS_STARTED)
                                .build()
                );
            } else if (grace.getAgreed_grace_calls() - 1 >= grace.getUsed_grace_calls()) {
                // expire the key
                quotaService.registerInExpireKeySpace(new ExpiredPublicKey(requestWrapper.getKeyClientApi().getUniqueId(), Instant.now().getEpochSecond(), "Paid Calls Quota finished!!!"));
                // register email and notification to be sent
                requestWrapper.addEmail(
                        Email.builder()
                                .key(grace.getUuid())
                                .to(requestWrapper.getKeyClientApi().getUserEmail())
                                .subject("key expired. Paid Calls Quota finished")
                                .remark("This key has expired")
                                .type(EmailEntry.KEY_EXPIRED)
                                .build()
                );
                // grace calls watermarks
            }else {
                Arrays.stream(applicationConfiguration.getGRACE_CALLS_LOWER_WATERMARKS())
                        .filter(c -> grace.getAgreed_grace_calls()-grace.getUsed_grace_calls() == c)
                        .findFirst()
                        .ifPresent(c -> requestWrapper.addEmail(
                                Email.builder()
                                        .key(grace.getUuid())
                                        .to(requestWrapper.getKeyClientApi().getUserEmail())
                                        .subject(c+" grace calls remaining")
                                        .remark("Grace calls alert")
                                        .type(EmailEntry.GRACE_CALLS_LEFT)
                                        .build()));
            }
            quotaService.increaseGraceCallUses(grace);
        } else {
            Arrays.stream(applicationConfiguration.getGRACE_CALLS_LOWER_WATERMARKS())
                    .filter(c -> paid.getAgreed_calls()-paid.getAgreed_calls() == c)
                    .findFirst()
                    .ifPresent(c -> requestWrapper.addEmail(
                            Email.builder()
                                    .key(paid.getUuid())
                                    .to(requestWrapper.getKeyClientApi().getUserEmail())
                                    .subject(c+" paid calls remaining")
                                    .remark("Paid calls alert")
                                    .type(EmailEntry.CALLS_LEFT)
                                    .build()));
            quotaService.increasePaidCallUses(paid);
        }
    }
}
