package sibsutis.sed.sedsibsutis.app.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class WebClientConfig implements WebFluxConfigurer {

    @Value("${sed.document.url}")
    private String sedDocumentUrl;

    @Value("${sed.document.connect.timeout}")
    private int connectTimeout;

    @Value("${sed.document.read.timeout}")
    private int readTimeout;

    @Value("${sed.document.write.timeout}")
    private int writeTimeout;

    /**
     * @return WebClient для http взаимодействия с sed-document модулем
     */
    @Bean
    public WebClient getSedDocumentWebClient() throws Exception {
        // Отключаем проверку SSL-сертификатов
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        HttpClient httpClient = HttpClient.create()
                .secure(t -> t.sslContext(sslContext))
                .tcpConfiguration(client ->
                        client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
                                .doOnConnected(conn -> conn
                                        .addHandlerLast(
                                                new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
                                        .addHandlerLast(
                                                new WriteTimeoutHandler(writeTimeout, TimeUnit.MILLISECONDS))));

        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient.wiretap(true));

        return WebClient.builder()
                .baseUrl(sedDocumentUrl)
                .clientConnector(connector)
                .defaultHeader("Content-Type", "application/json; charset=utf-8")
                .filter(logRequest())
                .filter(logResponse())
                .exchangeStrategies(ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .build())
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("SED-DOCUMENT: request {} {}", clientRequest.method(), clientRequest.url());
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("SED-DOCUMENT: response status code {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }

}
