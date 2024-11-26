package com.lnyynet.filecollect.auth.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.beans.factory.annotation.Autowired

class JwtAuthenticationFilter : OncePerRequestFilter() {
    
    @Autowired
    private lateinit var jwtUtils: JwtUtils
    
    @Autowired
    private lateinit var userDetailsService: UserDetailsService
    
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }
        
        val jwt = authHeader.substring(7)
        val username = jwtUtils.extractUsername(jwt)
        
        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(username)
            
            if (jwtUtils.validateToken(jwt, userDetails)) {
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                ).apply {
                    details = WebAuthenticationDetailsSource().buildDetails(request)
                }
                SecurityContextHolder.getContext().authentication = authToken
            }
        }
        
        filterChain.doFilter(request, response)
    }
} 