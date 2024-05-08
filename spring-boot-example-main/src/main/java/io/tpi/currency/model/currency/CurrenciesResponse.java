package io.tpi.currency.model.currency;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class CurrenciesResponse {
    private List<CurrencyResponse> currencies;
}
