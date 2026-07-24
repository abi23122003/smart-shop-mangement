const TOKEN_KEY = "smart_shop_access_token";

// The API authenticates with a Bearer token, so persistence is isolated here.
// Move to an HttpOnly cookie when the backend exposes cookie-based authentication.
export const tokenStorage = {
  get: () => localStorage.getItem(TOKEN_KEY),
  set: (token) => localStorage.setItem(TOKEN_KEY, token),
  clear: () => localStorage.removeItem(TOKEN_KEY),
};
