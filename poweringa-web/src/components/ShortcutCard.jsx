import { Link } from "react-router-dom";

const ShortcutCard = ({ to, label, Icon }) => {
  return (
    <Link to={to} className="card bg-base-100 w-full shadow-sm hover:shadow-md transition-shadow">
      <div className="card-body items-center">
        <Icon className="text-4xl" />
        <h2 className="text-2xl">{label}</h2>
      </div>
    </Link>
  );
};

export default ShortcutCard;
