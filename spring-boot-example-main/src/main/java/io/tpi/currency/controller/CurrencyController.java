package io.tpi.currency.controller;

import io.tpi.currency.model.currency.CurrencyResponse;
import io.tpi.currency.model.currency.UpdateCurrencyRequest;
import io.tpi.currency.service.CurrencyService;
import io.tpi.currency.model.currency.CurrenciesResponse;
import io.tpi.currency.model.currency.CreateCurrencyRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public CurrenciesResponse getCurrencies() {
        log.info("getting all currencies");

        return currencyService.getCurrencies();
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CurrencyResponse> getCurrencyById(@PathVariable long id) {
        log.info("getting currency with id {}", id);

        return ResponseEntity.ok(currencyService.getCurrencyById(id));
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public CurrencyResponse createCurrency(@Valid @RequestBody CreateCurrencyRequest createCurrencyRequest) {
        log.info("creating currency {}", createCurrencyRequest);

        CurrencyResponse currency = currencyService.createCurrency(createCurrencyRequest);

        log.info("currency created, currency id {}", currency.getId());

        return currency;
    }

    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateCurrency(@PathVariable long id, @Valid @RequestBody UpdateCurrencyRequest updateCurrencyRequest) {
        log.info("updating currency with id {}: {}", id, updateCurrencyRequest);

        currencyService.updateCurrency(id, updateCurrencyRequest);

        log.info("currency updated, currency id {}", id);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable long id) {
        log.info("deleting currency with id {}", id);

        currencyService.deleteCurrency(id);

        log.info("currency deleted, currency id {}", id);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
