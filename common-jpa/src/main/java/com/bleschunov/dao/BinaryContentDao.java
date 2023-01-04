package com.bleschunov.dao;

import com.bleschunov.entity.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Bleschunov Dmitry
 */
public interface BinaryContentDao extends JpaRepository<BinaryContent, Long> {
}
