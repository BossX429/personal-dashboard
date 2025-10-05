package com.dashboard.api.repository;

import com.dashboard.api.model.SystemMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SystemMetricsRepository extends JpaRepository<SystemMetrics, String> {
    
    List<SystemMetrics> findByTimestampAfterOrderByTimestampDesc(LocalDateTime timestamp);
    
    List<SystemMetrics> findByServerNameOrderByTimestampDesc(String serverName);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM SystemMetrics sm WHERE sm.timestamp < :cutoff")
    void deleteByTimestampBefore(LocalDateTime cutoff);
    
    @Query("SELECT sm FROM SystemMetrics sm WHERE sm.timestamp >= :start AND sm.timestamp <= :end ORDER BY sm.timestamp DESC")
    List<SystemMetrics> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}