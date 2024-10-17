package com.francodavyd.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CircuitBreakerEventConfig {

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public CircuitBreakerEventConfig(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @Bean
    public void configureCircuitBreakerEvents() {
        CircuitBreaker emailCircuitBreaker = circuitBreakerRegistry.circuitBreaker("emailCB");
        emailCircuitBreaker.getEventPublisher()
                .onSuccess(event -> System.out.println("Email service call successful"))
                .onError(event -> System.out.println("Email service call failed: " + event.getThrowable().getMessage()))
                .onStateTransition(event -> System.out.println("EmailCB state changed from "
                        + event.getStateTransition().getFromState()
                        + " to " + event.getStateTransition().getToState()));

        CircuitBreaker pagoCircuitBreaker = circuitBreakerRegistry.circuitBreaker("pagoCB");
        pagoCircuitBreaker.getEventPublisher()
                .onSuccess(event -> System.out.println("Pago service call successful"))
                .onError(event -> System.out.println("Pago service call failed: " + event.getThrowable().getMessage()))
                .onStateTransition(event -> System.out.println("PagoCB state changed from "
                        + event.getStateTransition().getFromState()
                        + " to " + event.getStateTransition().getToState()));
    }
}
