package com.example.Movie_ticket_booking_service.security;

import com.example.Movie_ticket_booking_service.exception.AppException;
import com.example.Movie_ticket_booking_service.exception.ErrorCode;
import com.example.Movie_ticket_booking_service.dto.context.RequestContext;
import com.example.Movie_ticket_booking_service.service.Impl.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.InetAddress;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response) {
            private int status = SC_OK;

            @Override
            public void setStatus(int status) { super.setStatus(status); this.status = status; }

            @Override
            public int getStatus() { return status; }
        };
        createRequestContext(request, responseWrapper);

        try {
            String token = extractToken(request);
            if(redisService.isBlackList(token)) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            if(token == null) {
                filterChain.doFilter(request, response);
                return;
            }
            String email = jwtTokenUtil.extractEmail(token);
            if(token != null && SecurityContextHolder.getContext().getAuthentication() == null && email != null) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
                RequestContext requestContext = RequestContext.get();
                requestContext.setRoles(userDetails.getAuthorities().stream()
                                .map(a -> a.getAuthority())
                                .toList());
                requestContext.setSubject(userDetails.getUsername());
                if(jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    var auth = SecurityContextHolder.getContext().getAuthentication();
                    log.info("principal={}, authorities={}", auth.getName(), auth.getAuthorities());
                }
            }
            filterChain.doFilter(request, response);
        }catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during JWT processing: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"code\":9999,\"message\":\"Internal Server Error\"}");
        }

    }

    private void createRequestContext(HttpServletRequest request, HttpServletResponseWrapper responseWrapper) {
        RequestContext requestContext = new RequestContext();
        requestContext.setRequestId(UUID.randomUUID().toString());
        requestContext.setTimestamp(Instant.now());
        requestContext.setResponseStatus(responseWrapper.getStatus());
        requestContext.setRequestURL(request.getRequestURL().toString());
        requestContext.setMethod(request.getMethod());
        requestContext.setUserAgent(request.getHeader("User-Agent"));

        String ip = Optional.ofNullable(request.getHeader("X-Forwarded-For")).orElse(request.getRemoteAddr());
        requestContext.setIpAddress(ip);
        try { requestContext.setHostName(InetAddress.getLocalHost().getHostName()); } catch (Exception ignored) {}

        RequestContext.set(requestContext);
    }

    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
