package com.bleschunov.entity;

import com.bleschunov.entity.enums.UserState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author Bleschunov Dmitry
 */
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long telegramUserId;
    @CreationTimestamp
    private LocalDateTime firstLoginDate;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private boolean isActive;
    @Enumerated(EnumType.STRING)
    private UserState state;
}
