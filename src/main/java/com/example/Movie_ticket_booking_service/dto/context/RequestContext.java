package com.example.Movie_ticket_booking_service.dto.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
@AllArgsConstructor
@Data
@NoArgsConstructor
public class RequestContext {
    private static final ThreadLocal<RequestContext> context = new ThreadLocal<>();
    private String requestId;
    private Instant timestamp;
    private String requestURL;
    private String method;
    private int responseStatus;
    private String ipAddress;
    private String userAgent;
    private String hostName;
    private String subject;
    private List<String> roles;
    private String jti;

    public static RequestContext get() { return context.get(); }
    public static void set(RequestContext requestContext) { context.set(requestContext); }
    public static void clear() { context.remove(); }

}
