package com.zuk0.gaijinsmash.riderz.network;

import android.content.Context;

import com.zuk0.gaijinsmash.riderz.utils.InputStreamUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.InputStream;

import static junit.framework.Assert.assertTrue;

/**
 * Created by ryanj on 9/17/2017.
 */

public class FetchInputStreamTest {

    private static final String TEST_URI = "http://api.bart.gov/api/bsa.aspx?cmd=bsa&key=Q7Z9-PZ53-9QXT-DWE9";
    private InputStreamUtils mFis;
    @Mock
    private Context mContext;

    @Before
    public void setUp() throws Exception {
        mFis = new InputStreamUtils(mContext);
    }

    @Test
    public void test_should_return_true() throws Exception {
        InputStream is = mFis.connectToApi(TEST_URI);
        assertTrue("input stream is not null", is != null);
    }
}
