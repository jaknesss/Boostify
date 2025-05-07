package com.fra.boostify.vinyl;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowedVinylsResponse {

    private Integer id;
    private String albumTitle;
    private String artistName;
    private String barcode;
    private double rate;
    private boolean returned;
    private boolean returnApproved  ;
}
