package com.samana.controller;

import com.samana.dto.ContactMessageRequest;
import com.samana.entity.ContactMessage;
import com.samana.service.ContactMessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    public ContactMessageController(ContactMessageService contactMessageService) {
        this.contactMessageService = contactMessageService;
    }

    @PostMapping
    public ResponseEntity<?> sendMessage(
            @Valid @RequestBody ContactMessageRequest request) {

        ContactMessage savedMessage = contactMessageService.saveMessage(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "message", "Thank you! Your message has been sent successfully.",
                        "id", savedMessage.getId()
                )
        );
    }
}