package com.fra.groovelend.vinyl;

import com.fra.groovelend.common.BaseEntity;
import com.fra.groovelend.feedback.Feedback;
import com.fra.groovelend.history.VinylTransactionHistory;
import com.fra.groovelend.user.User;
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
    private String artistName;
    private String description;
    private String genre;
    private String vinylCoverImage;
    private String barcode;
    private boolean archived;
    private boolean shareable;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "vinyl")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "vinyl")
    private List<VinylTransactionHistory> histories;

    @Transient
    public double getRate() {
        if(feedbacks == null || feedbacks.isEmpty()) return 0.0;
        var rate = this.feedbacks
                .stream()
                .mapToDouble(Feedback::getVote)
                .average()
                .orElse(0.0);
        return Math.round(rate * 10.0) / 10.0;
    }

}
