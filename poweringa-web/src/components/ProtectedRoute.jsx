import { Navigate, Outlet } from "react-router-dom";

import { HOME_PATH_BY_ROLE } from "../constants/userRoles";
import { useAuth } from "../contexts/AuthContext";

const ProtectedRoute = ({ allowedRoles }) => {
  const { user, isLoading } = useAuth();

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <span className="loading loading-spinner loading-lg" />
      </div>
    );
  }

  if (!user) {
    return <Navigate to="/" replace />;
  }

  if (!allowedRoles.includes(user.cargo)) {
    return <Navigate to={HOME_PATH_BY_ROLE[user.cargo]} replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;
