package com.bleschunov.dao;

import com.bleschunov.entity.AppDocument;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Bleschunov Dmitry
 */
public interface AppDocumentDao extends JpaRepository<AppDocument, Long> {
}
