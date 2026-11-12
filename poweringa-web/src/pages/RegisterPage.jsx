import { useForm } from "react-hook-form";
import { Link, useNavigate } from "react-router-dom";
import Swal from "sweetalert2";

import AuthLayout from "../components/AuthLayout";
import FormField from "../components/FormField";
import api, { CONNECTION_ERROR_MESSAGE } from "../services/api";

const HTTP_STATUS_BAD_REQUEST = 400;
const HTTP_STATUS_CONFLICT = 409;

const getRegisterErrorMessage = (error) => {
  if (!error.response) {
    return CONNECTION_ERROR_MESSAGE;
  }

  if (error.response.status === HTTP_STATUS_CONFLICT) {
    return "Já existe uma conta com esse e-mail.";
  }

  // A API responde 400 com a lista de mensagens de validação do RegisterRequestDTO.
  if (error.response.status === HTTP_STATUS_BAD_REQUEST && Array.isArray(error.response.data)) {
    return error.response.data.join(" ");
  }

  return "Ocorreu um erro inesperado. Tente novamente.";
};

const RegisterPage = () => {
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm();

  const onSubmit = async ({ descricao, email, senha }) => {
    try {
      await api.post("/auth/register", { descricao, email, senha });

      navigate("/");
      Swal.fire({
        title: "Conta criada!",
        text: "Faça login para continuar.",
        icon: "success",
        confirmButtonText: "Ok",
      });
    } catch (error) {
      Swal.fire({
        title: "Não foi possível criar a conta",
        text: getRegisterErrorMessage(error),
        icon: "error",
        confirmButtonText: "Ok",
      });
    }
  };

  return (
    <AuthLayout title="Crie sua conta">
      <form
        noValidate
        onSubmit={handleSubmit(onSubmit)}
        className="flex flex-col gap-4 w-full sm:w-120 px-4"
      >
        <FormField
          id="descricao"
          label="Nome"
          placeholder="Insira o seu nome"
          error={errors.descricao}
          {...register("descricao", { required: "O nome é obrigatório." })}
        />

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

        <FormField
          id="confirmacaoSenha"
          label="Confirmar senha"
          type="password"
          placeholder="Insira a sua senha novamente"
          error={errors.confirmacaoSenha}
          {...register("confirmacaoSenha", {
            required: "Confirme a sua senha.",
            validate: (confirmacaoSenha, formValues) =>
              confirmacaoSenha === formValues.senha || "As senhas não conferem.",
          })}
        />

        <div className="flex flex-col gap-2">
          <button type="submit" className="btn btn-neutral" disabled={isSubmitting}>
            Cadastrar
          </button>

          <Link to="/" className="btn">
            Já tenho uma conta
          </Link>
        </div>
      </form>
    </AuthLayout>
  );
};

export default RegisterPage;
