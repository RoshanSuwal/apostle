package com.ekbana.bigdata.repository.jpa;

import com.ekbana.bigdata.entity.publickey.KeyClientApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeyClientApiRepository extends JpaRepository<KeyClientApi,Integer> {

//    List<KeyClientApi> findByPublicKeyAlias(String publicKeyAlias);

//    @Query("select A from KeyClientApi A where A.publicKeyAlias =:publicKeyAlias AND ( A.api.base_url =:baseUrl OR A.api.custom_url =:baseUrl ) AND A.api.methods=:method AND concat(:parameter,'/') like concat(A.api.parameters,'/%') order by length(A.api.parameters) DESC")
//    List<KeyClientApi> findByPublicKeyAliasParamMethodAndBaseUrl(String publicKeyAlias,String parameter,String method,String baseUrl);

    @Query("select A from KeyClientApi A where A.publicKeyAlias =:publicKeyAlias AND ( A.api.base_url =:baseUrl OR A.api.custom_url =:baseUrl ) AND A.api.methods=:method order by length(A.api.parameters) DESC")
    List<KeyClientApi> findKeyClientApiByPublicKeyAliasMethodAndBaseUrl(@Param("publicKeyAlias") String publicKeyAlias,
                                                                        @Param("method") String method,
                                                                        @Param("baseUrl") String baseUrl);
}
