package com.dashboard.api.service;

import com.dashboard.api.model.SystemMetrics;
import com.dashboard.api.repository.SystemMetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MetricsService {

    @Autowired
    private SystemMetricsRepository metricsRepository;

    public SystemMetrics getCurrentSystemMetrics() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        
        double cpuUsage = osBean.getProcessCpuLoad() * 100;
        double memoryUsed = memoryBean.getHeapMemoryUsage().getUsed();
        double memoryMax = memoryBean.getHeapMemoryUsage().getMax();
        double memoryUsage = (memoryUsed / memoryMax) * 100;
        
        // For demo purposes, using random values for disk usage
        double diskUsage = Math.random() * 100;
        
        SystemMetrics metrics = new SystemMetrics(cpuUsage, memoryUsage, diskUsage);
        metrics.setServerName("dashboard-api");
        metrics.setNetworkIn(Math.random() * 1000);
        metrics.setNetworkOut(Math.random() * 1000);
        
        return metrics;
    }

    public SystemMetrics saveMetrics(SystemMetrics metrics) {
        if (metrics.getTimestamp() == null) {
            metrics.setTimestamp(LocalDateTime.now());
        }
        return metricsRepository.save(metrics);
    }

    public List<SystemMetrics> getMetricsHistory(int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return metricsRepository.findByTimestampAfterOrderByTimestampDesc(since);
    }

    @Scheduled(fixedRate = 60000) // Every minute
    public void collectAndSaveMetrics() {
        try {
            SystemMetrics metrics = getCurrentSystemMetrics();
            saveMetrics(metrics);
            System.out.println("Metrics collected: " + metrics);
        } catch (Exception e) {
            System.err.println("Failed to collect metrics: " + e.getMessage());
        }
    }

    public void deleteOldMetrics(int daysToKeep) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(daysToKeep);
        metricsRepository.deleteByTimestampBefore(cutoff);
    }
}