package by.starovoytov.userservice.service;

import by.starovoytov.userservice.dto.CardInfoDto;
import by.starovoytov.userservice.mapper.CardInfoMapper;
import by.starovoytov.userservice.model.CardInfo;
import by.starovoytov.userservice.model.User;
import by.starovoytov.userservice.repository.CardInfoRepository;
import by.starovoytov.userservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardInfoService {

    private final CardInfoRepository cardInfoRepository;
    private final UserRepository userRepository;
    private final CardInfoMapper cardInfoMapper;

    @Transactional
    public CardInfoDto createCard(CardInfoDto cardInfoDto) {
        User user = userRepository.findById(cardInfoDto.getUserId())
            .orElseThrow(() -> new EntityNotFoundException("Cannot create card. User not found with id: " + cardInfoDto.getUserId()));
        
        CardInfo cardInfo = cardInfoMapper.toEntity(cardInfoDto);
        cardInfo.setUser(user);
        
        CardInfo savedCard = cardInfoRepository.save(cardInfo);
        return cardInfoMapper.toDto(savedCard);
    }

    @Transactional(readOnly = true)
    public CardInfoDto getCardById(Long id) {
        CardInfo card = cardInfoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Card not found with id: " + id));
        return cardInfoMapper.toDto(card);
    }

    @Transactional(readOnly = true)
    public List<CardInfoDto> getCardsByIds(List<Long> ids) {
        return cardInfoRepository.findAllById(ids).stream()
            .map(cardInfoMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public CardInfoDto updateCard(Long id, CardInfoDto cardInfoDto) {
        CardInfo existingCard = cardInfoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Card not found with id: " + id));
        
        cardInfoMapper.updateCardInfoFromDto(cardInfoDto, existingCard);

        CardInfo updatedCard = cardInfoRepository.save(existingCard);
        return cardInfoMapper.toDto(updatedCard);
    }

    @Transactional
    public void deleteCard(Long id) {
        if (!cardInfoRepository.existsById(id)) {
            throw new EntityNotFoundException("Card not found with id: " + id);
        }
        cardInfoRepository.deleteById(id);
    }
}