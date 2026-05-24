package com.iot.backend.controller;

import com.iot.backend.dto.AuthResponse;
import com.iot.backend.dto.RegisterRfidRequest;
import com.iot.backend.dto.RfidTagResponse;
import com.iot.backend.service.RfidTagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rfid")
@CrossOrigin(origins = "*")
public class RfidTagController {

    private final RfidTagService rfidTagService;

    public RfidTagController(RfidTagService rfidTagService) {
        this.rfidTagService = rfidTagService;
    }

    @PostMapping("/register")
    public RfidTagResponse registerTag(
            @RequestBody RegisterRfidRequest request
    ) {
        return rfidTagService.registerTag(request);
    }

    @GetMapping("/user/{userId}")
    public List<RfidTagResponse> getTagsByUser(
            @PathVariable Integer userId
    ) {
        return rfidTagService.getTagsByUser(userId);
    }

    @PostMapping("/login/{tagUid}")
    public AuthResponse loginByRfid(
            @PathVariable String tagUid
    ) {
        return rfidTagService.loginByRfid(tagUid);
    }
}