package com.vob.reactive.webflux.mapper;

import com.vob.reactive.webflux.repository.entity.Wishlist;
import com.vob.reactive.webflux.service.model.WishlistDto;

import java.util.List;
import java.util.stream.Collectors;

public class DtoMapper {
    public static List<WishlistDto> convertWishListToDto(List<Wishlist> wishlists, boolean includeProducts) {
        return wishlists.stream()
                .map(w->DtoMapper.convertToDto(w, includeProducts))
                .collect(Collectors.toList());
    }

    public static WishlistDto convertToDto(Wishlist wishlist, boolean includeProducts) {
        var wishListDto = WishlistDto.builder()
                .id(wishlist.getId())
                .name(wishlist.getName());
        if (includeProducts) wishListDto.products(wishlist.getProducts());

        return wishListDto.build();
    }
}
