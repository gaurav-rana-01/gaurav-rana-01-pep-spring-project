package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController

public class SocialMediaController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        if(messageService.createMessage(message)== null){
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>( message, HttpStatus.OK);
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Message message = messageService.getMessageById(messageId);

   
        if (message != null) {
            // return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        return null;
        
        
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable Integer messageId) {
        int rowsAffected = messageService.deleteMessage(messageId);
        if (rowsAffected > 0) {
            return new ResponseEntity<>(rowsAffected, HttpStatus.OK);
        } 
        // else {
        //     return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        // }
    
         return null;
        
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer messageId, @RequestBody Message newMessageText) {
        System.err.println("Hello" + newMessageText);
        if (newMessageText == null || newMessageText.getMessageText().trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
    
        Message msg = messageService.updateMessage(messageId, newMessageText.getMessageText());
        
        if (msg != null) {
            return new ResponseEntity<>( 1, HttpStatus.OK); 
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }
        
    }

    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getMessagesByAccount(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByAccount(accountId);
       

        // if (messages == null || messages.isEmpty()) {
        //     return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        // }
        
        return messages;
        // return null;
        
    }

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        if (account == null || account.getUsername() == null || account.getPassword() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    
        
        Account savedAccount = accountService.register(account);
    
        
        return new ResponseEntity<>(savedAccount, HttpStatus.OK);
        
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        if (account == null || account.getUsername() == null || account.getPassword() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    
        
        Optional<Account> existingAccount = accountService.login(account.getUsername(), account.getPassword());
        if (existingAccount.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); 
        }
    
        
        // existingAccount.setPassword(null); 
        return new ResponseEntity<>(existingAccount.get(), HttpStatus.OK);
        
    }
}
