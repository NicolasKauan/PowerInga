import { useState } from "react";
import Swal from "sweetalert2";

import PageLayout from "../components/PageLayout";
import RequestFeedback from "../components/RequestFeedback";
import RequestStatusBadge from "../components/RequestStatusBadge";
import { REQUEST_STATUS } from "../constants/requestStatus";
import { findRewardCategory } from "../constants/rewardCategories";
import { useApiGet } from "../hooks/useApiGet";
import api from "../services/api";
import { getApiErrorMessage } from "../services/apiErrorMessage";

const DECISIONS = {
  approve: { path: "aprovar", verb: "Aprovar", doneTitle: "Solicitação aprovada!" },
  reject: { path: "reprovar", verb: "Reprovar", doneTitle: "Solicitação reprovada." },
};

const ManagerRequestsPage = () => {
  const requestsQuery = useApiGet("/solicitacoes");
  const usersQuery = useApiGet("/users");
  const [resolvingRequestId, setResolvingRequestId] = useState(null);

  const requests = requestsQuery.data ?? [];
  const usersById = new Map((usersQuery.data ?? []).map((user) => [user.id, user]));
  const pendingRequests = requests.filter((request) => request.status === REQUEST_STATUS.PENDENTE);
  const resolvedRequests = requests.filter((request) => request.status !== REQUEST_STATUS.PENDENTE);

  const resolveRequest = async (request, decision) => {
    const confirmation = await Swal.fire({
      title: `${decision.verb} solicitação?`,
      text: request.descricao,
      icon: "question",
      showCancelButton: true,
      confirmButtonText: decision.verb,
      cancelButtonText: "Cancelar",
    });

    if (!confirmation.isConfirmed) {
      return;
    }

    setResolvingRequestId(request.id);

    try {
      await api.patch(`/solicitacoes/${request.id}/${decision.path}`);
      Swal.fire({ title: decision.doneTitle, icon: "success", confirmButtonText: "Ok" });
    } catch (error) {
      Swal.fire({
        title: `Não foi possível ${decision.verb.toLowerCase()}`,
        text: getApiErrorMessage(error),
        icon: "error",
        confirmButtonText: "Ok",
      });
    } finally {
      setResolvingRequestId(null);
    }

    // Recarrega nos dois casos: se a solicitação já tinha sido resolvida por outro gestor, a lista se corrige.
    requestsQuery.reload();
  };

  const renderRequest = (request) => {
    const category = findRewardCategory(request.categoria);
    const author = usersById.get(request.id_usuario);
    const isPending = request.status === REQUEST_STATUS.PENDENTE;

    return (
      <li key={request.id} className="card bg-base-100 shadow-sm">
        <div className="card-body gap-2">
          <div className="flex flex-wrap items-center justify-between gap-2">
            <p className="font-semibold">{request.descricao}</p>
            <RequestStatusBadge status={request.status} />
          </div>

          <p className="text-sm text-base-content/70">
            {author ? `${author.descricao} (${author.email})` : "Autor não encontrado"} · {category.label} ·{" "}
            {category.points} pontos
          </p>

          {isPending && (
            <div className="card-actions justify-end">
              <button
                type="button"
                className="btn btn-error btn-outline"
                disabled={resolvingRequestId !== null}
                onClick={() => resolveRequest(request, DECISIONS.reject)}
              >
                Reprovar
              </button>
              <button
                type="button"
                className="btn btn-success"
                disabled={resolvingRequestId !== null}
                onClick={() => resolveRequest(request, DECISIONS.approve)}
              >
                Aprovar
              </button>
            </div>
          )}
        </div>
      </li>
    );
  };

  return (
    <PageLayout title="Solicitações">
      <RequestFeedback isLoading={requestsQuery.isLoading} errorMessage={requestsQuery.errorMessage} />

      {usersQuery.errorMessage && (
        <div role="alert" className="alert alert-warning">
          Não foi possível carregar os autores das solicitações: {usersQuery.errorMessage}
        </div>
      )}

      {requestsQuery.data && (
        <>
          <h2 className="text-xl font-semibold">Pendentes ({pendingRequests.length})</h2>
          {pendingRequests.length === 0 && <p>Nenhuma solicitação aguardando análise.</p>}
          <ul className="flex flex-col gap-4">{pendingRequests.map(renderRequest)}</ul>

          <h2 className="text-xl font-semibold">Resolvidas ({resolvedRequests.length})</h2>
          <ul className="flex flex-col gap-4">{resolvedRequests.map(renderRequest)}</ul>
        </>
      )}
    </PageLayout>
  );
};

export default ManagerRequestsPage;
