package com.example.gaijinsmash.transitapp.model;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by ryanj on 9/16/2017.
 */

public abstract class AbstractModelTest<T> {
    final Class<T> object;
    Class<T> first = null;
    Class<T> second = null;
    Class<T> third = null;

    public AbstractModelTest(Class<T> object) {
        this.object = object;
    }

    //TODO: create initial tests
    public void setupTest() throws Exception {
        first = (Class<T>) object.newInstance();
        second = (Class<T>) object.newInstance();
        third = (Class<T>) object.newInstance();
    }
}
