package com.ekbana.bigdata.entity.api;

import com.ekbana.bigdata.entity.funnel.Funnel;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "personal_pkg")
@Data
public class PersonalPackage {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "alias")
    private String alias;
    @Column(name = "serial_id")
    private String serialId;
    @Column(name = "active")
    private boolean active;
    @Column(name = "funnel")
    private String fun;
    @Column(name = "created_at")
    private int createdAt;
    @Column(name = "update_at")
    private int updatedAt;
    @Column(name = "grace")
    private long graceCall;

    public Funnel getFunnel() {
        try {
            return Funnel.create(getFun());
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
