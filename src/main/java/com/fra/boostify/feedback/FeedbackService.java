package com.fra.boostify.feedback;

import com.fra.boostify.common.PageResponse;
import com.fra.boostify.exception.OperationNotPermittedException;
import com.fra.boostify.user.User;
import com.fra.boostify.vinyl.Vinyl;
import com.fra.boostify.vinyl.VinylRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final VinylRepository vinylRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRepository feedbackRepository;

    public Integer save(FeedbackRequest request, Authentication connectedUser) {
        Vinyl vinyl = vinylRepository.findById(request.vinylId())
                .orElseThrow(() -> new EntityNotFoundException("No vinyl found with ID: " + request.vinylId()));
        if(vinyl.isArchived() || !vinyl.isShareable())
            throw new OperationNotPermittedException("You cannot leave feedback on this vinyl, it's either archived or not shareable");
        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(vinyl.getOwner().getId(), user.getId()))
            throw new OperationNotPermittedException("You cannot leave a feedback on your own vinyl");
        Feedback feedback = feedbackMapper.toFeedback(request);

        return feedbackRepository.save(feedback).getId();
    }

    public PageResponse<FeedbackResponse> findAllFeedbacksByVinyl(Integer vinylId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        User user = ((User) connectedUser.getPrincipal());
        Page<Feedback> feedbacks = feedbackRepository.findAllByVinylId(vinylId, pageable );
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(f -> feedbackMapper.toFeedbackResponse(f, user.getId()))
                .toList();
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
