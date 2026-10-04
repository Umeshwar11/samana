package com.samana.service;

import com.samana.dto.ContactMessageRequest;
import com.samana.entity.ContactMessage;
import com.samana.repository.ContactMessageRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;

    public ContactMessageService(ContactMessageRepository contactMessageRepository) {
        this.contactMessageRepository = contactMessageRepository;
    }

    public ContactMessage saveMessage(ContactMessageRequest request) {
        ContactMessage contactMessage = new ContactMessage();

        contactMessage.setName(request.getName());
        contactMessage.setEmail(request.getEmail());
        contactMessage.setPhone(request.getPhone());
        contactMessage.setSubject(request.getSubject());
        contactMessage.setMessage(request.getMessage());

        return contactMessageRepository.save(contactMessage);
    }

    public List<ContactMessage> getAllMessages() {
    return contactMessageRepository.findAllByOrderByCreatedAtDesc();
}
}