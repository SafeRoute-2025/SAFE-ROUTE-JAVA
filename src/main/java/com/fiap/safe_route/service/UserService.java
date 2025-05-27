package com.fiap.safe_route.service;

import com.fiap.safe_route.dto.user.UserRequest;
import com.fiap.safe_route.dto.user.UserResponse;
import com.fiap.safe_route.exception.EmailJaCadastradoException;
import com.fiap.safe_route.model.User;
import com.fiap.safe_route.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository repository) {
        this.repository = repository;
        this.encoder = new BCryptPasswordEncoder();
    }

    public UserResponse create(UserRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new EmailJaCadastradoException("Email já está em uso.");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(encoder.encode(request.password()))
                .phone(request.phone())
                .createdAt(new Date())
                .build();

        return toResponse(repository.save(user));
    }

    public List<UserResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public UserResponse findById(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        repository.deleteById(id);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getCreatedAt(),
                null
        );
    }

    public Page<UserResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    public UserResponse update(Long id, UserRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getEmail().equals(request.email()) && repository.existsByEmail(request.email())) {
            throw new EmailJaCadastradoException("Email já está em uso.");
        }

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(encoder.encode(request.password()));
        user.setPhone(request.phone());
        return toResponse(repository.save(user));
    }
}
