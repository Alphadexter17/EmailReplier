package com.email_writer.Controller;

import com.email_writer.Paylod.EmailRequestDto;
import com.email_writer.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "*")
public class EmailGenerator {
    private EmailService emailService;
    public EmailGenerator(EmailService emailService) {
        this.emailService = emailService;
    }
    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail(@RequestBody EmailRequestDto emailRequest){
        String response = emailService.generateReply(emailRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
