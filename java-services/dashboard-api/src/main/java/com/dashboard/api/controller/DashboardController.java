package com.dashboard.api.controller;

import com.dashboard.api.model.SystemMetrics;
import com.dashboard.api.service.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:3000", "https://*.azurewebsites.net"})
public class DashboardController {

    @Autowired
    private MetricsService metricsService;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "healthy",
            "service", "dashboard-api",
            "timestamp", System.currentTimeMillis(),
            "version", "1.0.0"
        ));
    }

    @GetMapping("/metrics/system")
    public ResponseEntity<SystemMetrics> getSystemMetrics() {
        SystemMetrics metrics = metricsService.getCurrentSystemMetrics();
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/metrics/history")
    public ResponseEntity<List<SystemMetrics>> getMetricsHistory(
            @RequestParam(defaultValue = "24") int hours) {
        List<SystemMetrics> history = metricsService.getMetricsHistory(hours);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/metrics")
    public ResponseEntity<SystemMetrics> saveMetrics(@RequestBody SystemMetrics metrics) {
        SystemMetrics saved = metricsService.saveMetrics(metrics);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/dashboard/config")
    public ResponseEntity<Map<String, Object>> getDashboardConfig() {
        return ResponseEntity.ok(Map.of(
            "widgets", List.of(
                Map.of("id", 1, "type", "system-metrics", "title", "System Metrics", "enabled", true),
                Map.of("id", 2, "type", "performance", "title", "Performance", "enabled", true),
                Map.of("id", 3, "type", "alerts", "title", "Alerts", "enabled", false)
            ),
            "refreshInterval", 30000,
            "theme", "dark"
        ));
    }
}