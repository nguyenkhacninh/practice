package io.tpi.currency.model.currency;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CurrencyPopulatorItem {
    private String code;
    private String symbol;
    private String rate;
    private Float rateFloat;
}
