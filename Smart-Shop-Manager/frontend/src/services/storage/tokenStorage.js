const TOKEN_KEY = "smart_shop_access_token";

// The API authenticates with a Bearer token, so persistence is isolated here.
// Storing JWTs in localStorage is vulnerable to XSS attacks and is a known, accepted risk
// for the current stage of this project.
// In a future hardening phase, migrate to HttpOnly cookie-based authentication with CSRF protection.
export const tokenStorage = {
  get: () => localStorage.getItem(TOKEN_KEY),
  set: (token) => localStorage.setItem(TOKEN_KEY, token),
  clear: () => localStorage.removeItem(TOKEN_KEY),
};
