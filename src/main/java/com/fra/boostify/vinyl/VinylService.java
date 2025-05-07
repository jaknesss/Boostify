package com.fra.boostify.vinyl;

import com.fra.boostify.common.PageResponse;
import com.fra.boostify.exception.OperationNotPermittedException;
import com.fra.boostify.file.FileStorageService;
import com.fra.boostify.history.VinylTransactionHistory;
import com.fra.boostify.history.VinylTransactionHistoryRepository;
import com.fra.boostify.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.fra.boostify.vinyl.VinylSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
public class VinylService {

    private final VinylRepository vinylRepository;
    private final VinylTransactionHistoryRepository transactionHistoryRepository;
    private final VinylMapper vinylMapper;
    private final FileStorageService fileStorageService;

    public Integer save(VinylRequest request, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Vinyl vinyl = vinylMapper.toVinyl(request);
        vinyl.setOwner(user);
        return vinylRepository.save(vinyl).getId();
    }

    public VinylResponse findById(Integer vinylId) {
        return vinylRepository
                .findById(vinylId)
                .map(vinylMapper::toVinylResponse)
                .orElseThrow(() -> new EntityNotFoundException("No vinyl found with ID: " + vinylId));
    }

    public PageResponse<VinylResponse> findAllVinyls(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Vinyl> vinyls = vinylRepository.findAllDisplayableVinyls(pageable, user.getId());
        List<VinylResponse> vinylResponses = vinyls.stream().map(vinylMapper::toVinylResponse).toList();
        return new PageResponse<>(
                vinylResponses,
                vinyls.getNumber(),
                vinyls.getSize(),
                vinyls.getTotalElements(),
                vinyls.getTotalPages(),
                vinyls.isFirst(),
                vinyls.isLast()
        );
    }

    public PageResponse<VinylResponse> findAllVinylsByOwner(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Vinyl> vinyls = vinylRepository.findAll(withOwnerId(user.getId()), pageable);
        List<VinylResponse> vinylResponses = vinyls.stream().map(vinylMapper::toVinylResponse).toList();
        return new PageResponse<>(
                vinylResponses,
                vinyls.getNumber(),
                vinyls.getSize(),
                vinyls.getTotalElements(),
                vinyls.getTotalPages(),
                vinyls.isFirst(),
                vinyls.isLast()
        );
    }

    public PageResponse<BorrowedVinylsResponse> findAllBorrowedVinyls(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<VinylTransactionHistory> allBorrowedVinyls =
                transactionHistoryRepository.findAllBorrowedVinyls(pageable, user.getId());
        List<BorrowedVinylsResponse> vinylResponse = allBorrowedVinyls.stream()
                .map(vinylMapper::toBorrowedVinylResponse)
                .toList();
        return new PageResponse<>(
                vinylResponse,
                allBorrowedVinyls.getNumber(),
                allBorrowedVinyls.getSize(),
                allBorrowedVinyls.getTotalElements(),
                allBorrowedVinyls.getTotalPages(),
                allBorrowedVinyls.isFirst(),
                allBorrowedVinyls.isLast()
        );
    }

    public PageResponse<BorrowedVinylsResponse> findAllReturnedVinyls(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<VinylTransactionHistory> allBorrowedVinyls =
                transactionHistoryRepository.findAllReturnedVinyls(pageable, user.getId());
        List<BorrowedVinylsResponse> vinylResponse = allBorrowedVinyls.stream()
                .map(vinylMapper::toBorrowedVinylResponse)
                .toList();
        return new PageResponse<>(
                vinylResponse,
                allBorrowedVinyls.getNumber(),
                allBorrowedVinyls.getSize(),
                allBorrowedVinyls.getTotalElements(),
                allBorrowedVinyls.getTotalPages(),
                allBorrowedVinyls.isFirst(),
                allBorrowedVinyls.isLast()
        );
    }

    public Integer updateShareableStatus(Integer vinylId, Authentication connectedUser) {
        Vinyl vinyl = vinylRepository.findById(vinylId)
                      .orElseThrow(() -> new EntityNotFoundException("No vinyl found with ID: " + vinylId));
        User user = ((User) connectedUser.getPrincipal());
        if(!Objects.equals(vinyl.getOwner().getId(), user.getId()))
            throw new OperationNotPermittedException("You cannot update others vinyls shareable status");
        vinyl.setShareable(!vinyl.isShareable());
        vinylRepository.save(vinyl);
        return vinylId;
    }

