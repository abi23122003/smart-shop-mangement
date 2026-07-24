import { useCallback, useEffect, useMemo, useState } from "react";
import { login as loginRequest } from "../services/authService";
import { tokenStorage } from "../services/storage/tokenStorage";
import { AuthContext } from "./authContextValue";

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => tokenStorage.get());

  const login = useCallback(async (username, password) => {
    const response = await loginRequest(username, password);
    tokenStorage.set(response.token);
    setToken(response.token);
    return response;
  }, []);

  const logout = useCallback(() => {
    tokenStorage.clear();
    setToken(null);
  }, []);

  useEffect(() => {
    function handleSessionExpired() {
      logout();
    }

    window.addEventListener("smart-shop-session-expired", handleSessionExpired);
    return () => window.removeEventListener("smart-shop-session-expired", handleSessionExpired);
  }, [logout]);

  const value = useMemo(() => ({ token, isAuthenticated: Boolean(token), login, logout }), [token, login, logout]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
