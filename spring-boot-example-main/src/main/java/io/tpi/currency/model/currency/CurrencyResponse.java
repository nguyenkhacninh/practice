package io.tpi.currency.model.currency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrencyResponse {
    private Long id;
    private String description;
    private String currency;
    private String updatedDateStr;
    private Float exchangeRate;
}
