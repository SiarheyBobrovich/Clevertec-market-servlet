package ru.clevertec.market.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class EncodingFilter extends HttpFilter {

    private static final String UTF_8 = "UTF-8";

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        req.setCharacterEncoding(UTF_8);
        res.setCharacterEncoding(UTF_8);
        super.doFilter(req, res, chain);
    }
}
