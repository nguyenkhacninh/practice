package io.tpi.currency.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceConfig {
    @Bean(name = "messageSource")
    public MessageSource messageSource() {
        MultipleMessageSource messageSource
                = new MultipleMessageSource();
        messageSource.setFileWildCard("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
