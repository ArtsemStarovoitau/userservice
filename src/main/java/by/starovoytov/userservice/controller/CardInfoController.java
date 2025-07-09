package by.starovoytov.userservice.controller;

import by.starovoytov.userservice.dto.CardInfoDto;
import by.starovoytov.userservice.service.CardInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardInfoController {

    private final CardInfoService cardInfoService;

    @PostMapping
    public ResponseEntity<CardInfoDto> createCard(@Valid @RequestBody CardInfoDto cardInfoDto) {
        CardInfoDto createdCard = cardInfoService.createCard(cardInfoDto);
        return new ResponseEntity<>(createdCard, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardInfoDto> getCardById(@PathVariable Long id) {
        return ResponseEntity.ok(cardInfoService.getCardById(id));
    }

    @GetMapping
    public ResponseEntity<List<CardInfoDto>> getCardsByIds(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(cardInfoService.getCardsByIds(ids));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardInfoDto> updateCard(@PathVariable Long id, @Valid @RequestBody CardInfoDto cardInfoDto) {
        return ResponseEntity.ok(cardInfoService.updateCard(id, cardInfoDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardInfoService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }
}