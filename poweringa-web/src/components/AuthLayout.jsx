import Logo1 from "/Logo-1.png";

const AuthLayout = ({ title, children }) => {
  return (
    <div className="min-h-screen grid grid-cols-1 lg:grid-cols-2">
      <div className="hidden lg:flex flex-col items-center justify-center green-background">
        <img className="w-90 h-90" src={Logo1} alt="Logo PowerInga" />
      </div>

      <div className="flex flex-col items-center justify-center gap-4 py-8">
        <h1 className="text-2xl font-bold">{title}</h1>
        {children}
      </div>
    </div>
  );
};

export default AuthLayout;
