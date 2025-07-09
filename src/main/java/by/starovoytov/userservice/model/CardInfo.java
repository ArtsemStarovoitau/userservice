package by.starovoytov.userservice.model;

import by.starovoytov.userservice.converter.YearMonthAttributeConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.YearMonth;

@Data
@Entity
@Table(name = "card_info", indexes = { @Index(name = "idx_cardinfo_user_id", columnList = "user_id") })
public class CardInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private String holder;
    
    @Convert(converter = YearMonthAttributeConverter.class)
    @Column(name = "expiration_date", nullable = false)
    private YearMonth expirationDate;
}