package by.starovoytov.userservice.dto;

import by.starovoytov.userservice.config.json.YearMonthDeserializer;
import by.starovoytov.userservice.config.json.YearMonthSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.CreditCardNumber;
import java.time.YearMonth;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardInfoDto {
    private Long id;

    @NotBlank(message = "Card number cannot be blank")
    @CreditCardNumber(message = "Card number is invalid")
    private String number;

    @NotBlank(message = "Card holder name cannot be blank")
    private String holder;

    @NotNull(message = "Expiration date must be provided")
    @Future(message = "Card expiration date must be in the future")
    @JsonSerialize(using = YearMonthSerializer.class)
    @JsonDeserialize(using = YearMonthDeserializer.class)
    private YearMonth expirationDate;

    private Long userId;
}