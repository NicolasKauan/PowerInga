import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";

import PageLayout from "../components/PageLayout";
import { REWARD_CATEGORIES } from "../constants/rewardCategories";
import api from "../services/api";
import { getApiErrorMessage } from "../services/apiErrorMessage";

const UserPostActionPage = () => {
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm({ defaultValues: { descricao: "", categoria: "" } });

  const onSubmit = async ({ descricao, categoria }) => {
    try {
      await api.post("/solicitacoes", { descricao, categoria });
    } catch (error) {
      Swal.fire({
        title: "Não foi possível enviar a ação",
        text: getApiErrorMessage(error),
        icon: "error",
        confirmButtonText: "Ok",
      });
      return;
    }

    navigate("/andamento");
    Swal.fire({
      title: "Ação enviada!",
      text: "Ela será analisada por um gestor. Acompanhe o andamento por aqui.",
      icon: "success",
      confirmButtonText: "Ok",
    });
  };

  return (
    <PageLayout title="Poste sua ação">
      <form
        noValidate
        onSubmit={handleSubmit(onSubmit)}
        className="card bg-base-100 shadow-sm card-body flex flex-col gap-4"
      >
        <fieldset className="fieldset">
          <label className="label text-black" htmlFor="descricao">
            O que você fez?
          </label>
          <textarea
            id="descricao"
            rows={4}
            placeholder="Descreva a sua ação sustentável"
            className={`textarea w-full ${errors.descricao ? "textarea-error" : ""}`}
            aria-invalid={Boolean(errors.descricao)}
            {...register("descricao", {
              validate: (descricao) => descricao.trim() !== "" || "Descreva a sua ação.",
            })}
          />
          {errors.descricao && <span className="text-error text-sm">{errors.descricao.message}</span>}
        </fieldset>

        <fieldset className="fieldset">
          <label className="label text-black" htmlFor="categoria">
            Categoria
          </label>
          <select
            id="categoria"
            className={`select w-full ${errors.categoria ? "select-error" : ""}`}
            aria-invalid={Boolean(errors.categoria)}
            {...register("categoria", { required: "Escolha uma categoria." })}
          >
            <option value="" disabled>
              Escolha a categoria da ação
            </option>
            {REWARD_CATEGORIES.map((category) => (
              <option key={category.value} value={category.value}>
                {category.label} ({category.points} pontos)
              </option>
            ))}
          </select>
          {errors.categoria && <span className="text-error text-sm">{errors.categoria.message}</span>}
        </fieldset>

        <button type="submit" className="btn btn-neutral" disabled={isSubmitting}>
          Enviar ação
        </button>
      </form>
    </PageLayout>
  );
};

export default UserPostActionPage;
