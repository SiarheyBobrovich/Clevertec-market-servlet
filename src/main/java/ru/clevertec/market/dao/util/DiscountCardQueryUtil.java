package ru.clevertec.market.dao.util;

public class DiscountCardQueryUtil {

    public static final String SELECT_ROW_COUNT = """
            SELECT
                COUNT(*) as rows
            FROM
                market.discount_card;
            """;


    public static final String SELECT_DISCOUNT_CARD_BY_ID_QUERY =
            """
                    SELECT
                        id,
                        discount
                    FROM
                        market.discount_card
                    WHERE
                        id = ?;
                    """;

    public static final String SELECT_ALL_DISCOUNT_CARDS_PAGEABLE =
            """
                    SELECT
                        id,
                        discount
                    FROM
                        market.discount_card
                    ORDER BY id
                    OFFSET ?
                    LIMIT ?;
                    """;

    public static final String INSERT_NEW_DISCOUNT_CARD =
            """
                    INSERT INTO
                        market.discount_card (
                            discount
                        )
                        VALUES (?);
                    """;

    public static final String UPDATE_DISCOUNT_CARD_BY_ID =
            """
                    UPDATE
                        market.discount_card
                        SET
                            discount = ?
                        WHERE
                            id = ?;
                    """;

    public static final String DELETE_DISCOUNT_CARD_BY_ID =
            """
                    DELETE FROM
                        market.discount_card
                        WHERE
                            id = ?;
                    """;

    private DiscountCardQueryUtil() {
    }
}
