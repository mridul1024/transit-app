package com.zuk0.gaijinsmash.riderz.data.remote.retrofit;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/*
    This class provides custom annotations for Converter.Factory
*/
public final class JsonAndXmlConverters {
    @Retention(RUNTIME)
    @interface Json {
    }

    @Retention(RUNTIME)
    @interface Xml {
    }
}

