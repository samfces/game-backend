package es.samfc.gamebackend.events.filter;

import es.samfc.gamebackend.utils.controller.ControllerUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class PreEventCallingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreEventCallingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ControllerUtils.logRequest(LOGGER, request);
        filterChain.doFilter(request, response);
    }
}
