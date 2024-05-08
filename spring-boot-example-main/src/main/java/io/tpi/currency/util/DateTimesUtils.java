package io.tpi.currency.util;

import lombok.SneakyThrows;

import org.apache.commons.lang3.time.DateUtils;

import java.text.Format;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class DateTimesUtils {
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";

    private DateTimesUtils() {
    }

    public static String convertDateToString(Date date) {
        Format f = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
        return f.format(date);
    }
}
