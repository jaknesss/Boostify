package com.fra.boostify.vinyl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface VinylRepository extends JpaRepository<Vinyl, Integer>, JpaSpecificationExecutor<Vinyl> {

    @Query("""
               SELECT vinyl
               FROM Vinyl vinyl
               WHERE vinyl.archived = false
               AND vinyl.shareable = true
               AND vinyl.owner.id != :userId
           """)
    Page<Vinyl> findAllDisplayableVinyls(Pageable pageable, Integer userId);

}
