package com.iot.backend.service;

import com.iot.backend.dto.AuthResponse;
import com.iot.backend.dto.RegisterRfidRequest;
import com.iot.backend.dto.RfidTagResponse;
import com.iot.backend.model.RfidTag;
import com.iot.backend.model.User;
import com.iot.backend.repository.RfidTagRepository;
import com.iot.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RfidTagService {

    private final RfidTagRepository rfidTagRepository;
    private final UserRepository userRepository;

    public RfidTagService(
            RfidTagRepository rfidTagRepository,
            UserRepository userRepository
    ) {
        this.rfidTagRepository = rfidTagRepository;
        this.userRepository = userRepository;
    }

    public RfidTagResponse registerTag(RegisterRfidRequest request) {

        if (rfidTagRepository.findByTagUid(request.getTagUid()).isPresent()) {
            throw new RuntimeException("Esta etiqueta RFID ya está registrada");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow();

        RfidTag tag = new RfidTag();
        tag.setTagUid(request.getTagUid());
        tag.setTagName(request.getTagName());
        tag.setUser(user);
        tag.setActive(true);
        tag.setCreatedAt(LocalDateTime.now());

        RfidTag saved = rfidTagRepository.save(tag);

        return new RfidTagResponse(
                saved.getId(),
                saved.getTagUid(),
                saved.getTagName(),
                saved.getCreatedAt()
        );
    }

    public List<RfidTagResponse> getTagsByUser(Integer userId) {
        return rfidTagRepository.findByUserId(userId)
                .stream()
                .map(tag -> new RfidTagResponse(
                        tag.getId(),
                        tag.getTagUid(),
                        tag.getTagName(),
                        tag.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public AuthResponse loginByRfid(String tagUid) {

        RfidTag tag = rfidTagRepository.findByTagUid(tagUid)
                .orElse(null);

        if (tag == null || !tag.isActive()) {
            return new AuthResponse(
                    null,
                    null,
                    null,
                    false,
                    "Etiqueta RFID no registrada"
            );
        }

        User user = tag.getUser();

        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                true,
                "Acceso RFID correcto"
        );
    }
}