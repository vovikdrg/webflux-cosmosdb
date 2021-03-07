package com.vob.reactive.webflux.controller;

import com.vob.reactive.webflux.mapper.DtoMapper;
import com.vob.reactive.webflux.service.WishlistManagementService;
import com.vob.reactive.webflux.service.model.WishlistDto;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("profile/{profileId}")
public class ProfileController {
    private WishlistManagementService service;

    public ProfileController(WishlistManagementService service) {
        this.service = service;
    }

    @GetMapping
    public Flux<WishlistDto> profile(@PathVariable String profileId) {
        return service.getProfileWishLists(profileId);
    }

    @PostMapping
    public Mono<WishlistDto> addProfile(@PathVariable String profileId, @RequestParam String name) {
        return service.createWishlist(profileId, name)
                .map(t -> DtoMapper.convertToDto(t, false));
    }
}