package com.vob.reactive.webflux.service;

import com.azure.spring.data.cosmos.exception.CosmosAccessException;
import com.vob.reactive.webflux.mapper.DtoMapper;
import com.vob.reactive.webflux.repository.ProfileRepository;
import com.vob.reactive.webflux.repository.WishlistRepository;
import com.vob.reactive.webflux.repository.entity.Profile;
import com.vob.reactive.webflux.repository.entity.Wishlist;
import com.vob.reactive.webflux.service.model.WishlistDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component
@Slf4j
public class WishlistManagementService {
    private WishlistRepository wishlistRepository;
    private ProfileRepository profileRepository;

    @Autowired
    public WishlistManagementService(WishlistRepository wishlistRepository, ProfileRepository profileRepository) {
        this.wishlistRepository = wishlistRepository;
        this.profileRepository = profileRepository;
    }

    public Flux<WishlistDto> getProfileWishLists(String profileId) {
        return profileRepository.findById(profileId)
                .flatMapMany(c -> wishlistRepository.findByIdIn(c.getWishLists())
                        .map(w -> DtoMapper.convertToDto(w, false))
                );
    }

    public Mono<Wishlist> getWishList(String profileId, String wishListId) {
        return Mono.zip(profileRepository.findById(profileId), wishlistRepository.findById(wishListId))
                .flatMap(this::validateProfileToWishlistPermission);
    }

    public Mono<Wishlist> addWishToWishlist(String profileId, String wishListId, String wish) {
        return Mono.defer(() -> getWishList(profileId, wishListId)
                .flatMap(w -> {
                    w.getProducts().add(wish);
                    return wishlistRepository.save(w);
                }))
                .retryWhen(getRetrySpecs());
    }

    public Mono<Wishlist> createWishlist(String profileId, String name) {
        return wishlistRepository.save(Wishlist.builder()
                .id(UUID.randomUUID().toString())
                .name(name)
                .build())
                // Zip is to path wishlist to next map
                .flatMap(wishList -> Mono.zip(
                        Mono.just(wishList),
                        this.appendWishlistToProfile(profileId, wishList)
                ))
                .map(Tuple2::getT1);
    }

    private Mono<Profile> appendWishlistToProfile(String profileId, Wishlist wishList) {
        return Mono.defer(() ->
                profileRepository
                        .findById(profileId)
                        .defaultIfEmpty(Profile
                                .builder()
                                .id(profileId)
                                .build())
                        .flatMap(profile -> {
                            profile.getWishLists().add(wishList.getId());
                            return profileRepository.save(profile);
                        })
        ).retryWhen(getRetrySpecs());
    }

    public Mono<Wishlist> validateProfileToWishlistPermission(Tuple2<Profile, Wishlist> objects) {
        //Validate that user has access to this profile
        var wishlist = objects.getT2();
        if (objects.getT1().getWishLists().contains(wishlist.getId())) {
            return Mono.just(wishlist);
        }
        return Mono.empty();
    }

    public static RetryBackoffSpec getRetrySpecs(){
        return Retry.backoff(3, Duration.of(10, ChronoUnit.MILLIS))
                .doBeforeRetry(retrySignal -> {
                    log.warn("Lets retry :)");
                })
                .filter(throwable -> throwable instanceof CosmosAccessException
                && ((CosmosAccessException) throwable).getCosmosException().getStatusCode() == HttpStatus.PRECONDITION_FAILED.value());
    }
}