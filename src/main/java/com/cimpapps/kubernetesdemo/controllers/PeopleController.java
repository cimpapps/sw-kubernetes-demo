package com.cimpapps.kubernetesdemo.controllers;

import com.cimpapps.kubernetesdemo.api.StarWarsDataProvider;
import com.cimpapps.kubernetesdemo.models.SwCharacter;
import com.cimpapps.kubernetesdemo.repo.CharacterRepo;
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

@RestController
@CrossOrigin
public class PeopleController {

    private CharacterRepo characterRepo;
    private StarWarsDataProvider swDataProvider;

    public PeopleController(CharacterRepo characterRepo, StarWarsDataProvider swDataProvider) {
        this.characterRepo = characterRepo;
        this.swDataProvider = swDataProvider;
    }

    @GetMapping("/people")
    public Iterable<SwCharacter> getPeople() {
        return characterRepo.findAll();
    }

    @GetMapping("/people/search")
    public Page<SwCharacter> searchPeople(@RequestParam String name) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("name", name)
                        .operator(Operator.AND)
                        .fuzziness(Fuzziness.AUTO)
                        .prefixLength(3))
                .build();
        Page<SwCharacter> search = characterRepo.search(searchQuery);
        return search;
    }

    @GetMapping("/data")
    public void provideData() {
        swDataProvider.provideData();
    }


}
