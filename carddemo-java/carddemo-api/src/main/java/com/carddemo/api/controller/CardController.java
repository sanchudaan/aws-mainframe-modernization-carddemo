package com.carddemo.api.controller;

import com.carddemo.api.dto.CardDto;
import com.carddemo.core.card.entity.Card;
import com.carddemo.core.card.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for Card operations - replaces CICS transactions:
 * - CCLI (Card List) -> GET /api/cards
 * - CCRD (Card View) -> GET /api/cards/{cardNumber}
 * - CCUP (Card Update) -> PUT /api/cards/{cardNumber}
 */
@RestController
@RequestMapping("/api/cards")
@Tag(name = "Cards", description = "Card management APIs")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    @Operation(summary = "List all cards", description = "Replaces CICS transaction CCLI")
    public ResponseEntity<Page<CardDto>> listCards(Pageable pageable) {
        Page<CardDto> cards = cardService.findAll(pageable).map(CardDto::from);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/{cardNumber}")
    @Operation(summary = "Get card by number", description = "Replaces CICS transaction CCRD")
    public ResponseEntity<CardDto> getCard(@PathVariable String cardNumber) {
        return cardService.findByCardNumber(cardNumber)
                .map(CardDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new card")
    public ResponseEntity<CardDto> createCard(@Valid @RequestBody CardDto cardDto, @RequestParam Long customerId) {
        Card card = cardDto.toEntity();
        Card created = cardService.create(card, customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(CardDto.from(created));
    }

    @PutMapping("/{cardNumber}")
    @Operation(summary = "Update card", description = "Replaces CICS transaction CCUP")
    public ResponseEntity<CardDto> updateCard(@PathVariable String cardNumber, @Valid @RequestBody CardDto cardDto) {
        Card card = cardDto.toEntity();
        card.setCardNumber(cardNumber);
        Card updated = cardService.update(card);
        return ResponseEntity.ok(CardDto.from(updated));
    }

    @DeleteMapping("/{cardNumber}")
    @Operation(summary = "Delete card")
    public ResponseEntity<Void> deleteCard(@PathVariable String cardNumber) {
        cardService.delete(cardNumber);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/account/{accountId}")
    @Operation(summary = "List cards by account ID")
    public ResponseEntity<Page<CardDto>> listCardsByAccountId(@PathVariable Long accountId, Pageable pageable) {
        Page<CardDto> cards = cardService.findByAccountId(accountId, pageable).map(CardDto::from);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/account/{accountId}/active")
    @Operation(summary = "List active cards by account ID")
    public ResponseEntity<List<CardDto>> listActiveCardsByAccountId(@PathVariable Long accountId) {
        List<CardDto> cards = cardService.findActiveCardsByAccountId(accountId).stream()
                .map(CardDto::from)
                .toList();
        return ResponseEntity.ok(cards);
    }

    @PostMapping("/{cardNumber}/activate")
    @Operation(summary = "Activate card")
    public ResponseEntity<CardDto> activateCard(@PathVariable String cardNumber) {
        Card activated = cardService.activateCard(cardNumber);
        return ResponseEntity.ok(CardDto.from(activated));
    }

    @PostMapping("/{cardNumber}/deactivate")
    @Operation(summary = "Deactivate card")
    public ResponseEntity<CardDto> deactivateCard(@PathVariable String cardNumber) {
        Card deactivated = cardService.deactivateCard(cardNumber);
        return ResponseEntity.ok(CardDto.from(deactivated));
    }

    @GetMapping("/expiring")
    @Operation(summary = "List cards expiring within days")
    public ResponseEntity<List<CardDto>> listExpiringCards(@RequestParam(defaultValue = "30") int daysAhead) {
        List<CardDto> cards = cardService.findExpiringCards(daysAhead).stream()
                .map(CardDto::from)
                .toList();
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/expiring-between")
    @Operation(summary = "List cards expiring between dates")
    public ResponseEntity<List<CardDto>> listCardsExpiringBetween(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        List<CardDto> cards = cardService.findCardsExpiringBetween(startDate, endDate).stream()
                .map(CardDto::from)
                .toList();
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/count/active")
    @Operation(summary = "Count active cards")
    public ResponseEntity<Long> countActiveCards() {
        return ResponseEntity.ok(cardService.countActiveCards());
    }

    @GetMapping("/count/inactive")
    @Operation(summary = "Count inactive cards")
    public ResponseEntity<Long> countInactiveCards() {
        return ResponseEntity.ok(cardService.countInactiveCards());
    }

    @GetMapping("/customer/{customerId}/accounts")
    @Operation(summary = "Get account IDs for customer")
    public ResponseEntity<List<Long>> getAccountIdsByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(cardService.findAccountIdsByCustomerId(customerId));
    }

    @GetMapping("/account/{accountId}/customers")
    @Operation(summary = "Get customer IDs for account")
    public ResponseEntity<List<Long>> getCustomerIdsByAccountId(@PathVariable Long accountId) {
        return ResponseEntity.ok(cardService.findCustomerIdsByAccountId(accountId));
    }
}
