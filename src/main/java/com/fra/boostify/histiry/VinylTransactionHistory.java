package com.fra.boostify.histiry;

import com.fra.boostify.common.BaseEntity;
import com.fra.boostify.user.User;
import com.fra.boostify.vinyl.Vinyl;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class VinylTransactionHistory extends BaseEntity {

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;
        @ManyToOne
        @JoinColumn(name = "vinyl_id")
        private Vinyl vinyl;

        private boolean returned;
        private boolean returnedApproved;
}


