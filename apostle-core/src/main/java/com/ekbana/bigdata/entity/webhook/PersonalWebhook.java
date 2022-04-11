package com.ekbana.bigdata.entity.webhook;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "personal_webhooks")
@Data
public class PersonalWebhook {
    @Id @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "funnel")
    private String funnel;
    @Column(name = "status")
    private boolean status;
    @Column(name = "api_id")
    private int apiId;
}
