package com.ekbana.bigdata.entity.webhook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "webhooks")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Webhook {
    @Id @Column(name = "id")
    private int id;
    @Column(name = "type")
    private String type;
    @Column(name = "name")
    private String name;
    @Column(name = "funnel")
    private String funnel;
}
