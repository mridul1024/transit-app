package com.example.gaijinsmash.transitapp.model.abstractClass;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public abstract class AbstractModelTest<T> {

    private T primary;
    private T same1;
    private T same2;
    private T different;

    @Before
    public void setUp() throws Exception {
        primary = null;
        same1 = null;
        same2 = null;
        different = null;
    }


    @Test
    public void compareObjectsTest() throws Exception {
        assertTrue("Same1 equals Same2", same1 == same2);
        //("Same1 does not equal Different", same1 != different);
        //assertTrue("Same2 does not equal Different", same2 != different);
        assertTrue("Primary object is not null", primary != null);
        assertFalse("Primary should fail if null", primary == null);
    }
}
