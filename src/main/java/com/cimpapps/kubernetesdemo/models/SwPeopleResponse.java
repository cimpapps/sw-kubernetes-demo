package com.cimpapps.kubernetesdemo.models;

import lombok.*;

import java.util.List;

@Data
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SwPeopleResponse {
    private int count;
    private String next;
    private String previous;
    private List<SwCharacter> results;
}
