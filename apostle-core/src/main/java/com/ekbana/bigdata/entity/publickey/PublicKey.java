package com.ekbana.bigdata.entity.publickey;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "public_key")
@Setter @NoArgsConstructor @AllArgsConstructor
public class PublicKey implements Serializable {

    private int id;
    private String serialId;
    private boolean active;

    private String clientSid;

    private int createdAt;
    private long expiresAt;
    private String key;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    @Column(name = "serial_id")
    public String getSerialId() {
        return serialId;
    }

    public boolean isActive() {
        return active;
    }

    @Column(name="client_sid")
    public String getClientSid() {
        return clientSid;
    }

    @Column(name = "created_at")
    public int getCreatedAt() {
        return createdAt;
    }

    @Column(name = "expires_at")
    public long getExpiresAt(){return expiresAt;}

    @Column(name="key")
    public String getKey() {
        return key;
    }

}
