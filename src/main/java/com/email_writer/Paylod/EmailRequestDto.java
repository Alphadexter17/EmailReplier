package com.email_writer.Paylod;

import lombok.Data;

@Data
public class EmailRequestDto {
    private String emailContent;
    private String tone;

    public EmailRequestDto(String emailContent, String tone) {
        this.emailContent = emailContent;
        this.tone = tone;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }
}
