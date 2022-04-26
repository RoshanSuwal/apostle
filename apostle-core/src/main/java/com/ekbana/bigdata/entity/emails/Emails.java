package com.ekbana.bigdata.entity.emails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "emails")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Emails implements Serializable {
    @Id @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_seq")
//    @SequenceGenerator(name = "email_seq",sequenceName = "email_seq")
    private int id;
    @Column(name = "subject")
    private String subject;
    @Column(name = "\"to\"")
    private String to;
    @Column(name = "body")
    private String body;
    @Column(name = "type")
    private int type;
    @Column(name = "sent")
    private boolean sent;

    public Emails(Email email){
        this.to=email.to;
        this.subject=email.getSubject();
        this.body=email.getRemark();
        this.type=email.type;
        this.sent=false;
    }
}
