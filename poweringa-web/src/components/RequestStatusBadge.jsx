import { REQUEST_STATUS_DISPLAY } from "../constants/requestStatus";

const RequestStatusBadge = ({ status }) => {
  const { label, badgeClass } = REQUEST_STATUS_DISPLAY[status];

  return <span className={`badge ${badgeClass}`}>{label}</span>;
};

export default RequestStatusBadge;
