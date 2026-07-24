import api from "../../../services/api";

export async function getReport(type) {
  const { data } = await api.get(`/reports/${type}`);
  return data;
}
