import { BsLightningCharge } from "react-icons/bs";
import { CiSearch } from "react-icons/ci";
import { IoIosHelpCircleOutline } from "react-icons/io";
import { IoStorefrontOutline } from "react-icons/io5";

import BalanceCard from "../components/BalanceCard";
import PageLayout from "../components/PageLayout";
import ShortcutCard from "../components/ShortcutCard";

const USER_SHORTCUTS = [
  { to: "/acao", label: "Poste sua ação", Icon: BsLightningCharge },
  { to: "/andamento", label: "Consultar andamento", Icon: CiSearch },
  { to: "/loja", label: "Lojinha", Icon: IoStorefrontOutline },
  { to: "/ajuda", label: "Ajuda", Icon: IoIosHelpCircleOutline },
];

const UserHomePage = () => {
  return (
    <PageLayout>
      <BalanceCard />

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
        {USER_SHORTCUTS.map((shortcut) => (
          <ShortcutCard key={shortcut.to} {...shortcut} />
        ))}
      </div>
    </PageLayout>
  );
};

export default UserHomePage;
