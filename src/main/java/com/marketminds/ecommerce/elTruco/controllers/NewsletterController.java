package com.marketminds.ecommerce.elTruco.controllers;

import com.marketminds.ecommerce.elTruco.dtos.NewsletterDto;
import com.marketminds.ecommerce.elTruco.models.Newsletter;
import com.marketminds.ecommerce.elTruco.services.NewsletterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/newsletter")
@AllArgsConstructor
public class NewsletterController {

    private final NewsletterService newsletterService;

    @PostMapping("/subscribe")
    public ResponseEntity<Newsletter> subscribe(@Valid @RequestBody NewsletterDto dto) {
        Newsletter saved = newsletterService.addNewsletterEmail(dto.getEmail());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Newsletter>> getAll() {
        return ResponseEntity.ok(newsletterService.getAllActiveNewsletterEmails());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Newsletter> unsubscribe(@PathVariable Long id) {
        return ResponseEntity.ok(newsletterService.deleteNewsletterEmailById(id));
    }
}
