package com.zuk0.gaijinsmash.riderz.data.remote.retrofit

/*
    This class provides custom annotations for Converter.Factory
*/
class ResponseTypeConverter {
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    internal annotation class Json

    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    internal annotation class Xml
}