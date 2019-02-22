package com.eriks.service.config.model;

import lombok.Getter;

@Getter
public class EmailProperties {
    private String apiKey;
    private String from;
    private boolean isTest;
    private String activationTemplateId;
    private String loginTemplateId;
    private String passwordResetTemplateId;
    private String passwordUpdatedTemplateId;
    private String verifyUserRedirectUrl;
    private String verifyPasswordRedirectUrl;

    public EmailProperties(String apiKey,
                           String from,
                           String isTest,
                           String activationTemplateId,
                           String loginTemplateId,
                           String passwordResetTemplateId,
                           String passwordUpdatedTemplateId,
                           String verifyUserRedirectUrl,
                           String verifyPasswordRedirectUrl
    ) {
        this.apiKey = apiKey;
        this.from = from;
        this.isTest = Boolean.valueOf(isTest);
        this.activationTemplateId = activationTemplateId;
        this.loginTemplateId = loginTemplateId;
        this.passwordResetTemplateId = passwordResetTemplateId;
        this.passwordUpdatedTemplateId = passwordUpdatedTemplateId;
        this.verifyUserRedirectUrl = verifyUserRedirectUrl;
        this.verifyPasswordRedirectUrl = verifyPasswordRedirectUrl;
    }
}
