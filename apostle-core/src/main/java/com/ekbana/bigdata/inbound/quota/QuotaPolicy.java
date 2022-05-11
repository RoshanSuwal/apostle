package com.ekbana.bigdata.inbound.quota;

import com.ekbana.bigdata.configuration.ApplicationConfiguration;
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
        Long totalPaidCalls = requestWrapper.getKeyClientApi().getPersonalPackage().getFunnel().getTotalCalls();

        Long usedPaidCalls = quotaService.isInUsedPaidKeySpace(requestWrapper.getKeyClientApi().getUniqueId())
                ? quotaService.getFromUsedPaidKeySpace(requestWrapper.getKeyClientApi().getUniqueId())
                : 0L;
        if (usedPaidCalls == 0) quotaService.registerInUsedPaidKeySpace(requestWrapper.getKeyClientApi().getUniqueId());

        if (usedPaidCalls >= totalPaidCalls) {
            final long usedGraceCalls = this.quotaService.isInUsedGraceKeySpace(requestWrapper.getKeyClientApi().getUniqueId())
                    ? this.quotaService.getFromUsedGraceKeySpace(requestWrapper.getKeyClientApi().getUniqueId())
                    : 0L;
            final long totalGraceCalls = requestWrapper.getKeyClientApi().getPersonalPackage().getGraceCall();
            if (totalGraceCalls == 0) {
                quotaService.registerInExpireKeySpace(new ExpiredPublicKey(requestWrapper.getKeyClientApi().getUniqueId(), Instant.now().getEpochSecond(), "Paid Calls Quota finished !!!"));
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
            } else if (usedGraceCalls == 0) {
                this.quotaService.registerInUsedGraceKeySpace(requestWrapper.getKeyClientApi().getUniqueId());
                requestWrapper.addEmail(
                        Email.builder()
                                .key(requestWrapper.getKeyClientApi().getUniqueId())
                                .to(requestWrapper.getKeyClientApi().getUserEmail())
                                .subject("Grace calls")
                                .remark("Grace calls started")
                                .type(EmailEntry.GRACE_CALLS_STARTED)
                                .build()
                );
            } else if (totalGraceCalls - usedGraceCalls <= 1) {
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
            } else {
                Arrays.stream(applicationConfiguration.getGRACE_CALLS_LOWER_WATERMARKS())
                        .filter(c -> totalGraceCalls - usedGraceCalls == c)
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
            quotaService.increaseUsedGraceCallUses(requestWrapper.getKeyClientApi().getUniqueId());

        } else {
            Arrays.stream(applicationConfiguration.getGRACE_CALLS_LOWER_WATERMARKS())
                    .filter(c -> totalPaidCalls - usedPaidCalls == c)
                    .findFirst()
                    .ifPresent(c -> requestWrapper.addEmail(
                            Email.builder()
                                    .key(requestWrapper.getKeyClientApi().getUniqueId())
                                    .to(requestWrapper.getKeyClientApi().getUserEmail())
                                    .subject(c + " paid calls remaining")
                                    .remark("Paid calls alert")
                                    .type(EmailEntry.CALLS_LEFT)
                                    .build()));
            quotaService.increaseUsedPaidCallUses(requestWrapper.getKeyClientApi().getUniqueId());
        }
    }
}
