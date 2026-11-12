import PageLayout from "../components/PageLayout";
import { REWARD_CATEGORIES } from "../constants/rewardCategories";

const UserHelpPage = () => {
  return (
    <PageLayout title="Ajuda">
      <div className="card bg-base-100 shadow-sm card-body gap-4">
        <h2 className="text-xl font-semibold">Como funciona</h2>
        <ol className="list-decimal pl-6 flex flex-col gap-1">
          <li>Em "Poste sua ação", descreva uma ação sustentável que você fez e escolha a categoria.</li>
          <li>Um gestor analisa a ação. Acompanhe o resultado em "Consultar andamento".</li>
          <li>Quando a ação é aprovada, os pontos da categoria entram no seu saldo.</li>
          <li>Na "Lojinha", troque os seus pontos por produtos.</li>
        </ol>

        <h2 className="text-xl font-semibold">Pontos por categoria</h2>
        <div className="overflow-x-auto">
          <table className="table">
            <thead>
              <tr>
                <th>Categoria</th>
                <th>Pontos</th>
              </tr>
            </thead>
            <tbody>
              {REWARD_CATEGORIES.map((category) => (
                <tr key={category.value}>
                  <td>{category.label}</td>
                  <td>{category.points}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </PageLayout>
  );
};

export default UserHelpPage;
