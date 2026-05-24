package com.iot.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "camera_events")
public class CameraEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "detected_object")
    private String detectedObject;

    private double confidence;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "detected_at")
    private LocalDateTime detectedAt;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    public CameraEvent() {}

    public Integer getId() { return id; }
    public String getDetectedObject() { return detectedObject; }
    public double getConfidence() { return confidence; }
    public String getImagePath() { return imagePath; }
    public LocalDateTime getDetectedAt() { return detectedAt; }
    public Device getDevice() { return device; }

    public void setId(Integer id) { this.id = id; }
    public void setDetectedObject(String detectedObject) { this.detectedObject = detectedObject; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public void setDetectedAt(LocalDateTime detectedAt) { this.detectedAt = detectedAt; }
    public void setDevice(Device device) { this.device = device; }
}