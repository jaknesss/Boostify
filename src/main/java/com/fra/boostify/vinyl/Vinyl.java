package com.fra.boostify.vinyl;

import com.fra.boostify.common.BaseEntity;
import com.fra.boostify.feedback.Feedback;
import com.fra.boostify.histiry.VinylTransactionHistory;
import com.fra.boostify.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Vinyl extends BaseEntity {

    private String albumTitle;
    private String artist;
    private String description;
    private String genre;
    private String vinylCover;
    private boolean archived;
    private boolean shareable;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "vinyl")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "vinyl")
    private List<VinylTransactionHistory> histories;



}
