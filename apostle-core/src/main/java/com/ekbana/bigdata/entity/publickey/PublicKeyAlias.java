package com.ekbana.bigdata.entity.publickey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "public_key_alias")
public class PublicKeyAlias implements Serializable {
    @Id
    @Column(name = "id", nullable = false) private int id;
    @Column(name="alias") private String alias;
    @Column(name="pk_serial_id") private String key;

    @OneToOne(cascade = CascadeType.ALL)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "pk_serial_id",
            insertable = false,
            updatable = false,
            referencedColumnName = "serial_id",
            foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT)
    )
    private PublicKey publicKey;

}
