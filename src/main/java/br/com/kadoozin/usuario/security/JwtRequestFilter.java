package br.com.kadoozin.usuario.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println("Requisição para: " + path);

        if (path.equals("/usuario/login") || path.equals("/usuario/criar")) {
            System.out.println("Endpoint público, liberando acesso.");
            chain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authorizationHeader);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            final String token = authorizationHeader.substring(7);
            System.out.println("Token extraído: " + token);

            String username = null;
            try {
                username = jwtUtil.extrairEmailDoToken(token);
                System.out.println("Email extraído do token: " + username);
            } catch (Exception e) {
                System.out.println("Erro ao extrair email do token: " + e.getMessage());
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = null;
                try {
                    userDetails = userDetailsService.loadUserByUsername(username);
                    System.out.println("UserDetails carregado: " + userDetails.getUsername());
                } catch (Exception e) {
                    System.out.println("Erro ao carregar usuário: " + e.getMessage());
                }

                if (userDetails != null && jwtUtil.validateToken(token, username)) {
                    System.out.println("Token válido, configurando autenticação.");
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    System.out.println("Token inválido ou usuário nulo.");
                }
            }
        } else {
            System.out.println("Authorization header ausente ou inválido.");
        }

        chain.doFilter(request, response);
    }
}
