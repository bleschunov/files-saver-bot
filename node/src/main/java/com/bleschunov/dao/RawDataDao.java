package com.bleschunov.dao;

import com.bleschunov.entity.RawData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Bleschunov Dmitry
 */
@Repository
public interface RawDataDao extends JpaRepository<RawData, Long> {
}
