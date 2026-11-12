import Header from "./Header";

const PageLayout = ({ title, children }) => {
  return (
    <div className="min-h-screen flex flex-col">
      <Header />

      <main className="gray-background flex-1 flex flex-col items-center p-4 md:p-8">
        <div className="flex flex-col gap-6 max-w-6xl w-full">
          {title && <h1 className="text-2xl font-bold">{title}</h1>}
          {children}
        </div>
      </main>
    </div>
  );
};

export default PageLayout;
