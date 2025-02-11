package com.lapdev.eventos_api.domain.user;

public record RegisterDTO(String login, String password, UserRole role) {
}
