package com.topkey.api.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JdeDateConverterUtil {

    public static String convertToJdeDate(String date) {
        // 假设输入日期格式为 "yyyy-MM-dd"
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(date, inputFormatter);

            // JDE日期计算
            int jdeStartYear = 1900;
            int jdeCentury = (localDate.getYear() / 100) * 100;
            int jdeYear = localDate.getYear() - jdeCentury + (jdeCentury - jdeStartYear);
            int dayOfYear = localDate.getDayOfYear();

            return String.format("%d%03d", jdeYear, dayOfYear);
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format", e);
        }
    }
}

