// Espelha o enum SolicitacaoStatus da API.
export const REQUEST_STATUS = {
  PENDENTE: "PENDENTE",
  APROVADO: "APROVADO",
  REPROVADO: "REPROVADO",
};

export const REQUEST_STATUS_DISPLAY = {
  [REQUEST_STATUS.PENDENTE]: { label: "Em análise", badgeClass: "badge-warning" },
  [REQUEST_STATUS.APROVADO]: { label: "Aprovada", badgeClass: "badge-success" },
  [REQUEST_STATUS.REPROVADO]: { label: "Reprovada", badgeClass: "badge-error" },
};
