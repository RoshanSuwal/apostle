package com.ekbana.bigdata.repository.jpa;

import com.ekbana.bigdata.entity.publickey.PublicKeyAlias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicKeyAliasRepository extends JpaRepository<PublicKeyAlias,Integer>{

    @Query(value = "select c from PublicKeyAlias c join c.publicKey p where  p.key = :publicKey")
    PublicKeyAlias findByPublicKey(@Param("publicKey") String publicKey);

//    PublicKeyAlias findByKey(String s);
}
