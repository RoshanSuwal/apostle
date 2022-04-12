package com.ekbana.bigdata.helpers;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Url components.
 */
public class UrlComponents {

    private final HttpServletRequest req;
    private HashMap<String, String> queryMap;
    private Map<String, String> pathVariableMap;

    public UrlComponents(HttpServletRequest request) {
        this.req = request;
        this.parseQueryParameters();
        this.pathVariableMap = new HashMap<>();
    }

    /**
     * parse Query Parameters
     * <p>
     * builds the Hashmap<String,String> of the base_url query params
     */
    private void parseQueryParameters() {
        this.queryMap = new HashMap<>();
        try {
            String[] split = this.req.getQueryString().split("&");
            for (String str : split) {
                int index = str.indexOf("=");
                this.queryMap.put(str.substring(0, index), str.substring(index + 1));
            }
        } catch (Exception e) {
            // queryString is null
        }
    }

    /**
     * Gets base url.
     *
     * @return the base url
     */
    public String getBaseUrl() {
        return this.getHeaderKey("Host");
    }

    /**
     * Gets method.
     *
     * @return the method
     */
    public String getMethod() {
        return this.req.getMethod();
    }

    /**
     * Gets ip.
     *
     * @return the ip
     */
    public String getIp() {
        return this.req.getLocalAddr();
    }

    /**
     * Gets path.
     *
     * @return the path
     */
    public String getEndPoint() {
        String uri = this.req.getRequestURI();
        if (uri.startsWith("/")) {
            uri = new StringBuilder(this.req.getRequestURI()).deleteCharAt(0).toString();
        }
        return uri;
    }

    /**
     * Gets parameters.
     *
     * @return the parameters
     */
    public Enumeration<String> getParameters() {
        Enumeration<String> pm = this.req.getParameterNames();
        return this.req.getParameterNames();
    }

    /**
     * Gets header key.
     *
     * @return the header key
     */
    public String getHeaderKey(String name) {
        return this.req.getHeader(name);
    }

    public String getParameterByKey(String key) {
        return this.queryMap.get(key);
    }

    public String getRequestBody() {
        char[] buffer = new char[req.getContentLength()];
        try {
            req.getReader().read(buffer);
            return String.copyValueOf(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return RequestBody  of the request
     * @method getRequest
     */
    public RequestBody getRequest() throws IOException, ServletException {
        org.springframework.http.MediaType mediaType = org.springframework.http.MediaType.valueOf(getContentType());

        if (mediaType.getType().toLowerCase().equals("multipart")) {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            Collection<Part> parts = req.getParts();

            for (Part part : parts) {
                try {
                    String value = new String(req.getPart(part.getName()).getInputStream().readAllBytes());
                    String name = part.getName();
                    if (part.getContentType() == null)
                        builder.addFormDataPart(name, value);
                    else
                        builder.addFormDataPart(part.getName(), part.getSubmittedFileName(), RequestBody.create(MediaType.get(part.getContentType()), part.getInputStream().readAllBytes()));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ServletException e) {
                    e.printStackTrace();
                }
            }

            builder.setType(MediaType.get(getContentType().split(";")[0]));
            return builder.build();
        } else {
            CharBuffer buffer = CharBuffer.allocate(req.getContentLength());
            req.getReader().read(buffer);
            String body1 = String.copyValueOf(buffer.array());
            RequestBody requestBody = RequestBody.create(body1, MediaType.get(getContentType()));
            return requestBody;
        }
    }

    public String getContentType() {
        return this.req.getContentType();
    }

    public HashMap<String, String> getHeaders() {
        HashMap<String, String> replacedHeader = new HashMap<>();
        Enumeration<String> headerNames = this.req.getHeaderNames();
//        this.req.getHeaderNames().asIterator();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String key = headerNames.nextElement();
//                System.out.println(key);
                replacedHeader.put(key, this.req.getHeader(key));
            }
        }
        String[] remove = new String[]{"user-agent", "accept", "postman-token", "host", "accept-encoding", "connection", "content-type", "content-length"};

        for (String r : remove) {
            replacedHeader.remove(r);
        }
        return replacedHeader;
    }

    public void setPathVariableMap(Map<String, String> pathVariableMap) {
        this.pathVariableMap = pathVariableMap;
    }

    public String getPathVariableByKey(String str) {
        return this.pathVariableMap.get(str);
    }
}
