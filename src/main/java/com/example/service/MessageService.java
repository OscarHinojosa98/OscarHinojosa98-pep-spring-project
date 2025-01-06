package com.example.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }
    //Create new message
    public Message addNewMessage(Message message){
        //Check for messageText requirements and if postedBy matches account
        if(!message.getMessageText().isBlank() && message.getMessageText().length() < 256 && accountRepository.existsById(message.getPostedBy())){
            Message msg = new Message(message.getPostedBy(),message.getMessageText(), message.getTimePostedEpoch());
            messageRepository.save(msg);
            return msg;
        }
        else{
            return null;
        }
    }
    //Get all messages
    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }
    //Get message by id
    public Optional<Message> getMessageById(Integer id){
        return messageRepository.findById(id);
    }
    //Delete message
    public boolean deleteMessageById(Integer id){
        //Check if Id is found
        if(messageRepository.findById(id).isPresent()){
            messageRepository.deleteById(id);
            return true;
        }
        else
        {
            return false;
        }
    }
    //Update message
    public boolean updateMessageById(Integer id,String messageText){
        Optional<Message> optionalMsg = messageRepository.findById(id);
        //Check for messageText requirements and if Id is found
        if(messageText.length() > 0 && messageText.length() < 256 && optionalMsg.isPresent()){
            Message msg = optionalMsg.get();
            msg.setMessageText(messageText);
            messageRepository.save(msg);
            return true;
        }
        else
        {
            return false;
        }
    }
    //Get all messages by user
    public List<Message> getAllMessagesByUser(Integer postedBy){
        return messageRepository.findByPostedBy(postedBy);
    }


}
