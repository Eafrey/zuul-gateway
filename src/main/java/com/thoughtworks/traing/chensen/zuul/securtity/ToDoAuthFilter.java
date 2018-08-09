package com.thoughtworks.traing.chensen.zuul.securtity;

import com.google.common.collect.ImmutableList;
import com.netflix.zuul.context.RequestContext;
import com.thoughtworks.traing.chensen.zuul.client.UserClient;
import com.thoughtworks.traing.chensen.zuul.dto.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ToDoAuthFilter extends OncePerRequestFilter {

    private static final byte[] SECRET_KEY = "kitty".getBytes(Charset.defaultCharset());

    @Autowired
    private UserClient userClient;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("income request {}", request.getServletPath());
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.isEmpty(token)) {
            try {
                User user = getUserFromToken(token);

                String internalToken = user.getId() + ":" + user.getUserName();

                RequestContext requestContext = RequestContext.getCurrentContext();
                requestContext.addZuulRequestHeader(HttpHeaders.AUTHORIZATION,internalToken);

                User user1 = userClient.verifyTokenInternal(internalToken);




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


    public static String generateToken(int id, String userName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("name", userName);

        String token = Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
        return token;
    }


}
