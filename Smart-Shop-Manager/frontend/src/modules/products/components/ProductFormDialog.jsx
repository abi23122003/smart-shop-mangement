import { useState } from "react";
import { Alert, Button, Dialog, DialogActions, DialogContent, DialogTitle, MenuItem, Stack, TextField } from "@mui/material";

const initialProduct = { productCode: "", barcode: "", productName: "", brand: "", categoryId: "", variant: "", unit: "pcs", quantity: 0, purchasePrice: 0, sellingPrice: 0, minimumStock: 0, expiryApplicable: false, expiryDate: "", active: true };
const numericFields = new Set(["categoryId", "quantity", "purchasePrice", "sellingPrice", "minimumStock"]);

export default function ProductFormDialog({ open, product, categories, saving, onClose, onSubmit }) {
  const [form, setForm] = useState(() => product ? { ...initialProduct, ...product, categoryId: product.categoryId ?? "", expiryDate: product.expiryDate ?? "" } : initialProduct); const [error, setError] = useState("");
  function change(key, value) { setForm((current) => ({ ...current, [key]: numericFields.has(key) && value !== "" ? Number(value) : value })); }
  function submit(event) { event.preventDefault(); if (!form.productName.trim() || !form.barcode.trim() || form.categoryId === "") return setError("Name, barcode, and category are required."); if (form.quantity < 0 || form.purchasePrice < 0 || form.sellingPrice < 0 || form.minimumStock < 0) return setError("Stock and prices cannot be negative."); onSubmit(form); }
  return <Dialog open={open} onClose={() => !saving && onClose()} maxWidth="sm" fullWidth slotProps={{ paper: { component: "form", onSubmit: submit } }}><DialogTitle>{product ? "Edit product" : "Add product"}</DialogTitle><DialogContent><Stack spacing={2} sx={{ pt: 1 }}>{error && <Alert severity="error">{error}</Alert>}
    <TextField label="Barcode" required value={form.barcode} onChange={(e) => change("barcode", e.target.value)} />
    <TextField label="Product name" required value={form.productName} onChange={(e) => change("productName", e.target.value)} /><TextField label="Brand" value={form.brand} onChange={(e) => change("brand", e.target.value)} />
    <TextField label="Category" required select value={form.categoryId} onChange={(e) => change("categoryId", e.target.value)}>{categories.map((category) => <MenuItem key={category.id} value={category.id}>{category.name}</MenuItem>)}</TextField>
    <TextField label="Variant" value={form.variant} onChange={(e) => change("variant", e.target.value)} /><TextField label="Unit" value={form.unit} onChange={(e) => change("unit", e.target.value)} />
    <TextField label="Quantity" type="number" slotProps={{ htmlInput: { min: 0 } }} value={form.quantity} onChange={(e) => change("quantity", e.target.value)} /><TextField label="Minimum stock" type="number" slotProps={{ htmlInput: { min: 0 } }} value={form.minimumStock} onChange={(e) => change("minimumStock", e.target.value)} />
    <TextField label="Purchase price" type="number" slotProps={{ htmlInput: { min: 0, step: "0.01" } }} value={form.purchasePrice} onChange={(e) => change("purchasePrice", e.target.value)} /><TextField label="Selling price" type="number" slotProps={{ htmlInput: { min: 0, step: "0.01" } }} value={form.sellingPrice} onChange={(e) => change("sellingPrice", e.target.value)} />
    <TextField label="Status" select value={String(form.active)} onChange={(e) => change("active", e.target.value === "true")}><MenuItem value="true">Active</MenuItem><MenuItem value="false">Inactive</MenuItem></TextField>
  </Stack></DialogContent><DialogActions><Button onClick={onClose}>Cancel</Button><Button type="submit" variant="contained" disabled={saving}>{saving ? "Saving…" : "Save product"}</Button></DialogActions></Dialog>;
}
