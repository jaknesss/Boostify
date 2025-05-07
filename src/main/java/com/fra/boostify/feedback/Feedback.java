package com.fra.boostify.feedback;


import com.fra.boostify.common.BaseEntity;
import com.fra.boostify.vinyl.Vinyl;
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


