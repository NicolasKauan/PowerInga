import { useEffect, useState } from "react";

import { useAuth } from "../contexts/AuthContext";
import { formatPoints } from "../utils/formatPoints";

const BalanceCard = () => {
  const { user, refreshUser } = useAuth();
  const [hasRefreshFailed, setHasRefreshFailed] = useState(false);

  // O saldo muda fora desta sessão (um gestor aprova uma ação), então é buscado de novo sempre que o card aparece.
  // Enquanto a resposta não chega, o card mostra o último saldo conhecido.
  useEffect(() => {
    refreshUser().catch(() => setHasRefreshFailed(true));
  }, [refreshUser]);

  return (
    <div className="card bg-base-100 w-full shadow-sm">
      <div className="card-body items-center">
        <h2 className="text-2xl">Saldo</h2>
        <p className="text-center font-semibold text-4xl">{formatPoints(user.pontos)}</p>
        <p className="text-base-content/70">pontos</p>

        {hasRefreshFailed && (
          <p className="text-sm text-warning">Não foi possível atualizar o saldo agora.</p>
        )}
      </div>
    </div>
  );
};

export default BalanceCard;
