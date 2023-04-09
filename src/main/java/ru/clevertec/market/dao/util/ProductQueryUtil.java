package ru.clevertec.market.dao.util;

public class ProductQueryUtil {

    public static final String SELECT_PRODUCT_BY_ID_QUERY =
            """
                    SELECT
                        id,
                        description,
                        price,
                        quantity,
                        is_discount
                    FROM
                        market.product
                    WHERE
                        id = ?;
                    """;

    public static final String SELECT_PRODUCT_BY_ID_AND_QUANTITY_QUERY = """
            SELECT
                p.id,
                p.description,
                p.price,
                p.quantity,
                p.is_discount
            FROM
                market.product p
            WHERE
                p.id = ? AND
                p.quantity >= ?;
            """;

    public static final String UPDATE_PRODUCT_QUERY = """
            UPDATE market.product
                SET
                    description = ?,
                    price = ?,
                    quantity = ?,
                    is_discount = ?
                WHERE
                    id = ?;
            """;

    public static final String DELETE_PRODUCT_BY_ID = """
            DELETE FROM market.product
                WHERE id = ?;
            """;

    public static final String INSERT_NEW_PRODUCT = """
            INSERT INTO
                market.product(
                    description,
                    price,
                    quantity,
                    is_discount
                )
                VALUES (?,?,?,?);
            """;

    public static final String SELECT_ALL_PRODUCTS =
            """
                    SELECT
                        id,
                        description,
                        price,
                        quantity,
                        is_discount
                    FROM
                        market.product
                    ORDER BY id
                    """;

    public static final String SELECT_ROW_COUNT = """
            SELECT
                COUNT(*) as rows
            FROM
                market.product;
            """;

    public static final String SELECT_ALL_PRODUCTS_PAGEABLE =
            """
                    SELECT
                        id,
                        description,
                        price,
                        quantity,
                        is_discount
                    FROM
                        market.product
                    ORDER BY id
                    OFFSET ?
                    LIMIT ?;
                    """;

    private ProductQueryUtil() {
    }
}
