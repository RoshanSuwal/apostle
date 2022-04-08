package com.ekbana.bigdata.entity.api;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="request_adons")
@Setter @AllArgsConstructor @NoArgsConstructor
public class RequestAddons implements Serializable {

    private int id;
    private String queryParam;
    private String requestHeaders;
    private int apiId;
    private Api api;

    @Id
    @Column(name="id")
    public Integer getId() {
        return id;
    }

    @Column(name = "api_id")
    public int getApiId() {
        return apiId;
    }

    @Column(name = "query_param")
    public String getQueryParam() {
        return queryParam;
    }
    @Column(name = "request_headers")
    public String getRequestHeaders() {
        return requestHeaders;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "api_id",
            insertable = false,
            updatable = false,
            referencedColumnName = "id")
    public Api getApi() {
        return api;
    }
}
