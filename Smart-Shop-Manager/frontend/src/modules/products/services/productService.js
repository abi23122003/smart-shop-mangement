import api from "../../../services/api";

export async function getProductPage(page, size) {
  const { data } = await api.get("/products/page", { params: { page, size } });
  return data;
}

export async function searchProducts(keyword) {
  const { data } = await api.get("/products/search", { params: { keyword } });
  return data;
}

export async function getCategories() {
  const { data } = await api.get("/categories");
  return data;
}

export const createProduct = (product) => api.post("/products", product);
export const updateProduct = (id, product) => api.put(`/products/${id}`, product);
export const deleteProduct = (id) => api.delete(`/products/${id}`);
export const restockProduct = (barcode, quantity) => api.post("/products/restock", { barcode, quantity });
