package com.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    // ✅ application.properties 에서 Toss API 키를 주입받음
    @Value("${toss.secret-key}")
    private String tossSecretKey;

    @Value("${toss.client-key}")
    private String tossClientKey;

    // 결제 성공 처리
    @GetMapping("/success")
    public String confirmPayment(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount,
            Model model) {

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            String auth = tossSecretKey + ":";
            String encodedAuth = Base64.getEncoder()
                    .encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            headers.set("Authorization", "Basic " + encodedAuth);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> payloadMap = new HashMap<>();
            payloadMap.put("paymentKey", paymentKey);
            payloadMap.put("orderId", orderId);
            payloadMap.put("amount", amount);

            // ObjectMapper 사용 (Gson 대신)
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(payloadMap);

            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.tosspayments.com/v1/payments/confirm",
                    request,
                    String.class
            );

            model.addAttribute("paymentInfo", response.getBody());
            return "payment/success";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "payment/fail";
        }
    }

    // 결제 실패 처리
    @GetMapping("/fail")
    public String failPayment(
            @RequestParam String code,
            @RequestParam String message,
            @RequestParam String orderId,
            Model model) {

        model.addAttribute("code", code);
        model.addAttribute("message", message);
        model.addAttribute("orderId", orderId);
        return "payment/fail";
    }
}