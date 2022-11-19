package org.kristiania.chatRoom.server;

import jakarta.persistence.EntityManager;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class EntityManagerFilter implements Filter {
    private final ChatRoomConfig config;
    private static final Logger logger = LoggerFactory.getLogger(EntityManagerFilter.class);

    public EntityManagerFilter(ChatRoomConfig config) {
        this.config = config;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try (EntityManager entityManager = config.createEntityMangerForRequest()) {
            logger.info( ((HttpServletRequest) servletRequest).getMethod() + ((HttpServletRequest) servletRequest).getPathInfo() + " from " + servletRequest.getRemoteAddr());
            if (((HttpServletRequest) servletRequest).getMethod().equals("GET")) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                entityManager.getTransaction().begin();
                filterChain.doFilter(servletRequest, servletResponse);
                entityManager.flush();
                entityManager.getTransaction().commit();
            }
        }

        config.cleanRequestEntityManager();
    }
}
