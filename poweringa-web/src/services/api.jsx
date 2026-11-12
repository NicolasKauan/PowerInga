import axios from "axios";

export const TOKEN_STORAGE_KEY = "token";

export const CONNECTION_ERROR_MESSAGE =
  "Não foi possível conectar ao servidor. Tente novamente em instantes.";

const api = axios.create({
  baseURL: "http://localhost:8080",
});

api.interceptors.request.use((requestConfig) => {
  const token = localStorage.getItem(TOKEN_STORAGE_KEY);

  if (token) {
    requestConfig.headers.Authorization = `Bearer ${token}`;
  }

  return requestConfig;
});

export default api;
