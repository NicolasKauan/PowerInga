const FormField = ({ id, label, error, ...inputProps }) => {
  return (
    <fieldset className="fieldset">
      <label className="label text-black" htmlFor={id}>
        {label}
      </label>

      <input
        id={id}
        className={`input w-full ${error ? "input-error" : ""}`}
        aria-invalid={Boolean(error)}
        {...inputProps}
      />

      {error && <span className="text-error text-sm">{error.message}</span>}
    </fieldset>
  );
};

export default FormField;
