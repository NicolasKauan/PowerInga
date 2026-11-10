import React from "react";
import Logo1 from "/Logo-1.png";
import api from "../services/api";
import { useForm } from "react-hook-form";

const LoginPage = () => {
  const { register, handleSubmit } = useForm();

  const onSubmit = (data) => {
    alert("Login enviado!");
  };

  return (
    <div className="min-h-screen grid grid-cols-1 lg:grid-cols-2">
      <div className="hidden lg:flex flex-col items-center justify-center green-background">
        <img className="w-90 h-90" src={Logo1} alt="logo-1" />
      </div>

      <div className="flex flex-col items-center justify-center gap-4">
        <h1 className="text-2xl font-bold">Faça seu login</h1>

        <form
          onSubmit={handleSubmit(onSubmit)}
          className="flex flex-col gap-4 w-full sm:w-120 px-4"
        >
          <fieldset className="fieldset">
            <label className="text-black label" htmlFor="usuario">
              Usuário
            </label>

            <input
              {...register("usuario")}
              type="text"
              id="usuario"
              className="input w-full"
              placeholder="Insira o seu usuário"
            />
          </fieldset>

          <fieldset className="fieldset">
            <label className="text-black label" htmlFor="senha">
              Senha
            </label>
            <input
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
