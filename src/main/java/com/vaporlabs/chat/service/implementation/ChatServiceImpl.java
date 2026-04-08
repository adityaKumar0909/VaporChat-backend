package com.vaporlabs.chat.service.implementation;

import com.vaporlabs.chat.entity.Lobby;
import com.vaporlabs.chat.entity.Message;
import com.vaporlabs.chat.enums.LobbyTTL;
import com.vaporlabs.chat.repository.ILobbyRepository;
import com.vaporlabs.chat.service.IChatService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements IChatService {
    private final ILobbyRepository lobbyRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void testRedis() {
        redisTemplate.opsForValue().set("test", "hello");
        System.out.println(redisTemplate.opsForValue().get("test"));
    }


    @Override
    public Lobby createLobby(LobbyTTL ttlPreset) {
        Lobby lobby = new Lobby();
        lobby.setId(java.util.UUID.randomUUID().toString());
        lobby.setJoinCode(generateJoinCode());
        lobby.setCreatedAt(java.time.LocalDateTime.now());
        lobby.setTtl(ttlPreset.getSeconds());
        return lobbyRepository.save(lobby);
    }

    @Override
    public void joinLobby(String lobbyId, String userId) {

        String presenceKey = "lobby:" + lobbyId + ":user:" + userId;
        String usersKey = "lobby:" + lobbyId + ":users";
        redisTemplate.opsForValue().set(presenceKey, "online", 30, TimeUnit.SECONDS);

        redisTemplate.opsForSet().add(usersKey, userId);
        redisTemplate.expire(usersKey, 30, TimeUnit.SECONDS);

    }

    @Override
    public void sendMessage(String encryptedMessage, String lobbyId, String username) {

        Lobby lobby = lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new RuntimeException("Lobby not found with id: " + lobbyId));

//        String lobbyId = lobby.getId();
        Message message = new Message();
        message.setEncryptedMessage(encryptedMessage);
        message.setLobbyId(lobbyId);
        message.setUsername(username);
        String key = "lobby:" + lobbyId + ":messages";

        redisTemplate.opsForList().rightPush(key, message);


        // if TTL is not set, set it
        if (redisTemplate.getExpire(key) == -1) {
            redisTemplate.expire(key, lobby.getTtl(), TimeUnit.SECONDS);
        }

    }

    @Override
    public void heartbeat(String lobbyId, String userId) {
        String presenceKey = "lobby:" + lobbyId + ":user:" + userId;
        String usersKey = "lobby:" + lobbyId + ":users";
        redisTemplate.opsForValue().set(presenceKey, "online", 30, TimeUnit.SECONDS);

        redisTemplate.opsForSet().add(usersKey, userId);
        redisTemplate.expire(usersKey, 30, TimeUnit.SECONDS);
    }


    @Override
    public List<Message> getMessages(String lobbyId) {
        String key = "lobby:" + lobbyId + ":messages";
        List<Object> rawMessages = redisTemplate.opsForList().range(key, 0, -1);
        System.out.println("Raw messages from Redis: " + rawMessages);
        List<Message> messages = rawMessages.stream()
                .map(obj -> (Message) obj)
                .toList();
        System.out.println("Parsed messages: " + messages);
        return messages;
    }

    @Override
    public String generateJoinCode() {
        String code;

        do {
            code = java.util.UUID.randomUUID()
                    .toString()
                    .substring(0, 6)
                    .toUpperCase();
        } while (lobbyRepository.findByJoinCode(code).isPresent());

        return code;
    }

    @Override
    public List<String> getActiveUsers(String lobbyId) {
        String usersKey = "lobby:" + lobbyId + ":users";
        return redisTemplate.opsForSet().members(usersKey).stream()
                .map(obj -> (String) obj)
                .toList();
    }
}
