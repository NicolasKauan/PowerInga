import { IoDocumentTextOutline, IoPeopleOutline } from "react-icons/io5";

import PageLayout from "../components/PageLayout";
import ShortcutCard from "../components/ShortcutCard";
import { useAuth } from "../contexts/AuthContext";

const MANAGER_SHORTCUTS = [
  { to: "/gestor/solicitacoes", label: "Solicitações", Icon: IoDocumentTextOutline },
  { to: "/gestor/usuarios", label: "Usuários", Icon: IoPeopleOutline },
];

const ManagerHomePage = () => {
  const { user } = useAuth();

  return (
    <PageLayout title={`Olá, ${user.descricao}`}>
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
        {MANAGER_SHORTCUTS.map((shortcut) => (
          <ShortcutCard key={shortcut.to} {...shortcut} />
        ))}
      </div>
    </PageLayout>
  );
};

export default ManagerHomePage;
