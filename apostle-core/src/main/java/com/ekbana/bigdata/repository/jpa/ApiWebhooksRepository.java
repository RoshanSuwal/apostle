package com.ekbana.bigdata.repository.jpa;

import com.ekbana.bigdata.entity.webhook.ApiWebhook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiWebhooksRepository extends JpaRepository<ApiWebhook,Integer> {

    @Query("select A from ApiWebhook A where A.apiId =:apiId AND A.status =true ")
    List<ApiWebhook> findByApiId(@Param("apiId") int apiId);

    @Query("select A from ApiWebhook A where A.apiId =:apiId AND A.status =true ")
    ApiWebhook findByApiIds(@Param("apiId") int apiId);
}
