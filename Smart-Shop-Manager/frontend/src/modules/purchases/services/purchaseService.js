import api from "../../../services/api";

export const getPurchases = async () => (await api.get("/purchases")).data;
export const createPurchase = (purchase) => api.post("/purchases", purchase);
export const getSuppliers = async () => (await api.get("/suppliers")).data;
export const getProducts = async () => (await api.get("/products")).data;
