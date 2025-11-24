package com.leverx.trugame.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Component
public class TransactionLoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(TransactionLoggingFilter.class);

    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[34m";
    private static final String GREEN = "\u001B[32m";
    private static final String CYAN = "\u001B[36m";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        try {
            MDC.put("transactionId", UUID.randomUUID().toString());

            HttpServletRequest httpRequest = (HttpServletRequest) req;
            log.info(
                    "Transaction started: [{}{}{}]\n\t{}{}{} – {}{}{}",
                    CYAN, MDC.get("transactionId"), RESET,
                    BLUE, httpRequest.getMethod(), RESET,
                    GREEN, httpRequest.getRequestURI(), RESET
            );

            chain.doFilter(req, res);

            log.info(
                    "Transaction completed: [{}{}{}]",
                    CYAN, MDC.get("transactionId"), RESET
            );
        } finally {
            MDC.remove("transactionId");
        }
    }
}
