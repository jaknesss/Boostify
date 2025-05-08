package com.fra.groovelend.feedback;

import com.fra.groovelend.vinyl.Vinyl;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapper {
    public Feedback toFeedback(FeedbackRequest request) {
        return Feedback.builder()
                .vote(request.vote())
                .comment(request.comment())
                .vinyl(Vinyl.builder()
                        .id(request.vinylId())
                        .archived(false)
                        .shareable(false)
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
