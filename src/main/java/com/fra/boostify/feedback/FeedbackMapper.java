package com.fra.boostify.feedback;

import com.fra.boostify.vinyl.Vinyl;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapper {
    public Feedback toFeedback(FeedbackRequest request) {
        return Feedback.builder()
                .vote(request.note())
                .comment(request.comment())
                .vinyl(Vinyl.builder()
                        .id(request.vinylId())
                        .archived(false)
                        .shareable(true)
                        .build()
                )
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback, Integer userId) {
        return FeedbackResponse.builder()
                .vote(feedback.getVote())
                .comment(feedback.getComment())
                .ownFeedback(Objects.equals(feedback.getCratedBy(), userId))
                .build();
    }
}
