package com.ekbana.bigdata.inbound.api;

import com.ekbana.bigdata.configuration.ApplicationConfiguration;
import com.ekbana.bigdata.entity.publickey.KeyClientApi;
import com.ekbana.bigdata.policy.Policy;
import com.ekbana.bigdata.repository.jpa.KeyClientApiRepository;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;

import java.util.List;

@com.ekbana.bigdata.annotation.Policy(value = "api validation policy")
public class ApiValidationPolicy extends Policy {

    private final KeyClientApiRepository keyClientApiRepository;
    private final ApplicationConfiguration applicationConfiguration;

    private final AntPathMatcher antPathMatcher;

    @Autowired
    public ApiValidationPolicy(KeyClientApiRepository keyClientApiRepository, ApplicationConfiguration applicationConfiguration) {
        this.keyClientApiRepository = keyClientApiRepository;
        this.applicationConfiguration = applicationConfiguration;
        this.antPathMatcher=new AntPathMatcher();
    }

    @Override
    protected void pre(RequestWrapper requestWrapper) {
        List<KeyClientApi> keyClientApis = keyClientApiRepository.findKeyClientApiByPublicKeyAliasMethodAndBaseUrl(
                requestWrapper.getPublicKeyAlias().getAlias(),
                requestWrapper.getApi().getMethods(),
                requestWrapper.getApi().getBase_url()
        );

        if (keyClientApis==null || keyClientApis.size()==0){
            throw new ApiException(applicationConfiguration.getUNREGISTERED_API_MSG(),requestWrapper);
        }else {
            KeyClientApi clientApi = keyClientApis.stream()
                    .filter(keyClientApi -> antPathMatcher.match(keyClientApi.getApi().getParameters().replace("{}","{_pv}"), requestWrapper.getApi().getParameters()))
                    .findFirst()
                    .orElseThrow(() -> new ApiException(applicationConfiguration.getUNREGISTERED_API_MSG(), requestWrapper));
            requestWrapper.setKeyClientApi(clientApi);
            requestWrapper.getUrlComponents().setPathVariableMap(
                    antPathMatcher.extractUriTemplateVariables(
                            requestWrapper.getKeyClientApi().getApi().getParameters().replace("{}","{_pv}"),
                            requestWrapper.getApi().getParameters()));
            if (!clientApi.getApi().getActive()) throw new ApiException(applicationConfiguration.getINACTIVE_API_MSG(),requestWrapper);
        }
    }
}
