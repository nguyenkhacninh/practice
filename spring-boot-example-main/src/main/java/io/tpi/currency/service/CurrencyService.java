package io.tpi.currency.service;

import io.tpi.currency.constant.ErrorCode;
import io.tpi.currency.exception.ServiceException;
import io.tpi.currency.repository.model.Currency;
import io.tpi.currency.model.currency.CurrencyResponse;
import io.tpi.currency.model.currency.CurrenciesResponse;
import io.tpi.currency.model.currency.CreateCurrencyRequest;
import io.tpi.currency.model.currency.UpdateCurrencyRequest;
import io.tpi.currency.repository.CurrencyRepository;
import io.tpi.currency.util.DateTimesUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public CurrenciesResponse getCurrencies() {
        return new CurrenciesResponse(currencyRepository.findAll().stream()
                .map(this::currencyResponse)
                .sorted(Comparator.comparing(CurrencyResponse::getCurrency))
                .collect(toList()));
    }

    public CurrencyResponse getCurrencyById(long id) {
        return currencyResponse(findExistingCurrency(id));
    }
    @Transactional(rollbackOn = Exception.class)
    public CurrencyResponse createCurrency(CreateCurrencyRequest createCurrencyRequest) {
        Optional<Currency> optional = currencyRepository.findByCurrency(createCurrencyRequest.getCurrency());
        if (optional.isPresent()) {
            throw new ServiceException(ErrorCode.CURRENCY_DUPLICATE, optional.get().getCurrency());
        }
        Currency currency = currencyRepository.save(Currency.builder()
                .description(createCurrencyRequest.getDescription())
                .currency(createCurrencyRequest.getCurrency())
                .exchangeRate(createCurrencyRequest.getExchangeRate())
                .updatedDateStr(DateTimesUtils.convertDateToString(new Date()))
                .build());
        return currencyResponse(currency);
    }
    @Transactional(rollbackOn = Exception.class)
    public CurrencyResponse updateCurrency(long id, UpdateCurrencyRequest updateCurrencyRequest) {
        Currency existingCurrency = findExistingCurrency(id);

        Currency updateCurrency = currencyRepository.save(
                existingCurrency.toBuilder()
                        .currency(updateCurrencyRequest.getCurrency())
                        .description(updateCurrencyRequest.getDescription())
                        .exchangeRate(updateCurrencyRequest.getExchangeRate())
                        .updatedDateStr(DateTimesUtils.convertDateToString(new Date()))
                        .build()
        );

        return currencyResponse(updateCurrency);
    }
    @Transactional(rollbackOn = Exception.class)
    public void deleteCurrency(long id) {
        currencyRepository.delete(findExistingCurrency(id));
    }

    private Currency findExistingCurrency(long id) {

        return currencyRepository.findById(id).orElseThrow(() -> {
            log.error(" currency with id not found {}", id);
            // i18N
            throw new ServiceException(ErrorCode.CURRENCY_NOT_EXIST, id);
        });
    }

    private CurrencyResponse currencyResponse(Currency currency) {
        return CurrencyResponse.builder()
                .id(currency.getId())
                .currency(currency.getCurrency())
                .exchangeRate(currency.getExchangeRate())
                .description(currency.getDescription())
                .updatedDateStr(currency.getUpdatedDateStr())
                .build();
    }
}
