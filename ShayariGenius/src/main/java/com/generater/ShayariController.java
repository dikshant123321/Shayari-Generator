package com.generater;
import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpEntity;

//import com.dikshant.Dto.ChartGptResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin("*")
public class ShayariController {
//    private static final String OPENAI_API_URL = "https://api.openai.com/v1/engines/gpt-3.5/completions";
//	private static final String OPENAI_API_URL = "https://api.openai.com/v1/engines/gpt-3.5-turbo/completions";
	private static final String OPENAI_API_URL = "https://api.openai.com/v1/engines/text-davinci-003/completions";

    private static final String OPENAI_API_KEY = "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";
    
    @GetMapping("/shayari/{keyword}")
    public String generateShayari(@PathVariable String keyword) throws JsonMappingException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//        headers.set("Authorization", "Bearer " + OPENAI_API_KEY);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + OPENAI_API_KEY);

        
        String prompt = "Shayari about " + keyword + ":";
//        String requestBody = "{\"prompt\":\"" + prompt + "\",\"max_tokens\":50}";
//        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        
//        ResponseEntity<String> response = restTemplate.postForEntity(OPENAI_API_URL, entity, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(Map.of("prompt", prompt, "max_tokens", 100));
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(OPENAI_API_URL, entity, String.class);
        String responseBody = response.getBody();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        if (response.getStatusCode().is2xxSuccessful()) {
            if (responseJson.has("choices")) {
                JsonNode choicesNode = responseJson.get("choices");
                if (choicesNode.isArray() && choicesNode.size() > 0) {
                    JsonNode choiceNode = choicesNode.get(0);
                    if (choiceNode.has("text")) {
                        String shayari = choiceNode.get("text").asText();
                        return shayari;
                    }
                }
            }
            
            return "No shayari available for the given keyword.";
        } else {
            return "Failed to generate shayari.";
        }
    }
}
