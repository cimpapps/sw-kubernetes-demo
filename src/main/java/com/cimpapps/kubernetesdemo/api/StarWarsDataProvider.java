package com.cimpapps.kubernetesdemo.api;

import com.cimpapps.kubernetesdemo.models.SwPeopleResponse;
import com.cimpapps.kubernetesdemo.repo.CharacterRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service
public class StarWarsDataProvider {

    public static final String SW_API = "https://swapi.co/api/people";
    private RestTemplate restTemplate;
    private CharacterRepo characterRepo;

    public StarWarsDataProvider(RestTemplate restTemplate, CharacterRepo characterRepo) {
        this.restTemplate = restTemplate;
        this.characterRepo = characterRepo;
    }

    public void provideData() {
        ResponseEntity<SwPeopleResponse> responseEntity = this.restTemplate.getForEntity(SW_API, SwPeopleResponse.class);
        SwPeopleResponse body = responseEntity.getBody();
        characterRepo.saveAll(body.getResults());
    }
}
