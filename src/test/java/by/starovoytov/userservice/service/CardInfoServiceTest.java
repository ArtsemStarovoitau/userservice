package by.starovoytov.userservice.service;

import by.starovoytov.userservice.dto.CardInfoDto;
import by.starovoytov.userservice.mapper.CardInfoMapper;
import by.starovoytov.userservice.model.CardInfo;
import by.starovoytov.userservice.model.User;
import by.starovoytov.userservice.repository.CardInfoRepository;
import by.starovoytov.userservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardInfoServiceTest {

    @Mock
    private CardInfoRepository cardInfoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardInfoMapper cardInfoMapper;

    @InjectMocks
    private CardInfoService cardInfoService;

    @Test
    void createCard_whenUserExists_shouldReturnCardDto() {
        // Arrange
        Long userId = 1L;
        CardInfoDto inputDto = new CardInfoDto();
        inputDto.setUserId(userId);
        User user = new User();
        user.setId(userId);
        CardInfo cardToSave = new CardInfo();
        CardInfo savedCard = new CardInfo();
        savedCard.setId(10L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cardInfoMapper.toEntity(inputDto)).thenReturn(cardToSave);
        when(cardInfoRepository.save(cardToSave)).thenReturn(savedCard);
        when(cardInfoMapper.toDto(savedCard)).thenReturn(new CardInfoDto(10L, null, null, null, userId));

        // Act
        CardInfoDto result = cardInfoService.createCard(inputDto);

        // Assert
        assertThat(result.getId()).isEqualTo(10L);
        assertThat(cardToSave.getUser()).isEqualTo(user); // Проверяем, что пользователь был установлен
        verify(cardInfoRepository).save(cardToSave);
    }

    @Test
    void createCard_whenUserDoesNotExist_shouldThrowException() {
        // Arrange
        Long userId = 99L;
        CardInfoDto inputDto = new CardInfoDto();
        inputDto.setUserId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> cardInfoService.createCard(inputDto));
    }

    @Test
    void getCardById_whenCardExists_shouldReturnCardDto() {
        // Arrange
        Long cardId = 1L;
        CardInfo card = new CardInfo();
        card.setId(cardId);
        CardInfoDto expectedDto = new CardInfoDto();
        expectedDto.setId(cardId);

        when(cardInfoRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(cardInfoMapper.toDto(card)).thenReturn(expectedDto);

        // Act
        CardInfoDto result = cardInfoService.getCardById(cardId);

        // Assert
        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    void getCardById_whenCardDoesNotExist_shouldThrowException() {
        // Arrange
        Long cardId = 99L;
        when(cardInfoRepository.findById(cardId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> cardInfoService.getCardById(cardId));
    }
    
    @Test
    void getCardsByIds_shouldReturnListOfCardDtos() {
        // Arrange
        Long cardId = 1L;
        List<Long> ids = Collections.singletonList(cardId);
        CardInfo card = new CardInfo();
        card.setId(cardId);
        CardInfoDto cardDto = new CardInfoDto();
        cardDto.setId(cardId);

        when(cardInfoRepository.findAllById(ids)).thenReturn(Collections.singletonList(card));
        when(cardInfoMapper.toDto(card)).thenReturn(cardDto);

        // Act
        List<CardInfoDto> result = cardInfoService.getCardsByIds(ids);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(cardId);
    }

    @Test
    void updateCard_whenCardExists_shouldReturnUpdatedDto() {
        // Arrange
        Long cardId = 1L;
        CardInfoDto updateDto = new CardInfoDto();
        updateDto.setHolder("Jane Doe");
        CardInfo existingCard = new CardInfo();
        existingCard.setId(cardId);

        when(cardInfoRepository.findById(cardId)).thenReturn(Optional.of(existingCard));
        when(cardInfoRepository.save(existingCard)).thenReturn(existingCard);
        when(cardInfoMapper.toDto(existingCard)).thenReturn(updateDto);

        // Act
        CardInfoDto result = cardInfoService.updateCard(cardId, updateDto);

        // Assert
        assertThat(result.getHolder()).isEqualTo("Jane Doe");
        verify(cardInfoMapper).updateCardInfoFromDto(updateDto, existingCard);
        verify(cardInfoRepository).save(existingCard);
    }
    
    @Test
    void updateCard_whenCardDoesNotExist_shouldThrowException() {
        // Arrange
        Long cardId = 99L;
        when(cardInfoRepository.findById(cardId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> cardInfoService.updateCard(cardId, new CardInfoDto()));
    }

    @Test
    void deleteCard_whenCardExists_shouldCallDelete() {
        // Arrange
        Long cardId = 1L;
        when(cardInfoRepository.existsById(cardId)).thenReturn(true);

        // Act
        cardInfoService.deleteCard(cardId);

        // Assert
        verify(cardInfoRepository).deleteById(cardId);
    }

    @Test
    void deleteCard_whenCardDoesNotExist_shouldThrowException() {
        // Arrange
        Long cardId = 99L;
        when(cardInfoRepository.existsById(cardId)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> cardInfoService.deleteCard(cardId));
        verify(cardInfoRepository, never()).deleteById(cardId);
    }
}