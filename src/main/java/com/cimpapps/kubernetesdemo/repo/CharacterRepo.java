package com.cimpapps.kubernetesdemo.repo;

import com.cimpapps.kubernetesdemo.models.SwCharacter;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CharacterRepo extends ElasticsearchRepository<SwCharacter, String> {

    List<SwCharacter> findFuzzyByName(String name);

}
