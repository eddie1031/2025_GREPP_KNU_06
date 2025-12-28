package io.eddie.demo.common.model.web;

public record BaseResponse<T>(
        T data,
        String message
) {
}
