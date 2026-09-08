export function productLabel(product, { includeStock = false } = {}) {
  if (!product) return "Unknown product";
  const identity = [product.brand, product.subcategory, product.variant].filter(Boolean).join(" • ");
  const stock = includeStock ? ` (${product.quantity ?? 0} ${product.unit || "units"})` : "";
  return `${product.productName}${identity ? ` — ${identity}` : ""}${stock}`;
}
