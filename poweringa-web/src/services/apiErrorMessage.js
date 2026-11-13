import { CONNECTION_ERROR_MESSAGE } from "./api";

const UNEXPECTED_ERROR_MESSAGE = "Ocorreu um erro inesperado. Tente novamente.";

// A API responde regras de negócio com um texto e erros de validação com uma lista de textos.
export const getApiErrorMessage = (error) => {
  if (!error.response) {
    return CONNECTION_ERROR_MESSAGE;
  }

  const { data } = error.response;

  if (typeof data === "string" && data.trim()) {
    return data;
  }

  if (Array.isArray(data) && data.length > 0) {
    return data.join(" ");
  }

  return UNEXPECTED_ERROR_MESSAGE;
};
