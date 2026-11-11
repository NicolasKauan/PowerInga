// Valores do enum UserRole da API.
export const USER_ROLES = {
  CLIENTE: "CLIENTE",
  GESTOR: "GESTOR",
};

export const HOME_PATH_BY_ROLE = {
  [USER_ROLES.CLIENTE]: "/home",
  [USER_ROLES.GESTOR]: "/gestor",
};
