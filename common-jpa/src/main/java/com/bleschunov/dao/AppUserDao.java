package com.bleschunov.dao;

import com.bleschunov.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Bleschunov Dmitry
 */
public interface AppUserDao extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByTelegramUserId(long telegramId);
}
