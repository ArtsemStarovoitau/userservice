package by.starovoytov.userservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;
import java.time.YearMonth;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Data
public class CardInfoDto {
    private Long id;

    @NotBlank(message = "Card number cannot be blank")
    @CreditCardNumber(message = "Card number is invalid")
    private String number;

    @NotBlank(message = "Card holder name cannot be blank")
    private String holder;

    @NotNull(message = "Expiration date must be provided")
    @Future(message = "Card expiration date must be in the future")
    private YearMonth expirationDate;
    
    @JsonIgnore 
    private Long userId;
}