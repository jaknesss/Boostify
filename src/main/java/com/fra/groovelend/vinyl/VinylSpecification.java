package com.fra.groovelend.vinyl;

import org.springframework.data.jpa.domain.Specification;

public class VinylSpecification {
    public static Specification<Vinyl> withOwnerId(Integer ownerId) {
        return (root, query, cb)
                -> cb.equal(root.get("owner").get("id"), ownerId);
    }
}
