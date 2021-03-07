package com.vob.reactive.webflux.repository.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.util.ArrayList;

@Data
@Container(containerName = "wishlist")
@Builder
public class Wishlist {
    @Id
    private String id;
    private String name;
    @Version
    private String _tag;
    @Builder.Default
    private ArrayList<String> products = new ArrayList<>();
}
