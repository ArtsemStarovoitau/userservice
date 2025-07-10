package by.starovoytov.userservice.service;

import by.starovoytov.userservice.dto.UserDto;
import by.starovoytov.userservice.mapper.UserMapper;
import by.starovoytov.userservice.model.User;
import by.starovoytov.userservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Caching(put = {
        @CachePut(value = "users", key = "#result.id"),
        @CachePut(value = "usersByEmail", key = "#result.email")
    })
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#id")
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getUsersByIds(List<Long> ids) {
        return userRepository.findAllById(ids).stream()
            .map(userMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "usersByEmail", key = "#email")
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
        return userMapper.toDto(user);
    }


    @Transactional
    @Caching(put = {
        @CachePut(value = "users", key = "#id"),
        @CachePut(value = "usersByEmail", key = "#result.email")
    })
    public UserDto updateUser(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        
        userMapper.updateUserFromDto(userDto, existingUser);
        
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "users", key = "#id"),
        @CacheEvict(value = "usersByEmail", key = "T(by.starovoytov.userservice.service.UserService).findEmailById(#id, #root.target)")
    })
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public static String findEmailById(Long id, Object target) {
        UserService service = (UserService) target;
        try {
            return service.userRepository.findById(id).map(User::getEmail).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
}