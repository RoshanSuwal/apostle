package com.ekbana.bigdata.entity.webhook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "api_webhook_ref")
@Getter @AllArgsConstructor @NoArgsConstructor
public class ApiWebhook {
    @Id @Column(name = "id")
    private int id;
    @Column(name = "status")
    private boolean status;
    @Column(name = "api_id")
    private int apiId;
    @Column(name = "webhook_id")
    private int webhookId;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "webhook_id",
            insertable = false,
            updatable = false,
            referencedColumnName = "id"
    )
    private Webhook webhook;
}
