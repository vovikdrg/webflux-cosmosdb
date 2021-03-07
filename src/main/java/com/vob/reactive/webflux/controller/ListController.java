package com.vob.reactive.webflux.controller;

import com.vob.reactive.webflux.mapper.DtoMapper;
import com.vob.reactive.webflux.service.WishlistManagementService;
import com.vob.reactive.webflux.service.model.WishlistDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("profile/{profileId}/list/{listId}")
public class ListController {
    private WishlistManagementService service;

    public ListController(WishlistManagementService service) {
        this.service = service;
    }

    @GetMapping()
    public Mono<WishlistDto> getList(@PathVariable String profileId, @PathVariable String listId) {
        return service.getWishList(profileId, listId)
                .map(w -> DtoMapper.convertToDto(w, true));
    }

    @PostMapping()
    public Mono<ResponseEntity<WishlistDto>> saveListItem(@PathVariable String profileId, @PathVariable String listId,
                                          @RequestParam String wish) {
        return service.addWishToWishlist(profileId, listId, wish)
                .map(w -> ResponseEntity.ok().body(DtoMapper.convertToDto(w, true)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}