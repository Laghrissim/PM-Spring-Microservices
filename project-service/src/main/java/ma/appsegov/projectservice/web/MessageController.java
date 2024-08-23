package ma.appsegov.projectservice.web;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import ma.appsegov.projectservice.model.Message;
import ma.appsegov.projectservice.model.ReadReceiptRequest;
import ma.appsegov.projectservice.repository.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class MessageController {


    private final MessageRepository messageRepository;

//    @GetMapping(value = "/messages/{channelId}")
//    public Page<Message> findMessages(Pageable pageable, @PathVariable("channelId") String channelId) {
//        return messageRepository.findAllByChannel(channelId, pageable);
//    }

//    @PostMapping(value = "/messages")
//    @Transactional
//    public void sendReadReceipt(@RequestBody ReadReceiptRequest request) {
//        messageRepository.sendReadReceipt(request.getChannel(), request.getUsername());
//    }
    @GetMapping("/messages/{projectId}")
    public List<Message> getProjectMessages(@PathVariable Long projectId) {
        return messageRepository.findAllByProjectId(projectId);
    }
}