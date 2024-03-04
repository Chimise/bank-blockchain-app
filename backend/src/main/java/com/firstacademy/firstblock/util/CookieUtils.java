package com.firstacademy.firstblock.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static com.firstacademy.firstblock.security.SecurityConstants.*;

public class CookieUtils {
    public static Cookie getCookie(HttpServletRequest req, String cookieName) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName() == cookieName) {
                return cookie;
            }
        }

        return null;
    }

    public static String getCookieValue(HttpServletRequest req, String cookieName) {
        Cookie cookie = getCookie(req, cookieName);
        System.out.println(cookie);
        return cookie != null ? cookie.getValue() : null;
    }

    public static void setCookie(HttpServletResponse res, String cookieName, String value) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        int maxAge = (int) (EXPIRATION_TIME / 1000);
        cookie.setMaxAge(maxAge);
        res.addCookie(cookie);
    }

    public static void setHeaderToken(HttpServletResponse res) {

    }

    public static String extractJwtToken(HttpServletRequest req, String cookieName) {
        String token = getCookieValue(req, cookieName);
        if (token == null) {
            String header = req.getHeader(HEADER_STRING);
            if (header != null && header.startsWith(TOKEN_PREFIX)) {
                token = header.replace(TOKEN_PREFIX, "");
            }
        }

        return token;
    }

}
