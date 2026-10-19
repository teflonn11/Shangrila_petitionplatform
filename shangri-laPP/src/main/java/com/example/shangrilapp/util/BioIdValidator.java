
package com.example.shangrilapp.util;

import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Component
public class BioIdValidator {

    private static final Pattern BIOID_PATTERN = Pattern.compile("^[A-Z0-9]{10}$");
    private static final Set<String> VALID_BIOIDS = new HashSet<>();

    public BioIdValidator() {

        VALID_BIOIDS.add("K1YL8VA2HG");
        VALID_BIOIDS.add("7DMPYAZAP2");
        VALID_BIOIDS.add("D05HPPQNJ4");
        VALID_BIOIDS.add("2WYIM3QCK9");
        VALID_BIOIDS.add("DHKFIYHMAZ");
        VALID_BIOIDS.add("LZK7P0X0LQ");
        VALID_BIOIDS.add("H5C98XCENC");
        VALID_BIOIDS.add("6X6I6TSUFG");
        VALID_BIOIDS.add("QTLCWUS8NB");
        VALID_BIOIDS.add("Y4FC3F9ZGS");
    }

    public boolean isValid(String bioId) {
        if (bioId == null) {
            return false;
        }


        Matcher matcher = BIOID_PATTERN.matcher(bioId);
        if (!matcher.matches()) {
            return false;
        }


        return VALID_BIOIDS.contains(bioId);
    }

    public String extractBioIdFromQR(String qrContent) {
        if (qrContent == null) {
            return null;
        }

        String cleanBioId = qrContent.trim();
        return isValid(cleanBioId) ? cleanBioId : null;
    }
}