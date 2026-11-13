import { useCallback, useEffect, useMemo, useState } from "react";

import api, { TOKEN_STORAGE_KEY } from "../services/api";
import { AuthContext } from "./AuthContext";

const hasStoredToken = () => Boolean(localStorage.getItem(TOKEN_STORAGE_KEY));

const fetchLoggedUser = async () => {
  const response = await api.get("/users/me");
  return response.data;
};

const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  // Só há o que carregar se existir um token salvo de uma sessão anterior.
  const [isLoading, setIsLoading] = useState(hasStoredToken);

  const logout = useCallback(() => {
    localStorage.removeItem(TOKEN_STORAGE_KEY);
    setUser(null);
  }, []);

  const refreshUser = useCallback(async () => {
    const loggedUser = await fetchLoggedUser();
    setUser(loggedUser);
    return loggedUser;
  }, []);

  const login = useCallback(
    async (token) => {
      localStorage.setItem(TOKEN_STORAGE_KEY, token);

      try {
        return await refreshUser();
      } catch (error) {
        logout();
        throw error;
      }
    },
    [refreshUser, logout],
  );

  useEffect(() => {
    if (!hasStoredToken()) {
      return;
    }

    // Token expirado ou inválido: a sessão salva não vale mais.
    fetchLoggedUser()
      .then(setUser)
      .catch(() => logout())
      .finally(() => setIsLoading(false));
  }, [logout]);

  const authValue = useMemo(
    () => ({ user, isLoading, login, logout, refreshUser }),
    [user, isLoading, login, logout, refreshUser],
  );

  return <AuthContext value={authValue}>{children}</AuthContext>;
};

export default AuthProvider;
