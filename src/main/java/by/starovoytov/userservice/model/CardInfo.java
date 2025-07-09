package by.starovoytov.userservice.model;

import by.starovoytov.userservice.converter.YearMonthAttributeConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.YearMonth;

import org.hibernate.validator.constraints.CreditCardNumber;


@Data
@Entity
@Table(name = "card_info", indexes = {
    @Index(name = "idx_cardinfo_user_id", columnList = "user_id")
})
public class CardInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    @NotBlank(message = "Card number cannot be blank")
    @CreditCardNumber(message = "Card number is invalid")
    @Column(nullable = false)
    private String number;

    @NotBlank(message = "Card holder name cannot be blank")
    @Column(nullable = false)
    private String holder;

    @NotNull(message = "Expiration date must be provided")
    @Future(message = "Card expiration date must be in the future")
    @Convert(converter = YearMonthAttributeConverter.class)
    @Column(name = "expiration_date", nullable = false)
    private YearMonth expirationDate;
}