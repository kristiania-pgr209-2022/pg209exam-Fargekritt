package org.kristiania.chatRoom.server;

import jakarta.persistence.EntityManager;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class EntityManagerFilter implements Filter {
    private final ChatRoomConfig config;

    public EntityManagerFilter(ChatRoomConfig config) {
        this.config = config;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        EntityManager entityManager = config.createEntityMangerForRequest();

        if(((HttpServletRequest)servletRequest).getMethod().equals("GET")){
            filterChain.doFilter(servletRequest,servletResponse);
        } else{
            entityManager.getTransaction().begin();
            filterChain.doFilter(servletRequest,servletResponse);
            entityManager.flush();
            entityManager.getTransaction().commit();
        }
        config.cleanRequestEntityManager();
    }
}
