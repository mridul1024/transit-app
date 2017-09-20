package com.example.gaijinsmash.transitapp.validator;

import com.example.gaijinsmash.transitapp.utils.EmailValidator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by ryanj on 8/10/2017.
 */

public class EmailValidatorTest {

    private EmailValidator validator;

    @Before
    public void setupTest() throws Exception {
        validator = new EmailValidator();
    }

    @Test
    public void testEmailValidatorAcceptsValidEmails() {
        assertTrue(validator.isEmailValid("example@gmail.com"));
    }

    @Test
    public void testEmailValidatorDeniesInvalidEmails() {
        assertFalse(validator.isEmailValid("example@something"));
        assertFalse(validator.isEmailValid("ex%4!@gmail.com"));
    }
}
