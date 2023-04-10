package ru.clevertec.market.data.receipt;

import java.io.PrintStream;

public interface Receipt {
    void print(PrintStream out);
    String getTitle();
    String getBody();
    String getTotal();
}
