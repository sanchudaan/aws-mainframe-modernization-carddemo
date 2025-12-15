package com.carddemo.core.card.service;

import com.carddemo.core.card.entity.Card;
import com.carddemo.core.card.entity.CardCrossReference;
import com.carddemo.core.card.repository.CardCrossReferenceRepository;
import com.carddemo.core.card.repository.CardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service for Card operations - replaces COBOL programs:
 * - COCRDLIC.cbl (Card List)
 * - COCRDSLC.cbl (Card Select)
 * - COCRDUPC.cbl (Card Update)
 */
@Service
@Transactional
public class CardService {

    private final CardRepository cardRepository;
    private final CardCrossReferenceRepository crossReferenceRepository;

    public CardService(CardRepository cardRepository, CardCrossReferenceRepository crossReferenceRepository) {
        this.cardRepository = cardRepository;
        this.crossReferenceRepository = crossReferenceRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Card> findByCardNumber(String cardNumber) {
        return cardRepository.findById(cardNumber);
    }

    @Transactional(readOnly = true)
    public Page<Card> findAll(Pageable pageable) {
        return cardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Card> findByAccountId(Long accountId) {
        return cardRepository.findByAccountId(accountId);
    }

    @Transactional(readOnly = true)
    public Page<Card> findByAccountId(Long accountId, Pageable pageable) {
        return cardRepository.findByAccountId(accountId, pageable);
    }

    @Transactional(readOnly = true)
    public List<Card> findActiveCardsByAccountId(Long accountId) {
        return cardRepository.findActiveCardsByAccountId(accountId);
    }

    public Card save(Card card) {
        return cardRepository.save(card);
    }

    public Card create(Card card, Long customerId) {
        if (card.getCardNumber() != null && cardRepository.existsById(card.getCardNumber())) {
            throw new IllegalArgumentException("Card already exists with number: " + card.getCardNumber());
        }
        
        Card savedCard = cardRepository.save(card);
        
        CardCrossReference crossRef = new CardCrossReference(
                card.getCardNumber(),
                customerId,
                card.getAccountId()
        );
        crossReferenceRepository.save(crossRef);
        
        return savedCard;
    }

    public Card update(Card card) {
        if (card.getCardNumber() == null || !cardRepository.existsById(card.getCardNumber())) {
            throw new IllegalArgumentException("Card not found with number: " + card.getCardNumber());
        }
        return cardRepository.save(card);
    }

    public void delete(String cardNumber) {
        crossReferenceRepository.deleteById(cardNumber);
        cardRepository.deleteById(cardNumber);
    }

    public Card activateCard(String cardNumber) {
        Card card = cardRepository.findById(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card not found: " + cardNumber));
        
        card.setActiveStatus("Y");
        return cardRepository.save(card);
    }

    public Card deactivateCard(String cardNumber) {
        Card card = cardRepository.findById(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card not found: " + cardNumber));
        
        card.setActiveStatus("N");
        return cardRepository.save(card);
    }

    @Transactional(readOnly = true)
    public List<Card> findExpiringCards(int daysAhead) {
        LocalDate futureDate = LocalDate.now().plusDays(daysAhead);
        return cardRepository.findExpiringCardsBefore(futureDate);
    }

    @Transactional(readOnly = true)
    public List<Card> findCardsExpiringBetween(LocalDate startDate, LocalDate endDate) {
        return cardRepository.findCardsExpiringBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public long countActiveCards() {
        return cardRepository.countByActiveStatus("Y");
    }

    @Transactional(readOnly = true)
    public long countInactiveCards() {
        return cardRepository.countByActiveStatus("N");
    }

    @Transactional(readOnly = true)
    public Optional<CardCrossReference> findCrossReference(String cardNumber) {
        return crossReferenceRepository.findById(cardNumber);
    }

    @Transactional(readOnly = true)
    public List<CardCrossReference> findCrossReferencesByCustomerId(Long customerId) {
        return crossReferenceRepository.findByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    public List<CardCrossReference> findCrossReferencesByAccountId(Long accountId) {
        return crossReferenceRepository.findByAccountId(accountId);
    }

    @Transactional(readOnly = true)
    public List<Long> findAccountIdsByCustomerId(Long customerId) {
        return crossReferenceRepository.findDistinctAccountIdsByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    public List<Long> findCustomerIdsByAccountId(Long accountId) {
        return crossReferenceRepository.findDistinctCustomerIdsByAccountId(accountId);
    }
}
