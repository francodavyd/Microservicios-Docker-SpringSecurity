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
        CircuitBreaker pedidoCircuitBreaker = circuitBreakerRegistry.circuitBreaker("pedidoCB");
        pedidoCircuitBreaker.getEventPublisher()
                .onSuccess(event -> System.out.println("PedidoCB service call successful"))
                .onError(event -> System.out.println("PedidoCB service call failed: " + event.getThrowable().getMessage()))
                .onStateTransition(event -> System.out.println("PedidoCB state changed from "
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