package com.example.gaijinsmash.transitapp.model;

import com.example.gaijinsmash.transitapp.model.bart.Station;

import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public abstract class AbstractModelTest<T> implements Comparator<T> {
    Class<T> reference;
    Class<T> primary = null;
    Class<T> same1 = null;
    Class<T> same2 = null;
    Class<T> different = null;

    public AbstractModelTest(Class<T> classRef) {
        reference = classRef;
    }

    @Before
    public void setupTest() throws Exception {
        primary = (Class<T>) getInstance();
        same1 = (Class<T>) getInstance();
        same2 = (Class<T>) getInstance();
        different = (Class<T>) getInstance();
    }

    public T getInstance() throws Exception {
        return reference.newInstance();
    }

    @Test
    public void startTest() throws Exception {
        assertTrue("Same1 equals Same2", same1 == same2);
        //("Same1 does not equal Different", same1 != different);
        //assertTrue("Same2 does not equal Different", same2 != different);
        assertTrue("Primary object is not null", primary != null);
        assertFalse("Primary should fail if null", primary == null);
    }
}
