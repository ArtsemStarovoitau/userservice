package by.starovoytov.userservice.service;

import by.starovoytov.userservice.dto.UserDto;
import by.starovoytov.userservice.mapper.UserMapper;
import by.starovoytov.userservice.model.User;
import by.starovoytov.userservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_shouldReturnSavedUserDto() {
        // Arrange
        UserDto inputDto = new UserDto(null, "John", "Doe", LocalDate.now(), "john.doe@test.com", null);
        User userToSave = new User();
        User savedUser = new User();
        savedUser.setId(1L);

        when(userMapper.toEntity(inputDto)).thenReturn(userToSave);
        when(userRepository.save(userToSave)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(new UserDto(1L, "John", "Doe", LocalDate.now(), "john.doe@test.com", null));

        // Act
        UserDto result = userService.createUser(inputDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(userRepository).save(userToSave);
    }

    @Test
    void getUserById_whenUserExists_shouldReturnUserDto() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        UserDto expectedDto = new UserDto();
        expectedDto.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(expectedDto);

        // Act
        UserDto result = userService.getUserById(userId);

        // Assert
        assertThat(result).isEqualTo(expectedDto);
        verify(userRepository).findById(userId);
    }

    @Test
    void getUserById_whenUserDoesNotExist_shouldThrowException() {
        // Arrange
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void getUsersByIds_shouldReturnListOfUserDtos() {
        // Arrange
        Long userId = 1L;
        List<Long> ids = Collections.singletonList(userId);
        User user = new User();
        user.setId(userId);
        UserDto userDto = new UserDto();
        userDto.setId(userId);

        when(userRepository.findAllById(ids)).thenReturn(Collections.singletonList(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Act
        List<UserDto> result = userService.getUsersByIds(ids);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(userId);
    }

    @Test
    void getUserByEmail_whenUserExists_shouldReturnUserDto() {
        // Arrange
        String email = "test@test.com";
        User user = new User();
        user.setEmail(email);
        UserDto expectedDto = new UserDto();
        expectedDto.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(expectedDto);

        // Act
        UserDto result = userService.getUserByEmail(email);

        // Assert
        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    void getUserByEmail_whenUserDoesNotExist_shouldThrowException() {
        // Arrange
        String email = "notfound@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.getUserByEmail(email));
    }

    @Test
    void updateUser_whenUserExists_shouldReturnUpdatedDto() {
        // Arrange
        Long userId = 1L;
        UserDto updateDto = new UserDto();
        updateDto.setName("Jane");
        User existingUser = new User();
        existingUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(userMapper.toDto(existingUser)).thenReturn(updateDto);

        // Act
        UserDto result = userService.updateUser(userId, updateDto);

        // Assert
        assertThat(result.getName()).isEqualTo("Jane");
        verify(userMapper).updateUserFromDto(updateDto, existingUser);
        verify(userRepository).save(existingUser);
    }

    @Test
    void updateUser_whenUserDoesNotExist_shouldThrowException() {
        // Arrange
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(userId, new UserDto()));
    }

    @Test
    void deleteUser_shouldCallDeleteById() {
        // Arrange
        Long userId = 1L;

        // Act
        userService.deleteUser(userId);

        // Assert
        // Проверяем, что метод deleteById был вызван ровно 1 раз с правильным ID
        verify(userRepository, times(1)).deleteById(userId);
    }
}