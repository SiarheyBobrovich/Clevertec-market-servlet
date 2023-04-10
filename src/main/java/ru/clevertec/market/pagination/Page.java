package ru.clevertec.market.pagination;

import java.util.List;

public interface Page<T> {

    int page();

    int size();

    int maxPages();

    List<T> content();

    /**
     * Calculate maximum number of pages
     *
     * @param rows count rows
     * @param size page size
     * @return maximum number of pages
     */
    static int calculateMaxPages(int rows, int size) {
        return rows / size + (rows % size == 0 ? 0 : 1);
    }
}
