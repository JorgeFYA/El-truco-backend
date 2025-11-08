package com.marketminds.ecommerce.elTruco.services;

import com.marketminds.ecommerce.elTruco.models.Newsletter;
import com.marketminds.ecommerce.elTruco.repositories.NewsletterRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NewsletterService {

    private final NewsletterRepository newsletterRepository;

    public List<Newsletter> getAllActiveNewsletterEmails() {
        return newsletterRepository.findAllActive();
    }

    public Newsletter addNewsletterEmail(String email) {
        // Valida formato (aunque el DTO ya lo hace, no está de más para seguridad)
        Optional<Newsletter> existing = newsletterRepository.findByEmail(email);

        if (existing.isPresent()) {
            Newsletter found = existing.get();
            if (found.isActive()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está suscrito");
            } else {
                found.setActive(true);
                return newsletterRepository.save(found);
            }
        }

        Newsletter newNewsletter = new Newsletter();
        newNewsletter.setEmail(email);
        newNewsletter.setActive(true);

        return newsletterRepository.save(newNewsletter);
    }

    public Newsletter deleteNewsletterEmailById(Long id) {
        Newsletter newsletter = newsletterRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El correo no existe"));

        if (!newsletter.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya fue dado de baja");
        }

        newsletter.setActive(false);
        return newsletterRepository.save(newsletter);
    }
}

