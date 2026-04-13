// package com.finwing.service;

// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;
// import org.springframework.http.*;

// @Service
// public class AIService {

//     private final String FASTAPI_URL = "http://127.0.0.1:8000/predict";

//     public String getCategory(TransactionRequest request) {

//         RestTemplate restTemplate = new RestTemplate();

//         HttpHeaders headers = new HttpHeaders();
//         headers.setContentType(MediaType.APPLICATION_JSON);

//         HttpEntity<TransactionRequest> entity = new HttpEntity<>(request, headers);

//         ResponseEntity<String> response = restTemplate.exchange(
//                 FASTAPI_URL,
//                 HttpMethod.POST,
//                 entity,
//                 String.class
//         );

//         return response.getBody();
//     }
// }