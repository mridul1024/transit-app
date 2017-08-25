package com.example.gaijinsmash.transitapp.error;

import com.example.gaijinsmash.transitapp.internet.InternetOperations;

/**
 * Created by ryanj on 8/23/2017.
 */

public class InternetException extends Exception {

    public InternetException() {}

    public InternetException(String message) {
        super(message);
    }
}
