package com.vob.reactive.webflux.repository;

import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import com.vob.reactive.webflux.repository.entity.Wishlist;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface WishlistRepository  extends ReactiveCosmosRepository<Wishlist, String> {
    Flux<Wishlist> findByIdIn(Iterable<String> ids);

}
