package com.woong.projectmanager.adapter;

import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class WebHookConsumer {

    public String sendSlackMessage(String url, String message) {

        Map<String,String> parameters = new HashMap<>();

        parameters.put("username", "web");
        parameters.put("text", message);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try{
            ResponseEntity<String> response =  restTemplate.postForEntity(url, parameters, String.class);
            return response.getBody();
        }catch (Exception e){
            log.error(e);
            return "";
        }

        /*
        return WebClient.create(url)
                .post()
                .contentType(MediaType.APPLICATION_JSON.APPLICATION_JSON)
                .bodyValue(parameters)
                .exchange()
                .flatMap(res -> {
                    if (res.statusCode().value() != HttpStatus.OK.value()) {
                        return Mono.empty();
                    }
                    return res.bodyToMono(String.class);
                })
                .block();
         */
    }

}
