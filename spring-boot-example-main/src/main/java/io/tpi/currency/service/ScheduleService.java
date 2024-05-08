package io.tpi.currency.service;
import io.tpi.currency.populator.CurrencyDatabasePopulator;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Setter
@Component
@Slf4j
public class ScheduleService {
    @Autowired
    private CurrencyDatabasePopulator currencyDatabasePopulator;

    @Scheduled(cron = "${demo.config.schedule.update-rate}")
    public void updateExchangeRate() throws Exception {
        log.info(">>>>>>>>>>>>> Starting schedule update exchange rate");
        currencyDatabasePopulator.synExchangeRate();
    }

}
