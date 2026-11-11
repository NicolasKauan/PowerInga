import React from "react";
import Logo5 from "/Logo-5.png";
import UserPhoto from "/UserPhoto.png"

const UserHomePage = () => {
  return (
    <div className="min-h-screen flex flex-col">
      <header className="green-background flex justify-center md:justify-between items-center p-4">
        <div>
          <img src={Logo5} alt="logo-5" />
        </div>

        <div className="hidden">
          <img src={UserPhoto} alt="user-photo" />
        </div>
      </header>

      <section className="gray-background min-h-screen">
        <div>
          <h3>Saldo</h3>

          <span>$34.90</span>
        </div>

        <div>
          <div>
            <h3>Poste sua ação</h3>
          </div>
          <div>
            <h3>Consultar andamento</h3>
          </div>
          <div>
            <h3>Lojinha</h3>
          </div>
          <div>
            <h3>Ajuda</h3>
          </div>
        </div>
      </section>
    </div>
  );
};

export default UserHomePage;
