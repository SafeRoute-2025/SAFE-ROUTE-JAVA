package com.fiap.safe_route.controller;

import com.fiap.safe_route.controller.api.UserController;
import com.fiap.safe_route.dto.user.UserRequest;
import com.fiap.safe_route.dto.user.UserResponse;
import com.fiap.safe_route.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateUser() {
        UserRequest request = new UserRequest("Alice", "alice@example.com", "Senha@123", "11999999999");
        UserResponse response = new UserResponse(1L, "Alice", "alice@example.com", "11999999999", new Date(), null);

        when(userService.create(request)).thenReturn(response);

        ResponseEntity<UserResponse> result = userController.create(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldGetAllUsersPaginated() {
        UserResponse user = new UserResponse(1L, "Alice", "alice@example.com", "11999999999", new Date(), null);
        Page<UserResponse> page = new PageImpl<>(List.of(user));

        when(userService.findAll(any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<UserResponse>> result = userController.getAllUsers(PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertFalse(result.getBody().isEmpty());
    }

    @Test
    void shouldReturnNoContentWhenUserListEmpty() {
        when(userService.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        ResponseEntity<Page<UserResponse>> result = userController.getAllUsers(PageRequest.of(0, 10));

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void shouldGetUserById() {
        UserResponse user = new UserResponse(1L, "Alice", "alice@example.com", "11999999999", new Date(), null);
        when(userService.findById(1L)).thenReturn(user);

        ResponseEntity<UserResponse> result = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(user, result.getBody());
    }

    @Test
    void shouldReturnNotFoundForMissingUser() {
        when(userService.findById(999L)).thenThrow(new RuntimeException());

        ResponseEntity<UserResponse> result = userController.getUserById(999L);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void shouldDeleteUser() {
        doNothing().when(userService).delete(1L);

        ResponseEntity<Void> result = userController.deleteUser(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(userService).delete(1L);
    }

    @Test
    void shouldUpdateUser() {
        UserRequest request = new UserRequest("Alice", "alice@example.com", "Senha@123", "11999999999");
        UserResponse updated = new UserResponse(1L, "Alice", "alice@example.com", "11999999999", new Date(), null);

        when(userService.update(eq(1L), eq(request))).thenReturn(updated);

        ResponseEntity<UserResponse> result = userController.updateUser(1L, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(updated, result.getBody());
    }

    @Test
    void shouldReturnBadRequestWhenUpdateFails() {
        UserRequest request = new UserRequest("Alice", "alice@example.com", "Senha@123", "11999999999");
        when(userService.update(eq(1L), eq(request))).thenThrow(new RuntimeException());

        ResponseEntity<UserResponse> result = userController.updateUser(1L, request);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
}
