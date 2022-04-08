package com.ekbana.bigdata.entity.publickey;

import com.ekbana.bigdata.entity.api.Api;
import com.ekbana.bigdata.entity.api.PersonalPackage;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
@Table(name = "key_client_api")
@Data
public class KeyClientApi {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "client_sid") private String clientSid;
    @Column(name = "user_email") private String userEmail;
    @Column(name = "api_id") private int apiId;
    @Column(name = "pkg_id") private int packageId;
    @Column(name = "public_key_alias") private String publicKeyAlias;

    @OneToOne(cascade = CascadeType.ALL)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "api_id",
            insertable = false,
            updatable = false,
            referencedColumnName = "id"

    )
    private Api api;

    @OneToOne(cascade = CascadeType.ALL)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "pkg_id",
                insertable = false,
                updatable = false,
                referencedColumnName = "id"
    )
    private PersonalPackage personalPackage;

    public String getUniqueId(){
        return this.publicKeyAlias+this.getApiId();
    }

    public int getParametersLength(){
        return this.api.getParametersLength();
    }
}
