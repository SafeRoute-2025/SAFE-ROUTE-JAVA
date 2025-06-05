package com.fiap.safe_route.service;

import com.fiap.safe_route.dto.user.UserRequest;
import com.fiap.safe_route.dto.user.UserResponse;
import com.fiap.safe_route.exception.EmailJaCadastradoException;
import com.fiap.safe_route.model.User;
import com.fiap.safe_route.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        encoder = new BCryptPasswordEncoder();
    }

    @Test
    void shouldCreateUserSuccessfully() {
        UserRequest request = new UserRequest("Test", "test@example.com", "Senha123!", "11999999999");

        when(repository.existsByEmail(request.email())).thenReturn(false);
        when(repository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        UserResponse response = service.create(request);

        assertNotNull(response);
        assertEquals("Test", response.getName());
        assertEquals("test@example.com", response.getEmail());
        verify(repository).save(any(User.class));
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        UserRequest request = new UserRequest("Test", "test@example.com", "Senha123!", "11999999999");
        when(repository.existsByEmail(request.email())).thenReturn(true);

        assertThrows(EmailJaCadastradoException.class, () -> service.create(request));
    }

    @Test
    void shouldReturnUserById() {
        User user = User.builder().id(1L).name("Test").email("test@example.com").phone("11999999999").createdAt(new Date()).build();
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = service.findById(1L);

        assertNotNull(response);
        assertEquals("Test", response.getName());
    }

    @Test
    void shouldDeleteUser() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        assertDoesNotThrow(() -> service.delete(1L));
        verify(repository).deleteById(1L);
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        User existing = User.builder().id(1L).name("Old").email("old@example.com").password("123").phone("1111111111").createdAt(new Date()).build();
        UserRequest request = new UserRequest("New", "new@example.com", "Senha123!", "2222222222");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.existsByEmail(request.email())).thenReturn(false);
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = service.update(1L, request);

        assertNotNull(response);
        assertEquals("New", response.getName());
        assertEquals("new@example.com", response.getEmail());
    }

    @Test
    void shouldReturnAllUsers() {
        List<User> users = Arrays.asList(
                User.builder().id(1L).name("User1").email("u1@example.com").phone("1111111111").createdAt(new Date()).build(),
                User.builder().id(2L).name("User2").email("u2@example.com").phone("2222222222").createdAt(new Date()).build()
        );
        when(repository.findAll()).thenReturn(users);

        List<UserResponse> result = service.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnPagedUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = List.of(User.builder().id(1L).name("PagedUser").email("paged@example.com").createdAt(new Date()).build());
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(users));

        Page<UserResponse> page = service.findAll(pageable);

        assertEquals(1, page.getTotalElements());
    }
}
