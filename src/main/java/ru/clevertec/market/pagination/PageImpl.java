package ru.clevertec.market.pagination;

import java.util.List;

public record PageImpl<T>(int page, int size, int maxPages, List<T> content) implements Page<T> {
}
