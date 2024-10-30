package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private AccountRepository accountRepository;

    public Message createMessage(Message message) {
                if (message.getMessageText() == null || message.getMessageText().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message text cannot be blank");
        }
        if (message.getMessageText().length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message text cannot exceed 255 characters");
        }

        
        if (message.getPostedBy() == null || !accountRepository.existsById(message.getPostedBy())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PostedBy account does not exist");
        }

        
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(Integer messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

    public int deleteMessage(Integer messageId) {
        if (messageRepository.existsById(messageId)) {
        
            messageRepository.deleteById(messageId);
            return 1; 
        } else {
            return 0; 
        }
    }

    public Message updateMessage(Integer messageId, String newMessageText) {
                
        Message existingMessage = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message not found"));

        
        if (newMessageText == null || newMessageText.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New message text cannot be blank");
        }
        if (newMessageText.length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New message text cannot exceed 255 characters");
        }

        
        existingMessage.setMessageText(newMessageText);
       return messageRepository.save(existingMessage);

        
        // return 1;
    }

    public List<Message> getMessagesByAccount(int account) {
        return messageRepository.findByPostedBy(account);
    }
}
