package com.iot.backend.controller;

import com.iot.backend.model.User;
import com.iot.backend.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.iot.backend.dto.UserStatsResponse;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<User> getAll() {
        return service.getAll();
    }

    @GetMapping("/stats")
    public List<UserStatsResponse> getUsersStats() {
        return service.getUsersStats();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PostMapping
    public User create(@RequestBody User item) {
        return service.save(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
