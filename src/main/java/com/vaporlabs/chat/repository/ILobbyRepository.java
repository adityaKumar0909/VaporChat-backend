package com.vaporlabs.chat.repository;

import com.vaporlabs.chat.entity.Lobby;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ILobbyRepository extends CrudRepository<Lobby, String> {
    Optional<Lobby> findByJoinCode(String joinCode);
}
