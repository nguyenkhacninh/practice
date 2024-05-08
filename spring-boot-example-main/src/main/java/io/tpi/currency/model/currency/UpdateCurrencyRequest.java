package io.tpi.currency.model.currency;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCurrencyRequest {
    private String description;
    @NotEmpty
    private String currency;
    @NotNull
    private Float exchangeRate;
}
