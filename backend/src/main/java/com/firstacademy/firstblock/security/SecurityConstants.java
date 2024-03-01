package com.firstacademy.firstblock.security;

public interface SecurityConstants {
    String SECRET = "DXUHFuntt8l7VtjGw4hhwJtOPSGekJXitrYpGuSvagd7OFzetyoRz8EOPpcmwWINtgIbLOYc2oeMc60jDEw8pMG2gy7LbUOfzsjda3AbiRfD9yP0FFjMoPIqb0iAQtcjOgN99q0lWIyQLTHQkbphiV+MDA0vMn/wONjO/XwA=";
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String COOKIE_AUTH_NAME = "x-bankapp-token";
    String SIGN_UP_URL = "/users/sign-up";
    long EXPIRATION_TIME = 7_200_000;
}
