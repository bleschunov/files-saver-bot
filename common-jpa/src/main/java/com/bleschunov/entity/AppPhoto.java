package com.bleschunov.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
@Table(name = "app_photo")
public class AppPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String telegramFileId;
    @OneToOne(fetch = FetchType.LAZY)
    private BinaryContent binaryContent;
    private int fileSize;
}
