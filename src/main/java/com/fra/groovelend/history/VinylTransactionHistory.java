package com.fra.groovelend.history;

import com.fra.groovelend.common.BaseEntity;
import com.fra.groovelend.user.User;
import com.fra.groovelend.vinyl.Vinyl;
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


