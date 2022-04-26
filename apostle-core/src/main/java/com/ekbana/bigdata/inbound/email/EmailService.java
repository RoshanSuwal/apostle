package com.ekbana.bigdata.inbound.email;

import com.ekbana.bigdata.annotation.PostService;
import com.ekbana.bigdata.entity.emails.EmailEntry;
import com.ekbana.bigdata.entity.emails.Emails;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import com.ekbana.bigdata.wrapper.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PostService(value = "email service")
public class EmailService extends com.ekbana.bigdata.post.PostService {

    private final EmailRedisRepository emailRedisRepository;
    private final EmailRepository emailRepository;

    @Autowired
    public EmailService(EmailRedisRepository emailRedisRepository, EmailRepository emailRepository) {
        this.emailRedisRepository = emailRedisRepository;
        this.emailRepository = emailRepository;
    }

    @Override
    protected void execute(RequestWrapper requestWrapper, ResponseWrapper responseWrapper) {
//        for (Email email : requestWrapper.getEmails()) {
//            EmailEntry emailEntry = this.emailRedisRepository.getEmail(email.getKey());
//            if (emailEntry == null || !emailEntry.getType().contains(email.getType())) {
//
//                emailRepository.save(new Emails(email));
////                HTTPRequestDispatcher.builder()
////                        .okHttp(new OkHttpClient())
//////                        .url(applicationConfig.getEMAIL_URL())
////                        .build()
////                        .sendEmail(
////                                email.getTo(),
////                                email.getSubject(),
////                                email.getRemark(),
////                                email.getType()
////                        );
//                emailEntry = emailEntry == null ? new EmailEntry(email.getKey(), new ArrayList<>()) : emailEntry;
//                emailEntry.getType().add(email.getType());
//                emailRedisRepository.registerEmail(emailEntry);
//            }
//
//        }

        if (requestWrapper.getEmails().size()>0){
            EmailEntry emailEntry = Optional.ofNullable(this.emailRedisRepository.getEmail(requestWrapper.getKeyClientApi().getUniqueId()))
                    .orElse(new EmailEntry(requestWrapper.getKeyClientApi().getUniqueId(),new ArrayList<>()));
            List<Emails> emails = requestWrapper.getEmails().stream().filter(email -> !emailEntry.getType().contains(email.getType()))
                    .map(email -> {
                        emailEntry.getType().add(email.getType());
                        return new Emails(email);
                    }).collect(Collectors.toList());

            emailRedisRepository.registerEmail(emailEntry);
//            emailRepository.saveAll(emails);
            emails.forEach(email->emailRepository.save(email));
        }
    }
}
