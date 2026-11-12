import { Link, useNavigate } from "react-router-dom";
import Logo5 from "/Logo-5.png";
import UserPhoto from "/UserPhoto.png";

import { HOME_PATH_BY_ROLE } from "../constants/userRoles";
import { useAuth } from "../contexts/AuthContext";

const Header = () => {
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate("/", { replace: true });
  };

  return (
    <header className="green-background flex justify-between items-center p-4">
      <Link to={HOME_PATH_BY_ROLE[user.cargo]}>
        <img src={Logo5} alt="Logo PowerInga - voltar para o início" />
      </Link>

      <div className="dropdown dropdown-end">
        <div tabIndex={0} role="button" className="cursor-pointer">
          <img src={UserPhoto} alt="Foto do usuário" />
        </div>

        <ul
          tabIndex={0}
          className="dropdown-content menu bg-base-100 rounded-box z-10 w-40 p-2 shadow-sm"
        >
          <li>
            <button type="button" onClick={handleLogout}>
              Sair
            </button>
          </li>
        </ul>
      </div>
    </header>
  );
};

export default Header;
