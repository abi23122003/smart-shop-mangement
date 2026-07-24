import api from "../../../services/api";

export async function getDashboard() {
  const { data } = await api.get("/dashboard");
  return data;
}
