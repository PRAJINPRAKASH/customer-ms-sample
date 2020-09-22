package com.techgentsia.ecomexample.customerms.filters;

import com.techgentsia.ecomexample.customerms.services.CustomerService;
import com.techgentsia.ecomexample.customerms.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static com.techgentsia.ecomexample.customerms.constants.AuthConstants.AUTH_HEADER;
import static com.techgentsia.ecomexample.customerms.constants.AuthConstants.AUTH_HEADER_PREFIX;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private CustomerService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = httpServletRequest.getHeader(AUTH_HEADER);

        if(authHeader != null && authHeader.startsWith(AUTH_HEADER_PREFIX)){
            final String token = authHeader.replace(AUTH_HEADER_PREFIX,"");
            final String id = jwtTokenUtil.extractUserId(token);
            if(id!=null && SecurityContextHolder.getContext().getAuthentication() == null){
                try {
                    UUID customerId = UUID.fromString(id);
                    UserDetails userDetails = userService.loadUserByUserId(customerId);
                    if (jwtTokenUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                                = new UsernamePasswordAuthenticationToken(userDetails,
                                null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
                catch (Exception e) {
                    logger.error(e);
                }

            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);

    }
}
