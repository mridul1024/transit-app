package com.zuk0.gaijinsmash.riderz.network.retrofit;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class RetrofitClient {

    /*
        Retrofit should be created with the Singleton pattern.

       This client can be used for the whole android project to make requests.
    */

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl, Interceptor interceptor) {
        if(retrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            // For debugging
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.networkInterceptors().add(httpLoggingInterceptor);

            builder.interceptors().add(interceptor);
            //builder.interceptors().add(new JsonInterceptor());
            OkHttpClient client = builder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    // AnnotationStrategy avoids SimpleXml crashes from empty tags
                    .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(new Persister(new AnnotationStrategy())))
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
