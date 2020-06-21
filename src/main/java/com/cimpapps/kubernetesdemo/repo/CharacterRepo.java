package com.cimpapps.kubernetesdemo.repo;

import com.cimpapps.kubernetesdemo.models.SwCharacter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Repository
public class CharacterRepo {

    private static final String USERS_INDEX = "characters";
    private RestHighLevelClient restHighLevelClient;
    private ObjectMapper objectMapper;


    public CharacterRepo(RestHighLevelClient restHighLevelClient, ObjectMapper objectMapper) {
        this.restHighLevelClient = restHighLevelClient;
        this.objectMapper = objectMapper;
    }



    public void save(String id, SwCharacter character) {
        IndexRequest indexRequest = new IndexRequest(USERS_INDEX);
        indexRequest.id(id);
        try {
            indexRequest.source(objectMapper.writeValueAsString(character), XContentType.JSON);
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GetResponse list() {

        try {
           return restHighLevelClient.get(new GetRequest(USERS_INDEX), RequestOptions.DEFAULT);
        } catch (IOException e) {
            return null;
        }
    }


    public List<SwCharacter> search(String term) {
        SearchRequest searchRequest = buildSearchRequest(term);
        try {
            final SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            return Arrays.stream(search.getHits().getHits())
                    .map(SearchHit::getSourceAsString)
                    .map(this::deserializeUserElastic)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private SearchRequest buildSearchRequest(String term) {
        SearchRequest searchRequest = new SearchRequest(USERS_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);
        final BoolQueryBuilder query = boolQuery();
        query.should(matchQuery("name", term).fuzziness(Fuzziness.AUTO))
                .minimumShouldMatch(1);

        searchSourceBuilder.query(query);
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }

    private SwCharacter deserializeUserElastic(String source) {
        try {
            return objectMapper.readValue(source, SwCharacter.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
