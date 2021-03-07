package com.vob.reactive.webflux.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WishlistDto {
    private String name;
    private String id;
    private ArrayList<String> products;
}
