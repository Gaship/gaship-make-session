package com.nhnacademy.makesession.config;

import com.nhnacademy.makesession.dto.SecureKeyResponseDto;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Objects;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * secure key 와 관련된 설정을 위한 class.
 *
 * @author 유호철, 조재철
 * @version 1.0.0
 */
@Configuration
@ConfigurationProperties(prefix = "secure-key-manager")
public class AuthenticationConfig {

    private String url;
    private String appKey;
    private String localKey;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getLocalKey() {
        return localKey;
    }

    public void setLocalKey(String localKey) {
        this.localKey = localKey;
    }

    /**
     * nhn cloud key manager 에 secure key 를 얻기 위한 메서드.
     *
     * @param keyId secure key 를 얻기 위해 필요한 key id.
     * @return 원하는 secure key 를 반환.
     */
    String findSecretDataFromSecureKeyManager(final String keyId)
        throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException,
        UnrecoverableKeyException, KeyManagementException {

        KeyStore clientStore = KeyStore.getInstance("PKCS12");
        Resource resource = new ClassPathResource("/github-action.p12");
        clientStore.load(resource.getInputStream(), localKey.toCharArray());

        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
        sslContextBuilder.setProtocol("TLS");
        sslContextBuilder.loadKeyMaterial(clientStore, localKey.toCharArray());
        sslContextBuilder.loadTrustMaterial(new TrustSelfSignedStrategy());

        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
            sslContextBuilder.build());
        CloseableHttpClient httpClient = HttpClients.custom()
                                                    .setSSLSocketFactory(sslConnectionSocketFactory)
                                                    .build();
        HttpComponentsClientHttpRequestFactory requestFactory =
            new HttpComponentsClientHttpRequestFactory(httpClient);

        return Objects.requireNonNull(new RestTemplate(requestFactory)
                          .getForEntity(url + "/keymanager/v1.0/appkey/{appkey}/secrets/{keyid}",
                              SecureKeyResponseDto.class,
                              this.appKey,
                              keyId)
                          .getBody())
                      .getBody()
                      .getSecret();
    }

}

