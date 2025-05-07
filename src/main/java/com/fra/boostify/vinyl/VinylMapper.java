package com.fra.boostify.vinyl;

import com.fra.boostify.history.VinylTransactionHistory;
import org.springframework.stereotype.Service;

@Service
public class VinylMapper {
    public Vinyl toVinyl(VinylRequest request) {
        return Vinyl.builder()
                .id(request.id())
                .albumTitle(request.albumTitle())
                .artistName(request.artistName())
                .barcode(request.barcode())
                .description(request.description())
                .archived(false)
                .shareable(request.shareable())
                .build();
    }

    public VinylResponse toVinylResponse(Vinyl vinyl) {
        return VinylResponse.builder()
                .id(vinyl.getId())
                .albumTitle(vinyl.getAlbumTitle())
                .artistName(vinyl.getArtistName())
                .barcode(vinyl.getBarcode())
                .description(vinyl.getDescription())
                .rate(vinyl.getRate())
                .archived(vinyl.isArchived())
                .shareable(vinyl.isShareable())
                .owner(vinyl.getOwner().fullName())
                //.vinylCoverImage()
                .build();
    }

    public BorrowedVinylsResponse toBorrowedVinylResponse(VinylTransactionHistory history) {
        return BorrowedVinylsResponse.builder()
                .id(history.getVinyl().getId())
                .albumTitle(history.getVinyl().getAlbumTitle())
                .artistName(history.getVinyl().getArtistName())
                .barcode(history.getVinyl().getBarcode())
                .rate(history.getVinyl().getRate())
                .returned(history.isReturned())
                .returnApproved(history.isReturnedApproved())
                .build();
    }
}
