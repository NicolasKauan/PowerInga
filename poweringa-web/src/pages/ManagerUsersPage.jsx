import PageLayout from "../components/PageLayout";
import RequestFeedback from "../components/RequestFeedback";
import { ROLE_LABELS } from "../constants/userRoles";
import { useApiGet } from "../hooks/useApiGet";
import { formatPoints } from "../utils/formatPoints";

const ManagerUsersPage = () => {
  const { data: users, errorMessage, isLoading } = useApiGet("/users");

  return (
    <PageLayout title="Usuários">
      <RequestFeedback isLoading={isLoading} errorMessage={errorMessage} />

      {users && (
        <div className="card bg-base-100 shadow-sm overflow-x-auto">
          <table className="table">
            <thead>
              <tr>
                <th>Nome</th>
                <th>E-mail</th>
                <th>Cargo</th>
                <th className="text-right">Pontos</th>
              </tr>
            </thead>
            <tbody>
              {users.map((user) => (
                <tr key={user.id}>
                  <td>{user.descricao}</td>
                  <td>{user.email}</td>
                  <td>{ROLE_LABELS[user.cargo]}</td>
                  <td className="text-right">{formatPoints(user.pontos)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </PageLayout>
  );
};

export default ManagerUsersPage;
