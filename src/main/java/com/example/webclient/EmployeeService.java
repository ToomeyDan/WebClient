package com.example.webclient;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public class EmployeeService {

    private final WebClient webClient;

    public EmployeeService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Employee> createEmployee(Employee employee) {
        return webClient.post()
                .uri("/employees")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(employee), Employee.class)
                .retrieve()
                .bodyToMono(Employee.class);
    }

    public Flux<Employee> getAllEmployees() {
        return webClient.get()
                .uri("/employees")
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        clientResponse -> handleErrorResponse(clientResponse.statusCode()))
                .bodyToFlux(Employee.class)
                .onErrorResume(Exception.class, e -> Flux.empty()); // Return an empty collection on error
    }
    public Mono<Employee> getEmployeeById(int id) {
        return webClient.get()
                .uri("/employees/{id}", id)
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        clientResponse -> handleErrorResponse(clientResponse.statusCode()))
                .bodyToMono(Employee.class);
    }
    private Mono<? extends Throwable> handleErrorResponse(HttpStatusCode statusCode) {
        // Handle non-success status codes here (e.g., logging or custom error handling)
        return Mono.error(new EmployeeServiceException("Failed to fetch employee. Status code: " + statusCode));
    }
}
