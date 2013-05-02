package com.dlohaiti.dlokiosk.client;

import com.dlohaiti.dlokiosk.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import roboguice.inject.InjectResource;

public class RestClient {

    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);
    private final RestTemplate restTemplate;

    @InjectResource(R.string.dlo_server_url)
    String baseUrl;

    public RestClient() {
        restTemplate = getJsonRestTemplate();
    }

    private RestTemplate getJsonRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    public <T> T get(String uri, Class<T> responseType) {
        return restTemplate.getForObject(this.baseUrl + uri, responseType);
    }

    public boolean post(String uri, String jsonData) {
        String url = this.baseUrl + uri;
        try {
            restTemplate.postForObject(url, jsonData, String.class);
        } catch (HttpClientErrorException e) {
            logger.error("Error " + e.getStatusCode() + " while posting data to url " + url, e);
            return false;
        }

        return true;
    }
}