package ma.appsegov.projectservice.web;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import ma.appsegov.projectservice.model.Message;
import ma.appsegov.projectservice.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequiredArgsConstructor
public class ChatController {


    private final SimpMessagingTemplate template;


    private final MessageRepository messageRepository;


    @MessageMapping("/ichat")
//    @SendTo("/topic/messages/")
    public Message handleMessage(Message message) {
        message.setTimestamp(new Date());
        messageRepository.save(message);
        template.convertAndSend("/topic/messages/" + message.getProject().getId(), message);
        return message;
    }
}