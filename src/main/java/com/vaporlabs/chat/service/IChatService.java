package com.vaporlabs.chat.service;

import com.vaporlabs.chat.entity.Lobby;
import com.vaporlabs.chat.entity.Message;
import com.vaporlabs.chat.enums.LobbyTTL;

import java.util.List;

public interface IChatService {
    Lobby createLobby(LobbyTTL ttlPreset);

    void joinLobby(String lobbyId, String userId);

    void sendMessage(String encryptedMessage, String lobbyId, String username);

    void heartbeat(String lobbyId, String userId);

    List<Message> getMessages(String lobbyId);

    String generateJoinCode();

    List<String> getActiveUsers(String lobbyId);
}
