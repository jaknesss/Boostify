package com.fra.boostify.vinyl;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VinylResponse {
    private Integer id;
    private String albumTitle;
    private String artistName;
    private String barcode;
    private String description;
    private String owner;
    private byte[] vinylCoverImage;
    private double rate;
    private boolean archived;
    private boolean shareable;
}
