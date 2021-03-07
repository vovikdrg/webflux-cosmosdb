package com.vob.reactive.webflux.repository;

import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import com.vob.reactive.webflux.repository.entity.Profile;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends ReactiveCosmosRepository<Profile, String> {

}
