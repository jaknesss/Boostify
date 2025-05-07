package com.fra.boostify.feedback;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {
    private Double vote;
    private String comment;
    private boolean ownFeedback;
}
