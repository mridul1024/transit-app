package com.example.gaijinsmash.transitapp.internet;

import com.example.gaijinsmash.transitapp.internet.validator.EmailValidator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Created by ryanj on 8/10/2017.
 */

public class EmailValidatorTest {

    @Test
    public void emailValidator_IfCorrect_ReturnsTrue() {
        EmailValidator validator = new EmailValidator();
        assertTrue(validator.isEmailValid("example@gmail.com"));
    }
}
