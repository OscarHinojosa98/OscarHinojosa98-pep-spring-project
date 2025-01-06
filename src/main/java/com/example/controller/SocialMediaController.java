package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.h2.util.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;

import com.example.service.MessageService;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@Controller
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }
    //Register Account
    @PostMapping("/register")
    public @ResponseBody ResponseEntity<Account> addNewAccount(@RequestBody Account account){
         
        Optional<Account> optionalAccount = Optional.ofNullable((accountService.addNewAccount(account)));
        //Created Successfully
        if(optionalAccount.isPresent() && optionalAccount.get().getUsername() != ""){
        Account acc = optionalAccount.get();
        return  ResponseEntity.status(200).body(acc); 
        }
        //Duplicate Username
        else if (optionalAccount.get().getUsername() == "")
        {
            return  ResponseEntity.status(409).body(null);    
        }
        //Unsuccessful Account Creation
        else
        {
            return  ResponseEntity.status(400).body(null); 
        }
    }
    //Login Account
    @PostMapping("/login")
    public @ResponseBody ResponseEntity<Account> login(@RequestBody Account account){
        Optional<Account> optionalAccount = accountService.login(account);
        //Login Successful
        if(optionalAccount.isPresent()){
            Account acc = optionalAccount.get();
            return  ResponseEntity.status(200).body(acc); 
        }
        //Login Failed
        else{
            return  ResponseEntity.status(401).body(null); 
        }

    }
    //Create New Message
    @PostMapping("/messages")
    public @ResponseBody ResponseEntity<Message> addNewMessage(@RequestBody Message message){
        Optional<Message> optionalMessage = Optional.ofNullable((messageService.addNewMessage(message)));
        //Successful
        if(optionalMessage.isPresent()){
            Message msg = optionalMessage.get();
            return  ResponseEntity.status(200).body(msg); 
        }
        //Failed
        else{
            return  ResponseEntity.status(400).body(null); 
        }
    }
    //Get all Messages
    @GetMapping("/messages")
    public @ResponseBody ResponseEntity<List<Message>> getAllMessages(){
        List<Message> lst = messageService.getAllMessages();
        return  ResponseEntity.status(200).body(lst); 
    }
    //Get a message by id
    @GetMapping("/messages/{messageId}")
    public @ResponseBody ResponseEntity<Message> getMessageById(@PathVariable Integer messageId){
        Optional<Message> optionalMessage = messageService.getMessageById(messageId);
        //Successful
        if(optionalMessage.isPresent()){
            Message msg = optionalMessage.get();
            return  ResponseEntity.status(200).body(msg);
        }
        //Failed
        else{
        return  ResponseEntity.status(200).body(null); 
        }
    }
    //Delete message
    @DeleteMapping("/messages/{messageId}")
    public @ResponseBody ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId){
        boolean deleted  = messageService.deleteMessageById(messageId);
        //Successful
        if(deleted){
            return  ResponseEntity.status(200).body(1);
        }
        //Failed
        else{
        return  ResponseEntity.status(200).body(null); 
        }
    }
    //Update message
    @PatchMapping("/messages/{messageId}")
    public @ResponseBody ResponseEntity<Integer> updateMessageById(@PathVariable Integer messageId, @RequestBody String messageText) throws JsonParseException, IOException{
        //Getting only "messageText from requestbody"
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> body = mapper.readValue(messageText, Map.class);
        String str = body.get("messageText");
        boolean updated  = messageService.updateMessageById(messageId, str);
        //Succesful
        if(updated){
            return  ResponseEntity.status(200).body(1);
        }
        //Failed
        else{
        return  ResponseEntity.status(400).body(null); 
        }
    }
    //Get all of users messages
    @GetMapping("accounts/{accountId}/messages")
    public @ResponseBody ResponseEntity<List<Message>> getAllMessagesByUser(@PathVariable Integer accountId){
        List<Message> lst = messageService.getAllMessagesByUser(accountId);
        return  ResponseEntity.status(200).body(lst); 
    }

}
