import React from "react";
import Logo1 from "/Logo-1.png";
import api from "../services/api";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";

import Swal from "sweetalert2";

const LoginPage = () => {
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const onSubmit = async (data) => {
    try {
      const response = await api.post("/auth/login", data);

      localStorage.setItem("token", response.data.token);

      const result = await Swal.fire({
        title: "Login bem sucedido!",
        text: "Cadastro realizado com sucesso!",
        icon: "success",
        confirmButtonText: "OK",
      });

      if (result.isConfirmed) {
        navigate("/home");
      }
    } catch (error) {
      console.log(error);

      Swal.fire({
        title: "Dados incorretos!",
        text: "Seu usuário ou senha estão incorretos.",
        icon: "error",
        confirmButtonText: "Ok",
      });
    }
  };

  const onError = () => {
    Swal.fire({
      title: "Erro no login!",
      text: "Verifique se os campos estão preenchidos corretamente",
      icon: "error",
      confirmButtonText: "Ok",
    });
  };

  return (
    <div className="min-h-screen grid grid-cols-1 lg:grid-cols-2">
      <div className="hidden lg:flex flex-col items-center justify-center green-background">
        <img className="w-90 h-90" src={Logo1} alt="logo-1" />
      </div>

      <div className="flex flex-col items-center justify-center gap-4">
        <h1 className="text-2xl font-bold">Faça seu login</h1>

        <form
          onSubmit={handleSubmit(onSubmit, onError)}
          className="flex flex-col gap-4 w-full sm:w-120 px-4"
        >
          <fieldset className="fieldset">
            <label className="text-black label" htmlFor="usuario">
              E-mail
            </label>

            <input
              {...register("email", {
                required: "O e-mail é obrigatório",
              })}
              type="email"
              id="email"
              className="input w-full"
              placeholder="Insira o seu e-mail"
            />
          </fieldset>

          <fieldset className="fieldset">
            <label className="text-black label" htmlFor="senha">
              Senha
            </label>
            <input
              {...register("senha", {
                required: "A senha é obrigatória",
              })}
              type="password"
              id="senha"
              className="input w-full"
              placeholder="Insira a sua senha"
            />
          </fieldset>

          <div className="flex flex-col gap-2">
            <button className="btn btn-neutral">Entrar</button>
            <button className="btn">Criar uma conta</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default LoginPage;
