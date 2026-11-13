// Espelha o enum CategoriaRecompensa da API. Os pontos são definidos lá; aqui servem só para exibição.
export const REWARD_CATEGORIES = [
  { value: "MICRO_ACAO", label: "Micro ação", points: 15 },
  { value: "ACAO_LOCALIZADA", label: "Ação localizada", points: 30 },
  { value: "ACAO_ESTRUTURAL", label: "Ação estrutural", points: 60 },
  { value: "ACAO_ALTO_IMPACTO", label: "Ação de alto impacto", points: 120 },
  { value: "ACAO_GLOBAL", label: "Ação global", points: 240 },
];

export const findRewardCategory = (value) =>
  REWARD_CATEGORIES.find((category) => category.value === value);
