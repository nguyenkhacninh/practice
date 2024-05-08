package io.tpi.currency.controller;

import io.tpi.currency.CurrencyServiceSpringTest;
import io.tpi.currency.repository.model.Currency;
import io.tpi.currency.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Map;

import static java.lang.Long.parseLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@CurrencyServiceSpringTest
class CurrencyControllerTest {

    public static final int UNKNOWN_CURRENCY_ID = 9999999;

    @Autowired
    private WebTestClient httpClient;

    @Autowired
    private CurrencyRepository currencyRepository;

    @BeforeEach
    void deleteAllCurrencyFromDatabase() {
        currencyRepository.deleteAllInBatch();
    }


    @Nested
    public class GetCurrencies {

        @Test
        void returnsNoCurrenciesWhenNoCurrenciesArePresentInDatabase() {
            getCurrenciesRequest()
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(APPLICATION_JSON)
                    .expectBody()
                    .json("{\"currencies\":[]}");
        }

        @Test
        void returnsCurrenciesOrderedByCurrencyWhenCurrenciesArePresentInDatabase() {

            long id2 = givenTheFollowingCurrencyExistsInDatabase(aCurrency().currency("x2").description(null).build()).getId();
            long id3 = givenTheFollowingCurrencyExistsInDatabase(aCurrency().currency("x3").description(null).build()).getId();
            long id1 = givenTheFollowingCurrencyExistsInDatabase(aCurrency().currency("x1").description(null).build()).getId();

            getCurrenciesRequest()
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(APPLICATION_JSON)
                    .expectBody()
                    //
                    .jsonPath("$.currencies[0].id").isEqualTo(id1)
                    .jsonPath("$.currencies[0].currency").isEqualTo("x1")
                    //
                    .jsonPath("$.currencies[1].id").isEqualTo(id2)
                    .jsonPath("$.currencies[1].currency").isEqualTo("x2")
                    //
                    .jsonPath("$.currencies[2].id").isEqualTo(id3)
                    .jsonPath("$.currencies[2].currency").isEqualTo("x3");
        }

        private WebTestClient.RequestHeadersSpec<?> getCurrenciesRequest() {
            return httpClient.get().uri("/currencies");
        }
    }

    @Nested
    public class GetCurrencyById {

        @Test
        void getCurrencyById() {

            long id = givenTheFollowingCurrencyExistsInDatabase(aCurrency().currency("x1").build()).getId();

            getCurrencyByIdRequest(id)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(APPLICATION_JSON)
                    .expectBody()
                    //
                    .jsonPath("$.id").isEqualTo(id)
                    .jsonPath("$.currency").isEqualTo("x1");
        }

        @Test
        void getCurrencyByIdReturnsErrorWhenCurrencyIsNotFound() {
            getCurrencyByIdRequest(UNKNOWN_CURRENCY_ID)
                    .exchange()
                    .expectStatus().is5xxServerError();
        }


        private WebTestClient.RequestHeadersSpec<?> getCurrencyByIdRequest(long id) {
            return httpClient.get().uri("/currencies/" + id);
        }
    }

    @Nested
    public class CreateCurrency {

        @Test
        void createCurrency() {

            createCurrencyRequest()
                    .contentType(APPLICATION_JSON)
                    .body(BodyInserters.fromValue(Map.of(
                            "currency", "x1",
                            "exchangeRate", 160,
                            "description", "new description x1"
                    )))
                    .exchange()
                    .expectStatus().isCreated()
                    .expectHeader().contentType(APPLICATION_JSON)
                    .expectBody()
                    //
                    .jsonPath("$.currency").isEqualTo("x1")
                    .jsonPath("$.exchangeRate").isEqualTo(160)
                    .jsonPath("$.id").value(id -> {
                        Currency savedCurrency = currencyRepository.findById(parseLong(id.toString())).get();
                        assertThat(savedCurrency.getCurrency())
                                .isEqualTo("x1");
                    });
        }


        @Test
        void createDuplicateCurrencyReturnError() {

            givenTheFollowingCurrencyExistsInDatabase(aCurrency().currency("x1").description("description").build()).getId();

            createCurrencyRequest()
                    .contentType(APPLICATION_JSON)
                    .body(BodyInserters.fromValue(Map.of(
                            "currency", "x1",
                            "exchangeRate", 160,
                            "description", "new description x1"
                    )))
                    .exchange()
                    .expectStatus().is5xxServerError();
        }

        private WebTestClient.RequestBodySpec createCurrencyRequest() {
            return httpClient.post().uri("/currencies");
        }
    }

    @Nested
    public class UpdateCurrency {

        @Test
        void updateCurrency() {

            long id = givenTheFollowingCurrencyExistsInDatabase(aCurrency().currency("x1").description("existing description x1").exchangeRate(160F).build()).getId();

            updateCurrencyRequest(id)
                    .contentType(APPLICATION_JSON)
                    .body(BodyInserters.fromValue(Map.of(
                            "currency", "x3",
                            "exchangeRate", 180,
                            "description", "updated description"
                    )))
                    .exchange()
                    .expectStatus().isNoContent()
                    .expectBody().isEmpty();

            Currency savedCurrency = currencyRepository.findById(id).get();
            assertThat(savedCurrency.getId()).isEqualTo(id);
            assertThat(savedCurrency.getCurrency()).isEqualTo("x3");
            assertThat(savedCurrency.getExchangeRate()).isEqualTo(180F);
            assertThat(savedCurrency.getDescription()).isEqualTo("updated description");
        }

        @Test
        void updateCurrencyReturnsErrorWhenCurrencyIsNotFound() {

            updateCurrencyRequest(UNKNOWN_CURRENCY_ID)
                    .contentType(APPLICATION_JSON)
                    .body(BodyInserters.fromValue(Map.of(
                            "currency", "x1",
                            "exchangeRate", 170,
                            "description", "updated description"
                    )))
                    .exchange()
                    .expectStatus().is5xxServerError();
        }


        private WebTestClient.RequestBodySpec updateCurrencyRequest(long id) {
            return httpClient.put().uri("/currencies/" + id);
        }
    }

    @Nested
    public class DeleteCurrency {

        @Test
        void deleteCurrency() {
            long id = givenTheFollowingCurrencyExistsInDatabase(aCurrency().currency("x1").description("existing description").build()).getId();

            deleteCurrencyRequest(id)
                    .exchange()
                    .expectStatus().isNoContent()
                    .expectBody().isEmpty();

            assertThat(currencyRepository.findById(id)).isEmpty();
        }

        @Test
        void deleteCurrencyReturnsErrorWhenCurrencyIsNotFound() {

            deleteCurrencyRequest(UNKNOWN_CURRENCY_ID)
                    .exchange()
                    .expectStatus().is5xxServerError();
        }

        private WebTestClient.RequestHeadersSpec<?> deleteCurrencyRequest(long id) {
            return httpClient.delete().uri("/currencies/" + id);
        }
    }

    private Currency givenTheFollowingCurrencyExistsInDatabase(Currency currency) {
        return currencyRepository.saveAndFlush(currency);
    }

    private Currency.CurrencyBuilder aCurrency() {
        return Currency.builder().id(0L);
    }

}
