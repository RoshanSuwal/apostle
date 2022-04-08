package com.ekbana.bigdata.entity.response;

import com.ekbana.bigdata.entity.api.Api;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="mock_response")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Mock implements Serializable {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "response_body")
    private String responseBody;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "response_code")
    private String responseCode;

    @Column(name = "response_headers")
    private String responseHeaders;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "api_id")
    private int apiId;

    @OneToOne(cascade = CascadeType.ALL)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "api_id",
            insertable = false,
            updatable = false,
            referencedColumnName = "id")
    private Api api;
}
