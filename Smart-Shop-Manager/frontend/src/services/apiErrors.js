export function getApiErrorMessage(error, fallback = "The request could not be completed. Please try again.") {
  if (!error) {
    return fallback;
  }

  if (!error.response && error.request) {
    return "The server did not respond. Check your connection and try again.";
  }

  if (error.response?.status === 401) {
    return "Your session expired. Please sign in again.";
  }

  return error.response?.data?.message ?? error.message ?? fallback;
}