package ru.clevertec.market.pagination;

import org.apache.commons.lang3.StringUtils;

public interface Pageable {

    int page();

    int size();

    /**
     * Return standard pageable implementation
     *
     * @param page current number of page(if page <= 0 then 0, else page - 1)
     * @param size size of page(if size < 1 then 20, else size)
     * @return new pageable
     */
    static Pageable of(int page, int size) {
        return new PageableImpl(
                page <= 1 ? 0 : page - 1,
                size < 1 ? 20 : size
        );
    }

    /**
     * Return standard pageable implementation, see also Pageable.of(int, int)
     *
     * @param page current number of page(if page == null then of(1, size), else of(page, size))
     * @param size size of page(if size == null then of(page, 20), else of(page, size))
     * @return of(int, int)
     */
    static Pageable of(String page, String size) {
        return Pageable.of(
                StringUtils.isNumeric(page) ? Integer.parseInt(page) : 1,
                StringUtils.isNumeric(size) ? Integer.parseInt(size) : 20);
    }
}
