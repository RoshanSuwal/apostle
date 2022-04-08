package com.ekbana.bigdata.inbound.email;

import com.ekbana.bigdata.annotation.PostService;
import com.ekbana.bigdata.entity.emails.Email;
import com.ekbana.bigdata.entity.emails.EmailEntry;
import com.ekbana.bigdata.helpers.HTTPRequestDispatcher;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import com.ekbana.bigdata.wrapper.ResponseWrapper;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@PostService(value = "email service")
public class EmailService extends com.ekbana.bigdata.post.PostService {

    @Autowired
    EmailRedisRepository emailRedisRepository;

    @Override
    protected void execute(RequestWrapper requestWrapper, ResponseWrapper responseWrapper) {
        for (Email email : requestWrapper.getEmails()) {
            EmailEntry emailEntry = this.emailRedisRepository.getEmail(email.getKey());
            if (emailEntry == null || !emailEntry.getType().contains(email.getType())) {
                HTTPRequestDispatcher.builder()
                        .okHttp(new OkHttpClient())
//                        .url(applicationConfig.getEMAIL_URL())
                        .build()
                        .sendEmail(
                                email.getTo(),
                                email.getSubject(),
                                email.getRemark(),
                                email.getType()
                        );

                emailEntry = emailEntry == null ? new EmailEntry(email.getKey(), new ArrayList<>()) : emailEntry;
                emailEntry.getType().add(email.getType());
                emailRedisRepository.registerEmail(emailEntry);
            }

        }
    }
}
