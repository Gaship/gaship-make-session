package com.nhnacademy.makesession.config;

import com.nhnacademy.makesession.config.exceptions.NoResponseDataException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * Redis와 관련된 설정을 위한 class.
 *
 * @author 유호철, 조재철
 * @since 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {

    private String host;

    private int port;

    private String password;

    private int database;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }


    /**
     * redis 연동을 위한 연결 설정을 한 Lettuce를 반환하는 빈등록 하는 메서드.
     *
     * @param authenticationConfig Redis 연동을 위해 필요한 설정 정보들.
     * @return Redis Client로 Lettuce를 반환.
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(
            AuthenticationConfig authenticationConfig) {
        String secretHost;
        String secretPassword;

        try {
            secretHost = authenticationConfig.findSecretDataFromSecureKeyManager(host);
            secretPassword = authenticationConfig.findSecretDataFromSecureKeyManager(password);
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException
                | UnrecoverableKeyException | IOException | KeyManagementException e) {
            throw new NoResponseDataException("NHN Secucre Excpetion");
        }

        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(secretHost);
        configuration.setPort(port);
        configuration.setPassword(secretPassword);
        configuration.setDatabase(database);

        return new LettuceConnectionFactory(configuration);
    }

    /**
     * Redis Command 를 도와주는 Template 관련 설정을 하여 빈 등록하는 메서드이다.
     *
     * @return RedisTemplate을 반환.
     */
    @Bean
    public RedisTemplate<byte[], byte[]> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory(null));
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }
}
