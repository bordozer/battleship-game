package com.bordozer.battleship.multiplayer.config;

import com.bordozer.commons.utils.FileUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Configuration
@Profile(value = "dev")
public class DevConfig {

    @Value("${server.ssl.key-store-password}")
    private String trustStorePassword;

    @Bean
    public RestTemplate restTemplate() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException {
        final SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(FileUtils.getResourceFile("keystore/battleship.p12"), trustStorePassword.toCharArray())
                .build();
        final SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
        final HttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(socketFactory)
                .build();
        final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }
}
