package com.francodavyd.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CircuitBreakerEventConfig {

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public CircuitBreakerEventConfig(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

@PostConstruct
    public void configureCircuitBreakerEvents() {
        CircuitBreaker emailCircuitBreaker = circuitBreakerRegistry.circuitBreaker("emailCB");
        emailCircuitBreaker.getEventPublisher()
                .onSuccess(event -> System.out.println("EmailCB service call successful"))
                .onError(event -> System.out.println("EmailCB service call failed: " + event.getThrowable().getMessage()))
                .onStateTransition(event -> System.out.println("EmailCB state changed from "
                        + event.getStateTransition().getFromState()
                        + " to " + event.getStateTransition().getToState()));

        CircuitBreaker pagoCircuitBreaker = circuitBreakerRegistry.circuitBreaker("pagoCB");
        pagoCircuitBreaker.getEventPublisher()
                .onSuccess(event -> System.out.println("PaymentCB service call successful"))
                .onError(event -> System.out.println("PaymentCB service call failed: " + event.getThrowable().getMessage()))
                .onStateTransition(event -> System.out.println("PaymentCB state changed from "
                        + event.getStateTransition().getFromState()
                        + " to " + event.getStateTransition().getToState()));

        CircuitBreaker productoCircuitBreaker = circuitBreakerRegistry.circuitBreaker("productoCB");
        productoCircuitBreaker.getEventPublisher()
                .onSuccess(event -> System.out.println("ProductCB service call successful"))
                .onError(event -> System.out.println("ProductCB service call failed: " + event.getThrowable().getMessage()))
                .onStateTransition(event -> System.out.println("ProductCB state changed from "
                        + event.getStateTransition().getFromState()
                        + " to " + event.getStateTransition().getToState()));

    }
}
