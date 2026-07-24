import api from "../../../services/api";

export const getSales = async () => (await api.get("/sales")).data;
export const createSale = (sale) => api.post("/sales", sale);
export const deleteSale = (id) => api.delete(`/sales/${id}`);
export const getCustomers = async () => (await api.get("/customers")).data;
export const getProducts = async () => (await api.get("/products")).data;
export const getInvoice = (id) => api.get(`/sales/${id}/invoice`, { responseType: "blob" });
