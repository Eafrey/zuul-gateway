package com.thoughtworks.traing.chensen.zuul.securtity;

import com.google.common.collect.ImmutableList;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.thoughtworks.traing.chensen.zuul.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

@Component
public class ToDoAuthFilter extends OncePerRequestFilter {

    private static final byte[] SECRET_KEY = "kitty".getBytes(Charset.defaultCharset());

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.isEmpty(token)) {
            try {
//                User user = userClient.verifyToken(token);
                User user = getUserFromToken(token);

                String internalToken = user.getId() + ":" + user.getUserName();

//                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                RequestContext requestContext = RequestContext.getCurrentContext();
                requestContext.addZuulRequestHeader("Authorization",internalToken);

                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(internalToken, null,
                                ImmutableList.of(new SimpleGrantedAuthority("admin"),
                                        new SimpleGrantedAuthority("role")))
                );
            } catch (Exception e) {
            } finally {
                filterChain.doFilter(request, response);
            }

        } else {
            filterChain.doFilter(request, response);
        }

    }

    private User getUserFromToken(String token) {
        Claims body = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        int id = (int) body.get("id");
        String name = (String) body.get("name");
        return new User(id, name, "*");
    }


}
