package com.fra.groovelend.feedback;


import com.fra.groovelend.common.BaseEntity;
import com.fra.groovelend.vinyl.Vinyl;
import jakarta.persistence.*;
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
public class Feedback extends BaseEntity {

    @Column
    private Double vote;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "vinyl_id")
    private Vinyl vinyl;


}


