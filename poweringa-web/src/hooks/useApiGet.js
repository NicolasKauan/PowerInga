import { useCallback, useEffect, useState } from "react";

import api from "../services/api";
import { getApiErrorMessage } from "../services/apiErrorMessage";

const fetchResource = async (path) => {
  try {
    const response = await api.get(path);
    return { data: response.data, errorMessage: null };
  } catch (error) {
    return { data: null, errorMessage: getApiErrorMessage(error) };
  }
};

// Se uma nova busca falhar, mantém os dados anteriores na tela junto com a mensagem de erro.
const mergeResult = (previousState, result) => ({
  data: result.data ?? previousState.data,
  errorMessage: result.errorMessage,
  isLoading: false,
});

// Busca um recurso da API ao montar o componente e oferece reload para buscar de novo após uma ação.
export const useApiGet = (path) => {
  const [state, setState] = useState({ data: null, errorMessage: null, isLoading: true });

  useEffect(() => {
    // Ignora a resposta se o componente foi desmontado ou o caminho mudou antes de ela chegar.
    let isCurrentRequest = true;

    fetchResource(path).then((result) => {
      if (isCurrentRequest) {
        setState((previousState) => mergeResult(previousState, result));
      }
    });

    return () => {
      isCurrentRequest = false;
    };
  }, [path]);

  const reload = useCallback(async () => {
    const result = await fetchResource(path);
    setState((previousState) => mergeResult(previousState, result));
  }, [path]);

  return { ...state, reload };
};
