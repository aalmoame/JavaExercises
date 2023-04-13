package com.example.retries;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
@EnableRetry
public class DownloaderService {

    private static final Logger logger = LoggerFactory.getLogger(DownloaderService.class);

    @Autowired
    RetryTemplate retryTemplate;

    @Retryable(value = Exception.class, maxAttempts = 5)
    public void downloadWithSimpleBackoff() {
        logger.info("Downloading with simple backoff");
        downloadBadFiles();
    }

    @Retryable(value = RuntimeException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 5000,
                    multiplier = 2,
                    maxDelay = 20000))
    public void downloadExponentialBackoff() {
        logger.info("Downloading with exponential backoff");
        downloadBadFiles();
    }

    @Retryable(value = RuntimeException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 2000,
                    random = true,
                    maxDelay = 10000))
    public void downloadRandomBackoff() {
        logger.info("Downloading with random backoff");
        downloadBadFiles();
    }

    public void downloadRetryTemplate() {
        retryTemplate.execute(arg0 -> {
            logger.info("Downloading with retry template");
            downloadBadFiles();
            return null;
        });
    }


    private void downloadBadFiles() {
        Path path = Paths.get("src/main/resources/picture.png");
        WebClient webClient = WebClient.builder()
                .baseUrl("bad_url")
                .build();

        Flux<DataBuffer> dataBuffer = webClient.get().retrieve().onStatus(HttpStatus::isError, clientResponse -> { return Mono.error(new RuntimeException("bad url"));}).bodyToFlux(DataBuffer.class).onErrorMap(throwable -> {return new RuntimeException("bad url");});
        DataBufferUtils.write(dataBuffer, path, StandardOpenOption.CREATE).share().block();
    }


}
