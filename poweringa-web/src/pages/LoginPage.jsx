import { useForm } from "react-hook-form";
import { Link, useNavigate } from "react-router-dom";
import Swal from "sweetalert2";

import AuthLayout from "../components/AuthLayout";
import FormField from "../components/FormField";
import { HOME_PATH_BY_ROLE } from "../constants/userRoles";
import { useAuth } from "../contexts/AuthContext";
import api, { CONNECTION_ERROR_MESSAGE } from "../services/api";

const LoginPage = () => {
  const navigate = useNavigate();
  const { login } = useAuth();

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm();

  const onSubmit = async (credentials) => {
    try {
      const response = await api.post("/auth/login", credentials);
      const loggedUser = await login(response.data.token);

      navigate(HOME_PATH_BY_ROLE[loggedUser.cargo]);
    } catch (error) {
      Swal.fire({
        title: "Não foi possível entrar",
        text: error.response
          ? "Seu e-mail ou senha estão incorretos."
          : CONNECTION_ERROR_MESSAGE,
        icon: "error",
        confirmButtonText: "Ok",
      });
    }
  };

  return (
    <AuthLayout title="Faça seu login">
      <form
        noValidate
        onSubmit={handleSubmit(onSubmit)}
        className="flex flex-col gap-4 w-full sm:w-120 px-4"
      >
        <FormField
          id="email"
          label="E-mail"
          type="email"
          placeholder="Insira o seu e-mail"
          error={errors.email}
          {...register("email", { required: "O e-mail é obrigatório." })}
        />

        <FormField
          id="senha"
          label="Senha"
          type="password"
          placeholder="Insira a sua senha"
          error={errors.senha}
          {...register("senha", { required: "A senha é obrigatória." })}
        />

        <div className="flex flex-col gap-2">
          <button type="submit" className="btn btn-neutral" disabled={isSubmitting}>
            Entrar
          </button>

          <Link to="/cadastro" className="btn">
            Criar uma conta
          </Link>
        </div>
      </form>
    </AuthLayout>
  );
};

export default LoginPage;
