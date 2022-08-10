package com.nhnacademy.makesession.config;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mysql DB 설정을 위한 클래스 입니다.
 *
 * @author 유호철, 조재철
 * @version 1.0.0
 */
@Configuration
@ConfigurationProperties(prefix = "datasource")
public class DataSourceConfig {

    private String driverClassName;
    private String url;
    private String username;
    private String password;

    /**
     * MySql 설정을 위한 빈 입니다.
     *
     * @param authenticationConfig secure key 와 관련된 설정을 위한 객체.
     * @return 연결 설정하는 ConnectionFactory 반환.
     * @throws UnrecoverableKeyException key 를 복원할 수 없는 경우에 예외를 발생.
     * @throws CertificateException      인증서의 encode 문제, 유효하지 않은 경우 예외 발생.
     * @throws KeyStoreException         키스토어 예외 발생.
     * @throws IOException               I/O 오류가 발생하는 경우에 throw 되는 예외 발생.
     * @throws NoSuchAlgorithmException  암호 알고리즘이 요구되었음에도 불구하고, 현재의 환경에서는 사용 가능하지 않은 경우에 예외 발생.
     * @throws KeyManagementException    키 관리를 다루는 모든 작업에 대한 일반적인 키 관리 예외 발생.
     */
    @Bean
    public DataSource getDataSource(AuthenticationConfig authenticationConfig)
        throws UnrecoverableKeyException, CertificateException, KeyStoreException, IOException,
        NoSuchAlgorithmException, KeyManagementException {
        String secretUrl = authenticationConfig.findSecretDataFromSecureKeyManager(url);
        String secretPassword = authenticationConfig.findSecretDataFromSecureKeyManager(password);

        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(driverClassName);
        dataSourceBuilder.url(secretUrl);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(secretPassword);

        return dataSourceBuilder.build();
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
