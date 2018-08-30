package com.createbest.secruity.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.createbest.secruity.service.JwtUserDetailService;
import com.createbest.secruity.utils.JwtTokenUtils;

@Component
public class JwtTokenFilter extends UsernamePasswordAuthenticationFilter {

	/**
	 * json web token 在请求头的名字
	 */
	@Value("${token.header}")
	private String tokenHeader;

	
	/**
	 * 辅助操作 token 的工具类
	 */
	@Autowired
	private JwtTokenUtils tokenUtils;

	@Autowired
	private JwtUserDetailService userDetailsService;

	@Autowired
	private JwtTokenUtils jwtTokenUtil;

	public JwtTokenFilter(AuthenticationManager authenticationManager) {
		setAuthenticationManager(authenticationManager);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// 将 ServletRequest 转换为 HttpServletRequest 才能拿到请求头中的 token
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // 尝试获取请求头的 token
        String authToken = httpRequest.getHeader(this.tokenHeader);
        System.out.println("getHeader(\"Authorization\")" + httpRequest.getHeader("Authorization"));
        // 尝试拿 token 中的 username
        // 若是没有 token 或者拿 username 时出现异常，那么 username 为 null
        String username = this.tokenUtils.getUsernameFromToken(authToken);
        
        // 如果上面解析 token 成功并且拿到了 username 并且本次会话的权限还未被写入
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (this.tokenUtils.validateToken(authToken, userDetails)) {
                // 生成通过认证
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                // 将权限写入本次会话
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }


		chain.doFilter(request, response);
	}

}