package com.apidemo.mspatterns.domain;

import java.util.function.Function;

public sealed interface DomainResult<T> permits DomainResult.Success, DomainResult.Failure {
    record Success<T>(T value) implements DomainResult<T> {}
    record Failure<T>(String reason) implements DomainResult<T> {}

    static <T> DomainResult<T> ok(T value) {
        return new Success<>(value);
    }

    static <T> DomainResult<T> fail(String reason) {
        return new Failure<>(reason);
    }

    default <R> DomainResult<R> map(Function<T, R> mapper) {
        return switch (this) {
            case Success<T> success -> ok(mapper.apply(success.value()));
            case Failure<T> failure -> fail(failure.reason());
        };
    }

    default T orThrow() {
        return switch (this) {
            case Success<T> success -> success.value();
            case Failure<T> failure -> throw new IllegalArgumentException(failure.reason());
        };
    }
}
