package com.cimpapps.kubernetesdemo.controllers;

import com.cimpapps.kubernetesdemo.api.StarWarsDataProvider;
import com.cimpapps.kubernetesdemo.models.SwCharacter;
import com.cimpapps.kubernetesdemo.repo.CharacterRepo;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class PeopleController {

    private CharacterRepo characterRepo;
    private StarWarsDataProvider swDataProvider;

    public PeopleController(CharacterRepo characterRepo, StarWarsDataProvider swDataProvider) {
        this.characterRepo = characterRepo;
        this.swDataProvider = swDataProvider;
    }


    @GetMapping("/people/search")
    public List<SwCharacter> searchPeople(@RequestParam String name) {

        List<SwCharacter> search = characterRepo.search(name);

        return search;
    }

    @GetMapping("/data")
    public void provideData() {
        swDataProvider.provideData();
    }


}
