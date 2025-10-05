package com.dashboard.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "system_metrics")
public class SystemMetrics {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @NotNull
    @Column(name = "cpu_usage")
    private Double cpuUsage;
    
    @NotNull
    @Column(name = "memory_usage")
    private Double memoryUsage;
    
    @NotNull
    @Column(name = "disk_usage")
    private Double diskUsage;
    
    @Column(name = "network_in")
    private Double networkIn;
    
    @Column(name = "network_out")
    private Double networkOut;
    
    @NotNull
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    
    @Column(name = "server_name")
    private String serverName;

    // Constructors
    public SystemMetrics() {
        this.timestamp = LocalDateTime.now();
    }

    public SystemMetrics(Double cpuUsage, Double memoryUsage, Double diskUsage) {
        this();
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.diskUsage = diskUsage;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(Double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public Double getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(Double diskUsage) {
        this.diskUsage = diskUsage;
    }

    public Double getNetworkIn() {
        return networkIn;
    }

    public void setNetworkIn(Double networkIn) {
        this.networkIn = networkIn;
    }

    public Double getNetworkOut() {
        return networkOut;
    }

    public void setNetworkOut(Double networkOut) {
        this.networkOut = networkOut;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public String toString() {
        return "SystemMetrics{" +
                "id='" + id + '\'' +
                ", cpuUsage=" + cpuUsage +
                ", memoryUsage=" + memoryUsage +
                ", diskUsage=" + diskUsage +
                ", timestamp=" + timestamp +
                '}';
    }
}