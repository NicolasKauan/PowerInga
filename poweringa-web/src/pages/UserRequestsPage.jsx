import { Link } from "react-router-dom";

import PageLayout from "../components/PageLayout";
import RequestFeedback from "../components/RequestFeedback";
import RequestStatusBadge from "../components/RequestStatusBadge";
import { findRewardCategory } from "../constants/rewardCategories";
import { useApiGet } from "../hooks/useApiGet";

const UserRequestsPage = () => {
  const { data: requests, errorMessage, isLoading } = useApiGet("/solicitacoes");

  return (
    <PageLayout title="Consultar andamento">
      <RequestFeedback isLoading={isLoading} errorMessage={errorMessage} />

      {requests?.length === 0 && (
        <div className="card bg-base-100 shadow-sm card-body items-center">
          <p>Você ainda não enviou nenhuma ação.</p>
          <Link to="/acao" className="btn btn-neutral">
            Poste sua primeira ação
          </Link>
        </div>
      )}

      <ul className="flex flex-col gap-4">
        {requests?.map((request) => {
          const category = findRewardCategory(request.categoria);

          return (
            <li key={request.id} className="card bg-base-100 shadow-sm">
              <div className="card-body flex-row flex-wrap items-center justify-between gap-2">
                <div>
                  <p className="font-semibold">{request.descricao}</p>
                  <p className="text-sm text-base-content/70">
                    {category.label} · {category.points} pontos
                  </p>
                </div>
                <RequestStatusBadge status={request.status} />
              </div>
            </li>
          );
        })}
      </ul>
    </PageLayout>
  );
};

export default UserRequestsPage;
