package org.hisp.dhis.android.core.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hisp.dhis.android.core.D2;
import org.hisp.dhis.android.core.configuration.ConfigurationModel;
import org.hisp.dhis.android.core.data.api.BasicAuthenticatorFactory;
import org.hisp.dhis.android.core.data.api.FieldsConverterFactory;
import org.hisp.dhis.android.core.data.database.DatabaseAdapter;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class D2Factory {
    public static D2 create(String url, DatabaseAdapter databaseAdapter) {
        ConfigurationModel config = ConfigurationModel.builder()
                .serverUrl(HttpUrl.parse(url))
                .build();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new D2.Builder()
                .configuration(config)
                .databaseAdapter(databaseAdapter)
                .okHttpClient(
                        new OkHttpClient.Builder()
                                .addInterceptor(BasicAuthenticatorFactory.create(databaseAdapter))
                                .addInterceptor(loggingInterceptor)
                                .build()
                ).build();

    }
    public static D2 create2(String url, DatabaseAdapter databaseAdapter) {
        ConfigurationModel config = ConfigurationModel.builder()
                .serverUrl(HttpUrl.parse(url))
                .build();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new D2.Builder()
                .configuration(config)
                .databaseAdapter(databaseAdapter)
                .okHttpClient(
                        new OkHttpClient.Builder()
                                .addInterceptor(BasicAuthenticatorFactory.create(databaseAdapter))
                                .addInterceptor(loggingInterceptor)
                                .build()
                ).build();


    }
}
