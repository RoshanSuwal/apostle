package com.ekbana.bigdata.entity.api;

import com.ekbana.bigdata.entity.response.Mock;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.regex.Pattern;

/**
 * The type Api.
 */
@Entity
@Table(name = "api")
@Data
@ToString
public class Api {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "base_url")
    private String base_url;

    @Column(name = "redirect_to")
    private String redirectTo;

    @Column(name = "serial_id")
    private String serialID;

    @Column(name = "parameters")
    private String parameters;

    @Column(name = "is_cacheable")
    private boolean isCacheable;

    @Column(name = "cache_period")
    private Long cachePeriod;

    @Column(name = "methods")
    private String methods;

    @Column(name = "private_key")
    private String private_key;

    @Column(name = "cache_reset_key")
    private String cache_reset_key;

    @Column(name = "protocol")
    private String protocol;

    @Column(name = "creator_email")
    private String creator_email;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "cache_headers")
    private String header_params_for_caching;

    @Column(name = "cache_queries")
    private String queries_params_for_caching;

    @Column(name = "cache_pathvariables")
    private String path_variables_for_caching;

    @Column(name="custom_url")
    private String custom_url;

    @OneToOne(mappedBy = "api")
    private RequestAddons  requestAddons;

    @OneToOne(mappedBy = "api")
    private Mock mock;


    /**
     * Instantiates a new Api.
     */
    public Api() {
    }

    /**
     * Instantiates a new Api.
     *
     * @param base_url   the base url
     * @param parameters the parameters
     * @param methods    the methods
     */
    public Api(String base_url, String parameters, String methods) {
        this.base_url = base_url;
        this.parameters = parameters;
        this.methods = methods;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets base url.
     *
     * @return the base url
     */
    public String getBase_url() {
        return base_url;
    }

    /**
     * Sets base url.
     *
     * @param base_url the base url
     */
    public void setBase_url(String base_url) {
        this.base_url = base_url;
    }

    /**
     * Gets parameters.
     *
     * @return the parameters
     */
    public String getParameters() {
        return parameters;
    }

    /**
     * Sets parameters.
     *
     * @param parameters the parameters
     */
    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    /**
     * Gets methods.
     *
     * @return the methods
     */
    public String getMethods() {
        return methods;
    }

    /**
     * Sets methods.
     *
     * @param methods the methods
     */
    public void setMethods(String methods) {
        this.methods = methods;
    }

    /**
     * Gets private key.
     *
     * @return the private key
     */
    public String getPrivate_key() {
        return private_key;
    }

    /**
     * Sets private key.
     *
     * @param private_key the private key
     */
    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    public String getReset_key() {
        return cache_reset_key;
    }

    /**
     * Gets creator email.
     *
     * @return the creator email
     */
    public String getCreator_email() {
        return creator_email;
    }

    /**
     * Sets creator email.
     *
     * @param creator_email the creator email
     */
    public void setCreator_email(String creator_email) {
        this.creator_email = creator_email;
    }

    /**
     * Gets active.
     *
     * @return the active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Sets active.
     *
     * @param active the active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * get Header Params for caching
     *
     * @return String of comma(,) separated header params name to be used for caching
     */
    public String getHeader_params_for_caching() {
        return header_params_for_caching;
    }

    /**
     * Sets Header Params for caching
     *
     * @param header_params_for_caching String of comma(,) separated list of the header params for caching
     */
    public void setHeader_params_for_caching(String header_params_for_caching) {
        this.header_params_for_caching = header_params_for_caching;
    }

    /**
     * get Queries Params for caching
     *
     * @return String of comma(,) separated url_query params name to be used for caching
     */
    public String getQueries_params_for_caching() {
        return queries_params_for_caching;
    }

    /**
     * Sets url queries Params for caching
     *
     * @param queries_params_for_caching String of comma(,) separated list of the url query params for caching
     */
    public void setQueries_params_for_caching(String queries_params_for_caching) {
        this.queries_params_for_caching = queries_params_for_caching;
    }

    public String getPath_variables_for_caching() {
        return path_variables_for_caching;
    }

    public void setPath_variables_for_caching(String path_variables_for_caching) {
        this.path_variables_for_caching = path_variables_for_caching;
    }

    public RequestAddons getRequestAddons() {
        return requestAddons;
    }

    public void setRequestAddons(RequestAddons requestAddons) {
        this.requestAddons = requestAddons;
    }

    public Mock getMock() {
        return mock;
    }

    public void setMock(Mock mock) {
        this.mock = mock;
    }

    public boolean equal(Api api) {
        return Pattern.matches(this.getParameters().replace("{}","([^/])+")+"([/])+$",api.getParameters()+"/");

//        String pattern = this.getParameters();
//        pattern = (pattern.charAt(pattern.length() - 1) == '/' ?
//                "^" + pattern.substring(0, pattern.length() - 1) + "(/?|/.+)" :
//                "^" + pattern + "(/?|/.+)");
        //.replace("{number}","([0-9]+)")
        //.replace("{string}","([a-zA-Z0-9]+)");
        //System.out.println(pattern+":"+api.getParameters());

//        return this.getBase_url().equals(api.getBase_url()) &&
//                this.getMethods().equals(api.getMethods()) &&
//                Pattern.matches(pattern, api.getParameters());

    }

    /**
     * get url parameters length
     *
     * @return int length of the url path length
     */
    public int getParametersLength() {
        return this.parameters.length();
    }

//    public String getKeyForCaching(UrlComponents urlComponents) {
//        StringBuilder cachingKey = new StringBuilder();
//        if (this.getHeader_params_for_caching() != null) {
//            for (String str : this.getHeader_params_for_caching().split(",")) {
//                cachingKey.append(str)
//                        .append("=")
//                        .append(urlComponents.getHeaderKey(str)).append(",");
//            }
//        }
//
//        if (this.getQueries_params_for_caching() != null) {
//            for (String str : this.getQueries_params_for_caching().split(",")) {
//                cachingKey.append(str)
//                        .append("=")
//                        .append(urlComponents.getParameterByKey(str)).append(",");
//            }
//        }
//
//        //TODO : perform some hashing algorithm to cachingKey
//        return cachingKey.toString();
//    }

    public boolean compare(Api api) {
        if (this.getParametersLength() < api.getParametersLength()) return false;
        else {
            /* requested api greater than the api ie api containing slugs
             * or '/' at the end
             * checks by adding '/' on the end of database api.parameter
             * checks string match from the start
             * eg: requested api: select/abc/1234 database_api: select
             *     check for select/ from start in requested api
             *
             * Also check for slug
             * **/
            String slug = "(.?|.+)";
            String param = this.getParameters();
            String req_param = param;
            String req_slug = "";

            if (this.getParametersLength() == api.getParametersLength()) {
                req_param = param + "/";
                req_slug = "";
            } else if (this.getParametersLength() > api.getParametersLength() + 1) {
                req_param = param.substring(0, api.getParametersLength() + 1);
                req_slug = param.substring(api.getParametersLength() + 2);
            }

            return (req_param.equals(api.getParameters() + "/") && Pattern.matches(slug, req_slug));
        }
    }




}
