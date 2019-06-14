package com.springer.hack.exambuddy.external;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public abstract class BaseRestService {

    private String baseUrl;

    public BaseRestService(BaseRestServiceProperties properties) {
        var url = new StringBuilder();

        if (properties.getSsl()) {
            url.append("https://");
        } else {
            url.append("http://");
        }

        url.append(properties.getHost());

        if (properties.getPort() != null && properties.getPort() != 80 && properties.getPort() != 443) {
            url.append(":").append(properties.getPort());
        }

        url.append(properties.getPath());

        baseUrl = url.toString();
    }

    protected String buildUrl(String pathTemplate, Map<String, Object> queryParams) {
        var builder = UriComponentsBuilder.fromUriString(baseUrl).path(pathTemplate);
        for (var param : queryParams.entrySet()) {
            builder.queryParam(param.getKey(), param.getValue());
        }

        return builder.build().toUriString();
    }
}
