package br.com.ponto.registro.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public abstract class SecurityUtil {

	private SecurityUtil() {
		throw new AssertionError();
	}
	
	private static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public static String getUsuarioLogado() {
		Jwt jwt = (Jwt) getAuthentication().getPrincipal();
		return jwt.getClaimAsString("preferred_username");
	}
}