package com.fra.boostify.vinyl;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record VinylRequest(
        Integer id,
        @NotNull(message = "100")
        @NotEmpty(message = "100")
        String albumTitle,
        @NotNull(message = "101")
        @NotEmpty(message = "101")
        String artistName,
        @NotNull(message = "102")
        @NotEmpty(message = "102")
        String barcode,
        @NotNull(message = "103")
        @NotEmpty(message = "103")
        String description,
        boolean shareable

) {
}
