const RequestFeedback = ({ isLoading, errorMessage }) => {
  if (isLoading) {
    return (
      <div className="flex justify-center p-8">
        <span className="loading loading-spinner loading-lg" />
      </div>
    );
  }

  if (errorMessage) {
    return (
      <div role="alert" className="alert alert-error">
        {errorMessage}
      </div>
    );
  }

  return null;
};

export default RequestFeedback;
