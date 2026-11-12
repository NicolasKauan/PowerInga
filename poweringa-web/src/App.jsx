import { BrowserRouter, Route, Routes } from "react-router-dom";

import ProtectedRoute from "./components/ProtectedRoute";
import { USER_ROLES } from "./constants/userRoles";
import AuthProvider from "./contexts/AuthProvider";
import LoginPage from "./pages/LoginPage";
import ManagerHomePage from "./pages/ManagerHomePage";
import ManagerRequestsPage from "./pages/ManagerRequestsPage";
import ManagerUsersPage from "./pages/ManagerUsersPage";
import RegisterPage from "./pages/RegisterPage";
import UserHelpPage from "./pages/UserHelpPage";
import UserHomePage from "./pages/UserHomePage";
import UserPostActionPage from "./pages/UserPostActionPage";
import UserRequestsPage from "./pages/UserRequestsPage";
import UserStorePage from "./pages/UserStorePage";

import "./App.css";

const App = () => {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path="/" element={<LoginPage />} />
          <Route path="/cadastro" element={<RegisterPage />} />

          <Route element={<ProtectedRoute allowedRoles={[USER_ROLES.CLIENTE]} />}>
            <Route path="/home" element={<UserHomePage />} />
            <Route path="/loja" element={<UserStorePage />} />
            <Route path="/acao" element={<UserPostActionPage />} />
            <Route path="/andamento" element={<UserRequestsPage />} />
            <Route path="/ajuda" element={<UserHelpPage />} />
          </Route>

          <Route element={<ProtectedRoute allowedRoles={[USER_ROLES.GESTOR]} />}>
            <Route path="/gestor" element={<ManagerHomePage />} />
            <Route path="/gestor/solicitacoes" element={<ManagerRequestsPage />} />
            <Route path="/gestor/usuarios" element={<ManagerUsersPage />} />
          </Route>
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
};

export default App;
