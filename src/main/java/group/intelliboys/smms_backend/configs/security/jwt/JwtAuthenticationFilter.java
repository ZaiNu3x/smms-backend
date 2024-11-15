package group.intelliboys.smms_backend.configs.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import group.intelliboys.smms_backend.configs.security.UserDetailsServiceImpl;
import group.intelliboys.smms_backend.models.entities.auth.Token;
import group.intelliboys.smms_backend.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        String deviceId = request.getHeader("Device-ID");
        String deviceName = request.getHeader("Device-Name");

        if (authorization == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = Objects.requireNonNull(authorization).substring(7);
        Token fetchedToken = tokenService.getTokenByValue(jwt);

        if (fetchedToken != null) {
            if (!fetchedToken.isExpired() && !fetchedToken.isBlacklisted() &&
                    fetchedToken.getDeviceId().equals(deviceId) && fetchedToken.getDeviceName().equals(deviceName)) {

                String username = jwtService.extractUsername(fetchedToken.getValue());

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtService.isValid(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                                userDetails.getPassword(), userDetails.getAuthorities());

                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } else {
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                Map<String, String> jsonObject = new HashMap<>();
                jsonObject.put("status", "INVALID_TOKEN");
                jsonObject.put("message", "Token is either Expired, Blacklisted or Invalid!");

                ObjectMapper objectMapper = new ObjectMapper();
                String jsonString = objectMapper.writeValueAsString(jsonObject);

                writer.print(jsonString);
                writer.flush();
                writer.close();
            }
        } else {
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            Map<String, String> jsonObject = new HashMap<>();
            jsonObject.put("status", "TOKEN_NOT_FOUND");
            jsonObject.put("message", "Token not found!");

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(jsonObject);

            writer.print(jsonString);
            writer.flush();
            writer.close();
        }

        filterChain.doFilter(request, response);
    }
}
