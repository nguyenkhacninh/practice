package io.tpi.currency.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import java.util.Locale;


@Component
public class MessageSourceUtils {

    private static MessageSource messageSource;
    private static Locale localeVN = new Locale("vi", "VN");
    private static Locale localeEN = new Locale("en", "US");
    private static String locale = "vi";

    @Autowired
    public void setMessageSource(MessageSource messageSource, @Value("${demo.locale.language:vi}") String locale) {
        this.locale = locale;
        MessageSourceUtils.messageSource = messageSource;
    }

    public static String getMessage(String key, Object... args) {
        try {
            return messageSource.getMessage(key, args, "en".equalsIgnoreCase(locale) ? localeEN : localeVN);
        } catch (Exception ex) {
            return key;
        }
    }

}
