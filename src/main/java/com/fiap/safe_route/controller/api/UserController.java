package com.fiap.safe_route.controller.api;

import com.fiap.safe_route.dto.user.LoginRequest;
import com.fiap.safe_route.dto.user.UserRequest;
import com.fiap.safe_route.dto.user.UserResponse;
import com.fiap.safe_route.repository.UserRepository;
import com.fiap.safe_route.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "User API")
public class UserController {

    private final UserService service;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserController(UserService service, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Create a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping
    public ResponseEntity<UserResponse> create(
            @RequestBody @Valid UserRequest request) {
        try {
            UserResponse response = service.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get all users (paginated)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retrieved"),
            @ApiResponse(responseCode = "204", description = "No content")
    })
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @PageableDefault(size = 10, sort = "name") Pageable pageable
    ) {
            Page<UserResponse> users = service.findAll(pageable);
            if (users.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            Page<UserResponse> usersWithLinks = users.map(user -> {
                Link link = linkTo(methodOn(UserController.class).getUserById(
                        user.getId())).withSelfRel();
                user.setLink(link);
                return user;
            });
            return ResponseEntity.ok(usersWithLinks);
    }


    @Operation(summary = "Get user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        try {
            UserResponse user = service.findById(id);
            Link selfLink = linkTo(methodOn(UserController.class).getUserById(
                    id)).withSelfRel();
            user.setLink(selfLink);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(summary = "Delete user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                   @RequestBody
                                                   @Valid UserRequest request
    ) {
        try {
            UserResponse updatedUser = service.update(id, request);
            Link selfLink = linkTo(methodOn(UserController.class).getUserById(
                    id)).withSelfRel();
            updatedUser.setLink(selfLink);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userRepository.findByEmail(request.email())
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
                .map(user -> ResponseEntity.ok().build())
                .orElse(ResponseEntity.status(401).body("Credenciais inválidas"));
    }
}
