package com.email_writer.service;


import com.email_writer.Paylod.EmailRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailService {
    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    @Value("${gemini.api.key}")
    private String geminiApiKey;
    private final WebClient webClient;

    EmailService(WebClient.Builder webClient){
        this.webClient = WebClient.builder().build();
    }
    public String generateReply(EmailRequestDto emailRequestDto){
        //Build the prompt
        String prompt=buildPrompt(emailRequestDto);
        //craft the request
        Map<String , Object> requestBody=Map.of(
                "contents",new Object[]{
                        Map.of(
                                "parts",new Object[]{
                                        Map.of("text",prompt)
                                }
                        )
        }
        );
        //Do request and get response
        String response=webClient.post()
                .uri(geminiApiUrl+"?key="+geminiApiKey)
                .header("Content-Type","application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        //Return response and return it

        return extractResponseContent(response);
    }
    private String extractResponseContent(String response){
        try{
            ObjectMapper objectMapper= new ObjectMapper();
            JsonNode rootnode= objectMapper.readTree(response);
            return rootnode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .get("text")
                    .asText();
        }
        catch (Exception e){
            return "Error processing request" + e.getMessage();
        }
    }

    private String buildPrompt(EmailRequestDto emailRequestDto) {
        StringBuilder prompt=new StringBuilder();
        prompt.append("Generate a professional email reply for the following email content.I want only text directly nothing like 'sure there you go or etc ' ");
        if(emailRequestDto.getTone()!=null && !emailRequestDto.getTone().isEmpty()){
            prompt.append("use a ").append(emailRequestDto.getTone()).append(" tone.");
        }
        prompt.append("\nOriginal Message:\n").append(emailRequestDto.getEmailContent());
        return prompt.toString();
    }
}
