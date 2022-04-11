package com.ekbana.bigdata.inbound.email;

import com.ekbana.bigdata.entity.emails.Emails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<Emails,Integer> {

}
