package com.bleschunov.dao;

import com.bleschunov.entity.AppPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Bleschunov Dmitry
 */
public interface AppPhotoDao extends JpaRepository<AppPhoto, Long> {
}
