package com.cimpapps.kubernetesdemo.api;

import com.cimpapps.kubernetesdemo.models.SwPeopleResponse;
import com.cimpapps.kubernetesdemo.repo.CharacterRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StarWarsDataProvider {

    @Value("${connector.api.sw.host}")
    public  String swHost;
    private RestTemplate restTemplate;
    private CharacterRepo characterRepo;

    public StarWarsDataProvider(RestTemplate restTemplate, CharacterRepo characterRepo) {
        this.restTemplate = restTemplate;
        this.characterRepo = characterRepo;
    }

    public void provideData() {
        ResponseEntity<SwPeopleResponse> responseEntity = this.restTemplate.getForEntity(swHost, SwPeopleResponse.class);
        SwPeopleResponse body = responseEntity.getBody();
        body.getResults().forEach(ch -> characterRepo.save(ch.getName(), ch));
    }
}
