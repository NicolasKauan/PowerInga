import { useState } from "react";
import Swal from "sweetalert2";

import BalanceCard from "../components/BalanceCard";
import PageLayout from "../components/PageLayout";
import RequestFeedback from "../components/RequestFeedback";
import { useAuth } from "../contexts/AuthContext";
import { useApiGet } from "../hooks/useApiGet";
import api from "../services/api";
import { getApiErrorMessage } from "../services/apiErrorMessage";
import { formatPoints } from "../utils/formatPoints";

const BALANCE_REFRESH_FAILED_MESSAGE =
  "Não foi possível atualizar o saldo exibido. Recarregue a página para vê-lo.";

const confirmPurchase = (product) =>
  Swal.fire({
    title: "Confirmar resgate",
    text: `Resgatar "${product.descricao}" por ${formatPoints(product.valorPontos)} pontos?`,
    icon: "question",
    showCancelButton: true,
    confirmButtonText: "Resgatar",
    cancelButtonText: "Cancelar",
  });

const UserStorePage = () => {
  const { user, refreshUser } = useAuth();
  const { data: products, errorMessage, isLoading } = useApiGet("/produtos");
  const [purchasingProductId, setPurchasingProductId] = useState(null);

  const purchaseProduct = async (product) => {
    const confirmation = await confirmPurchase(product);

    if (!confirmation.isConfirmed) {
      return;
    }

    setPurchasingProductId(product.id);

    try {
      await api.post("/vendas", { itens: [{ idProduto: product.id, quantidade: 1 }] });
    } catch (error) {
      Swal.fire({
        title: "Não foi possível resgatar",
        text: getApiErrorMessage(error),
        icon: "error",
        confirmButtonText: "Ok",
      });
      return;
    } finally {
      setPurchasingProductId(null);
    }

    // A compra já foi registrada; uma falha aqui afeta só o saldo exibido, não o resgate.
    let balanceWarning;
    try {
      await refreshUser();
    } catch {
      balanceWarning = BALANCE_REFRESH_FAILED_MESSAGE;
    }

    Swal.fire({
      title: "Resgate realizado!",
      text: balanceWarning,
      icon: "success",
      confirmButtonText: "Ok",
    });
  };

  return (
    <PageLayout title="Lojinha">
      <BalanceCard />

      <RequestFeedback isLoading={isLoading} errorMessage={errorMessage} />

      {products?.length === 0 && <p>Nenhum produto disponível no momento.</p>}

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {products?.map((product) => {
          const hasEnoughBalance = user.pontos >= product.valorPontos;

          return (
            <div key={product.id} className="card bg-base-100 shadow-sm">
              <div className="card-body">
                <h2 className="card-title">{product.descricao}</h2>
                <p className="text-lg font-semibold">{formatPoints(product.valorPontos)} pontos</p>

                <div className="card-actions justify-end">
                  <button
                    type="button"
                    className="btn btn-neutral"
                    disabled={!hasEnoughBalance || purchasingProductId !== null}
                    onClick={() => purchaseProduct(product)}
                  >
                    {hasEnoughBalance ? "Resgatar" : "Saldo insuficiente"}
                  </button>
                </div>
              </div>
            </div>
          );
        })}
      </div>
    </PageLayout>
  );
};

export default UserStorePage;
