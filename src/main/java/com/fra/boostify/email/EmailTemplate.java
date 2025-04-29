package com.fra.boostify.email;
import lombok.Getter;

@Getter
public enum EmailTemplate {
    ACTIVATE_ACCOUNT("actvate_account");

    private final String name;

    EmailTemplate(String name) {
        this.name = name;
    }
}
