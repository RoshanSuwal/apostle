package com.ekbana.bigdata.entity.emails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "emails")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Emails {
    @Id @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "to")
    private String to;
    @Column(name = "subject")
    private String subject;
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
