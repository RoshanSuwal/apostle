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
        Long totalGraceCall = requestWrapper.getKeyClientApi().getPersonalPackage().getGraceCall();

        Long paidCallUses = quotaService.isInPaidKeySpace(requestWrapper.getKeyClientApi().getUniqueId())
                ? quotaService.getFromPaidKeySpace(requestWrapper.getKeyClientApi().getUniqueId())
                : 0L;

        if (paidCallUses == 0) {
            quotaService.registerInPaidKeySpace(requestWrapper.getKeyClientApi().getUniqueId());
        }

        if (paidCallUses >= totalCalls) {
            Long graceCallsUses = this.quotaService.isInGraceKeySpace(requestWrapper.getKeyClientApi().getUniqueId())
                    ? this.quotaService.getFromGraceKeySpace(requestWrapper.getKeyClientApi().getUniqueId())
                    : 0L;

            if (totalGraceCall == 0) {
                quotaService.registerInExpireKeySpace(new ExpiredPublicKey(requestWrapper.getKeyClientApi().getUniqueId(), Instant.now().getEpochSecond(), "Paid Calls Quota finished !!1"));
                requestWrapper.addEmail(
                        Email.builder()
                                .key(requestWrapper.getKeyClientApi().getUniqueId())
                                .to(requestWrapper.getKeyClientApi().getUserEmail())
                                .subject("key expired. Paid Calls Quota finished")
                                .remark("This key has expired")
                                .type(EmailEntry.KEY_EXPIRED)
                                .build()
                );
                return;
            } else if (graceCallsUses == 0) {
                this.quotaService.registerInGraceKeySpace(requestWrapper.getKeyClientApi().getUniqueId());
                requestWrapper.addEmail(
                        Email.builder()
                                .key(requestWrapper.getKeyClientApi().getUniqueId())
                                .to(requestWrapper.getKeyClientApi().getUserEmail())
                                .subject("key expired. Paid Calls Quota finished")
                                .remark("This key has expired")
                                .type(EmailEntry.KEY_EXPIRED)
                                .build()
                );
            } else if (totalGraceCall - 1 >= graceCallsUses) {
                // expire the key
                quotaService.registerInExpireKeySpace(new ExpiredPublicKey(requestWrapper.getKeyClientApi().getUniqueId(), Instant.now().getEpochSecond(), "Paid Calls Quota finished!!!"));
                // register email and notification to be sent
                requestWrapper.addEmail(
                        Email.builder()
                                .key(requestWrapper.getKeyClientApi().getUniqueId())
                                .to(requestWrapper.getKeyClientApi().getUserEmail())
                                .subject("key expired. Paid Calls Quota finished")
                                .remark("This key has expired")
                                .type(EmailEntry.KEY_EXPIRED)
                                .build()
                );
                // grace calls watermarks
            } else {
                Arrays.stream(applicationConfiguration.getGRACE_CALLS_LOWER_WATERMARKS())
                        .filter(c -> totalGraceCall - graceCallsUses == c)
                        .findFirst()
                        .ifPresent(c -> requestWrapper.addEmail(
                                Email.builder()
                                        .key(requestWrapper.getKeyClientApi().getUniqueId())
                                        .to(requestWrapper.getKeyClientApi().getUserEmail())
                                        .subject(c + " grace calls remaining")
                                        .remark("Grace calls alert")
                                        .type(EmailEntry.GRACE_CALLS_LEFT)
                                        .build()));
            }
            quotaService.increaseGraceCallUses(requestWrapper.getKeyClientApi().getUniqueId());
        } else {
            Arrays.stream(applicationConfiguration.getGRACE_CALLS_LOWER_WATERMARKS())
                    .filter(c -> totalCalls - paidCallUses == c)
                    .findFirst()
                    .ifPresent(c -> requestWrapper.addEmail(
                            Email.builder()
                                    .key(requestWrapper.getKeyClientApi().getUniqueId())
                                    .to(requestWrapper.getKeyClientApi().getUserEmail())
                                    .subject(c+" paid calls remaining")
                                    .remark("Paid calls alert")
                                    .type(EmailEntry.CALLS_LEFT)
                                    .build()));
            quotaService.increasePaidCallUses(requestWrapper.getKeyClientApi().getUniqueId());
        }
    }
}
