import { useEffect, useState } from "react";
import { CiSearch } from "react-icons/ci";
import { BsLightningCharge } from "react-icons/bs";
import { IoStorefrontOutline } from "react-icons/io5";
import { IoIosHelpCircleOutline } from "react-icons/io";
import { NavLink } from "react-router-dom";

import Header from "../components/Header";
import { useAuth } from "../contexts/AuthContext";

const pointsFormatter = new Intl.NumberFormat("pt-BR");

const UserHomePage = () => {
  const { user, refreshUser } = useAuth();
  const [hasBalanceRefreshFailed, setHasBalanceRefreshFailed] = useState(false);

  // O saldo muda fora desta sessão (um gestor aprova uma ação), então é buscado de novo a cada visita.
  // Enquanto a resposta não chega, a tela mostra o último saldo conhecido.
  useEffect(() => {
    refreshUser().catch(() => setHasBalanceRefreshFailed(true));
  }, [refreshUser]);

  return (
    <div className="min-h-screen flex flex-col">
      <Header />

      <section className="gray-background min-h-screen flex flex-col items-center p-4 md:p-8 gap-4">
        <div className="flex flex-col gap-8 max-w-6xl w-full">
          <div className="card bg-base-100 w-full shadow-sm">
            <div className="card-body items-center">
              <h3 className="text-2xl">Saldo</h3>
              <p className="text-center font-semibold text-4xl">{pointsFormatter.format(user.pontos)}</p>
              <p className="text-base-content/70">pontos</p>

              {hasBalanceRefreshFailed && (
                <p className="text-sm text-warning">Não foi possível atualizar o saldo agora.</p>
              )}
            </div>
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
            <div className="card cursor-pointer bg-base-100 w-full shadow-sm">
              <div className="card-body items-center">
                <BsLightningCharge className="text-4xl" />
                <h3 className="text-2xl">Poste sua ação</h3>
              </div>
            </div>

            <div className="card cursor-pointer bg-base-100 w-full shadow-sm">
              <div className="card-body flex items-center">
                <CiSearch className="text-4xl" />
                <h3 className="text-2xl">Consultar andamento</h3>
              </div>
            </div>

            <div className="card cursor-pointer bg-base-100 w-full shadow-sm">
              <NavLink to="/loja">
                <div className="card-body items-center">
                  <IoStorefrontOutline className="text-4xl" />
                  <h3 className="text-2xl">Lojinha</h3>
                </div>
              </NavLink>
            </div>

            <div className="card cursor-pointer bg-base-100 w-full shadow-sm">
              <div className="card-body items-center">
                <IoIosHelpCircleOutline className="text-4xl" />
                <h3 className="text-2xl">Ajuda</h3>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default UserHomePage;
