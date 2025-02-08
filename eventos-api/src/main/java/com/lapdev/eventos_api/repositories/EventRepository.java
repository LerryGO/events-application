package com.lapdev.eventos_api.repositories;

import com.lapdev.eventos_api.domain.events.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
}