    public Integer updateArchivedStatus(Integer vinylId, Authentication connectedUser) {
        Vinyl vinyl = vinylRepository.findById(vinylId)
                .orElseThrow(() -> new EntityNotFoundException("No vinyl found with ID: " + vinylId));
        User user = ((User) connectedUser.getPrincipal());
        if(!Objects.equals(vinyl.getOwner().getId(), user.getId()))
            throw new OperationNotPermittedException("You cannot update others vinyls archived status");
        vinyl.setArchived(!vinyl.isArchived());
        vinylRepository.save(vinyl);
        return vinylId;
    }

    public Integer borrowVinyl(Integer vinylId, Authentication connectedUser) {
        Vinyl vinyl = vinylRepository.findById(vinylId)
                .orElseThrow(() -> new EntityNotFoundException("No vinyl found with ID: " + vinylId));
        if(vinyl.isArchived() || !vinyl.isShareable())
            throw new OperationNotPermittedException("You cannot borrow this vinyl, it's either archived or not shareable");
        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(vinyl.getOwner().getId(), user.getId()))
            throw new OperationNotPermittedException("You cannot borrow your own vinyl");
        final boolean isAlreadyBorrowed = transactionHistoryRepository.isAlreadyBorrowedByUser(vinylId, user.getId());
        if(isAlreadyBorrowed) throw new OperationNotPermittedException("The requested vinyl is already borrowed");
        VinylTransactionHistory vinylTransactionHistory = VinylTransactionHistory.builder()
                .user(user)
                .vinyl(vinyl)
                .returned(false)
                .returnedApproved(false)
                .build();
        return transactionHistoryRepository.save(vinylTransactionHistory).getId();
    }

    public Integer returnBorrowedVinyl(Integer vinylId, Authentication connectedUser) {
        Vinyl vinyl = vinylRepository.findById(vinylId)
                .orElseThrow(() -> new EntityNotFoundException("No vinyl found with ID: " + vinylId));
        if(vinyl.isArchived() || !vinyl.isShareable())
            throw new OperationNotPermittedException("You cannot borrow this vinyl, it's either archived or not shareable");
        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(vinyl.getOwner().getId(), user.getId()))
            throw new OperationNotPermittedException("You cannot borrow or return your own vinyl");
        VinylTransactionHistory vinylTransactionHistory = transactionHistoryRepository.findByVinylIdAndUserId(vinylId, user.getId())
                  .orElseThrow(() -> new OperationNotPermittedException("You did not borrow this vinyl"));
        vinylTransactionHistory.setReturned(true);
        return transactionHistoryRepository.save(vinylTransactionHistory).getId();
    }

    public Integer approveReturnBorrowedVinyl(Integer vinylId, Authentication connectedUser) {
        Vinyl vinyl = vinylRepository.findById(vinylId)
                .orElseThrow(() -> new EntityNotFoundException("No vinyl found with ID: " + vinylId));
        if(vinyl.isArchived() || !vinyl.isShareable())
            throw new OperationNotPermittedException("You cannot borrow this vinyl, it's either archived or not shareable");
        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(vinyl.getOwner().getId(), user.getId()))
            throw new OperationNotPermittedException("You cannot borrow or return your own vinyl");
        VinylTransactionHistory vinylTransactionHistory = transactionHistoryRepository.findByVinylIdAndOwnerId(vinylId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("The vinyl is not returned yet. You cannot approve its return"));
        vinylTransactionHistory.setReturnedApproved(true);
        return transactionHistoryRepository.save(vinylTransactionHistory).getId();
    }

    public void uploadVinylCoverPicture(MultipartFile file, Authentication connectedUser, Integer vinylId) {
        Vinyl vinyl = vinylRepository.findById(vinylId)
                .orElseThrow(() -> new EntityNotFoundException("No vinyl found with ID: " + vinylId));
        User user = ((User) connectedUser.getPrincipal());
        var vinylCover = fileStorageService.saveFile(file, user.getId());
        vinyl.setVinylCoverImage(vinylCover);
        vinylRepository.save(vinyl);
    }
}
