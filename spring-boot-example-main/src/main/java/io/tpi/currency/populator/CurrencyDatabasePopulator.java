package io.tpi.currency.populator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.tpi.currency.repository.model.Currency;
import io.tpi.currency.repository.CurrencyRepository;
import io.tpi.currency.util.DateTimesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;

@Slf4j
@Component
public class CurrencyDatabasePopulator {
    private final String currenciesUrl;
    private final CurrencyRepository currencyRepository;

    public CurrencyDatabasePopulator(@Value("${currencies.populator.url}") String currenciesUrl, CurrencyRepository currencyRepository) {
        this.currenciesUrl = currenciesUrl;
        this.currencyRepository = currencyRepository;
    }

    public void synExchangeRate() throws Exception {
        log.info("populating currency database from {}", currenciesUrl);

        CurrencyPopulatorResponse currenciesToPopulate = new ObjectMapper().readValue(new URL(currenciesUrl), new TypeReference<>() {
        });

        currenciesToPopulate.bpi.forEach((currency, value) -> {
            Optional<Currency> optional = currencyRepository.findByCurrency(currency);
            if (optional.isPresent()) {
                Currency entity = optional.get();
                if (entity.getExchangeRate().floatValue() != value.rate_float.floatValue()) {
                    entity.setExchangeRate(value.rate_float);
                    entity.setUpdatedDateStr(DateTimesUtils.convertDateToString(currenciesToPopulate.time.updatedISO));
                    currencyRepository.saveAndFlush(entity);
                }
            }
        });

    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    private record CurrencyPopulatorResponse(TimeModel time, Map<String, CurrencyPopulatorItem> bpi) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CurrencyPopulatorItem(String code,
                                        String description,
                                        Float rate_float) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TimeModel(Date updatedISO) {
    }
}
