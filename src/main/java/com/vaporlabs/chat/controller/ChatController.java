package com.vaporlabs.chat.controller;

import com.vaporlabs.chat.dto.CreateLobbyRequest;
import com.vaporlabs.chat.dto.CreateLobbyResponse;
import com.vaporlabs.chat.dto.PresenceRequest;
import com.vaporlabs.chat.dto.SendMessageRequest;
import com.vaporlabs.chat.entity.Lobby;
import com.vaporlabs.chat.entity.Message;
import com.vaporlabs.chat.service.IChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    private final IChatService chatService;

    @Autowired
    public ChatController(IChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/create")
    public CreateLobbyResponse createLobby(@RequestBody CreateLobbyRequest request) {
        log.info("Creating lobby with TTL: {}", request.getTtl());
        Lobby lobby = chatService.createLobby(request.getTtl());
        CreateLobbyResponse response = new CreateLobbyResponse();
        response.setJoinCode(lobby.getJoinCode());
        response.setCreatedAt(lobby.getCreatedAt());
        response.setId(lobby.getId());
        return response;
    }

    @PostMapping("/heartbeat")
    public void heartbeat(@RequestBody PresenceRequest request) {
        log.info("User {} is online in lobby {}", request.getUserId(), request.getLobbyId());
        chatService.heartbeat(request.getLobbyId(), request.getUserId());
    }

    @PostMapping("/join")
    public void joinLobby(@RequestBody PresenceRequest request) {
        log.info("User {} joining lobby {}", request.getUserId(), request.getLobbyId());
        chatService.joinLobby(request.getLobbyId(), request.getUserId());
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody SendMessageRequest request) {
        log.info("Message sent in lobby {}", request.getLobbyId());
        chatService.sendMessage(request.getMessage(), request.getLobbyId(), request.getUsername());
    }

    @GetMapping("/messages")
    public List<Message> getMessages(@RequestParam String lobbyId) {
        log.info("Getting messages for lobby {}", lobbyId);
        return chatService.getMessages(lobbyId);
    }

    @GetMapping("/activeUsers")
    public List<String> getActiveUsers(@RequestParam String lobbyId) {
        return chatService.getActiveUsers(lobbyId);
    }
}
