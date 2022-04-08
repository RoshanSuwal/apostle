package com.ekbana.bigdata.inbound.publicKey;

import com.ekbana.bigdata.entity.publickey.PublicKeyAlias;
import com.ekbana.bigdata.policy.Policy;
import com.ekbana.bigdata.repository.jpa.PublicKeyAliasRepository;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;

@com.ekbana.bigdata.annotation.Policy("public key policy")
public class PublicKeyValidationPolicy extends Policy {
    @Autowired
    PublicKeyAliasRepository publicKeyAliasRepository;

    @Override
    protected void pre(RequestWrapper requestWrapper) {
        PublicKeyAlias publicKeyAlias = publicKeyAliasRepository.findByPublicKey(requestWrapper.getUrlComponents().getHeaderKey("Api-Key"));
        if (publicKeyAlias!=null){
            if (publicKeyAlias.getPublicKey().isActive()) requestWrapper.setPublicKeyAlias(publicKeyAlias);
            else throw new PublicKeyException("Inactive key",requestWrapper);
        }else throw new PublicKeyException("invalid key",requestWrapper);
    }
}
