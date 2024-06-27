package com.audition.configuration;

import com.audition.common.logging.AuditionLogger;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class ResponseHeaderInjector implements ClientHttpRequestInterceptor {


    private static final Logger LOG = LoggerFactory.getLogger(ResponseHeaderInjector.class);

    private final AuditionLogger logger;

    private final Tracer tracer;

    public ResponseHeaderInjector(Tracer tracer) {
        this.tracer = tracer;
        this.logger = new AuditionLogger();
    }


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] requestBody, ClientHttpRequestExecution execution)
        throws IOException {

        Span span = tracer.spanBuilder("resttemplate-span").startSpan();
        logger.info(LOG, request.getMethod().name() + " " + request.getURI().toString());
        logger.info(LOG, "Request Headers: {}", request.getHeaders());
        logger.info(LOG, "Request Payload: {}", new String(requestBody.toString()));

        try (var scope = span.makeCurrent()) {
            ClientHttpResponse response = execution.execute(request, requestBody);
            response.getHeaders().add("traceid", span.getSpanContext().getTraceId());
            response.getHeaders().add("spanid", span.getSpanContext().getSpanId());
            logger.info(LOG, "Response Code: {}", response.getStatusCode().value());
            logger.info(LOG, "Response Headers: {}", response.getHeaders());

            InputStreamReader reader = new InputStreamReader(response.getBody(), StandardCharsets.UTF_8);
            String responsePayload = new BufferedReader(reader).lines().collect(Collectors.joining("\n"));
            logger.info(LOG, "Response Payload: {}", responsePayload);
            return response;
        } finally {
            span.end();
        }

    }


}
