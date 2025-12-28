package io.eddie.demo.common.model.web;

public record PageResponse<T>(
        int pageNum,
        int totalPages,
        int pageSize,
        boolean hasNext,
        T contents
) {
}
