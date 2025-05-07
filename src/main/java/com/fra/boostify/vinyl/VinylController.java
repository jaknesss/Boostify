package com.fra.boostify.vinyl;

import com.fra.boostify.common.PageResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/vinyls")
@RequiredArgsConstructor
@Tag(name = "Vinyl")
public class VinylController {

    private final VinylService service;

    @PostMapping
    public ResponseEntity<Integer> saveVinyl(
            @Valid @RequestBody VinylRequest request,
            Authentication connectedUser) {
        return ResponseEntity.ok(service.save(request, connectedUser));
    }

    @GetMapping("{vinyl-id}")
    public ResponseEntity<VinylResponse> findBookById(
            @PathVariable("vinyl-id") Integer vinylId){
        return ResponseEntity.ok(service.findById(vinylId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<VinylResponse>> findAllVinyls(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(service.findAllVinyls(page, size, connectedUser));
    }


    @GetMapping("/owner")
    public ResponseEntity<PageResponse<VinylResponse>> findAllVinylsByOwner(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(service.findAllVinylsByOwner(page, size, connectedUser));
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedVinylsResponse>> findAllBorrowedVinyls(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(service.findAllBorrowedVinyls(page, size, connectedUser));
    }

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedVinylsResponse>> findAllReturnedVinyls(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(service.findAllReturnedVinyls(page, size, connectedUser));
    }

    @PatchMapping("/shareable/{vinyl-id}")
    public ResponseEntity<Integer> updateShareableStatus(
            @PathVariable("vinyl-id") Integer vinylId,
            Authentication connectedUser)
    {
        return ResponseEntity.ok(service.updateShareableStatus(vinylId, connectedUser));
    }

    @PatchMapping("/archived/{vinyl-id}")
    public ResponseEntity<Integer> updateArchivedStatus(
            @PathVariable("vinyl-id") Integer vinylId,
            Authentication connectedUser)
    {
        return ResponseEntity.ok(service.updateArchivedStatus(vinylId, connectedUser));
    }

    @PatchMapping("/borrow/{vinyl-id}")
    public ResponseEntity<Integer> borrowVinyl(
            @PathVariable("vinyl-id") Integer vinylId,
            Authentication connectedUser)
    {
        return ResponseEntity.ok(service.borrowVinyl(vinylId, connectedUser));
    }

    @PatchMapping("/borrow/return/{vinyl-id}")
    public ResponseEntity<Integer> returnBorrowedVinyl(
            @PathVariable("vinyl-id") Integer vinylId,
            Authentication connectedUser)
    {
        return ResponseEntity.ok(service.returnBorrowedVinyl(vinylId, connectedUser));
    }

    @PatchMapping("/borrow/return/approve/{vinyl-id}")
    public ResponseEntity<Integer> approveReturnBorrowedVinyl(
            @PathVariable("vinyl-id") Integer vinylId,
            Authentication connectedUser)
    {
        return ResponseEntity.ok(service.approveReturnBorrowedVinyl(vinylId, connectedUser));
    }
    
    @PostMapping(value = "/cover/{vinyl-id}", consumes = "multipart/form-data")
    public ResponseEntity<Integer> uploadVinylCover(
            @PathVariable("vinyl-id") Integer vinylId,
            @Parameter()
            @RequestParam("file") MultipartFile file,
            Authentication connectedUser)
    {
        service.uploadVinylCoverPicture(file, connectedUser, vinylId);
        return ResponseEntity.accepted().build();
    }

}
