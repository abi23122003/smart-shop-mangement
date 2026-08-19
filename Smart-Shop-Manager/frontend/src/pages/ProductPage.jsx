import { useCallback, useEffect, useState } from "react";
import { Html5Qrcode } from "html5-qrcode";
import AddOutlinedIcon from "@mui/icons-material/AddOutlined";
import DeleteOutlineIcon from "@mui/icons-material/Delete";
import EditOutlinedIcon from "@mui/icons-material/EditOutlined";
import QrCodeScannerOutlinedIcon from "@mui/icons-material/QrCodeScannerOutlined";
import SearchIcon from "@mui/icons-material/Search";
import { Alert, Box, Button, Chip, CircularProgress, Dialog, DialogActions, DialogContent, DialogTitle, IconButton, InputAdornment, Pagination, Paper, Stack, Table, TableBody, TableCell, TableHead, TableRow, TextField, Typography } from "@mui/material";
import ProductFormDialog from "../modules/products/components/ProductFormDialog";
import { createProduct, deleteProduct, getCategories, getProductPage, restockProduct, searchProducts, updateProduct } from "../modules/products/services/productService";

const PAGE_SIZE = 10;
const errorMessage = (error) => error.response?.data?.message ?? "The product request could not be completed.";
const stockStatus = (product) => product.quantity === 0 ? ["Out of stock", "error"] : product.quantity <= product.minimumStock ? ["Low stock", "warning"] : ["In stock", "success"];
const currency = (value) => `₹${Number(value ?? 0).toLocaleString("en-IN", { maximumFractionDigits: 2 })}`;

export default function ProductPage() {
  const [products, setProducts] = useState([]); const [categories, setCategories] = useState([]); const [page, setPage] = useState(0); const [totalPages, setTotalPages] = useState(1); const [search, setSearch] = useState(""); const [loading, setLoading] = useState(true); const [error, setError] = useState(""); const [cameraError, setCameraError] = useState(""); const [scanMode, setScanMode] = useState("search"); const [scanValue, setScanValue] = useState(""); const [dialog, setDialog] = useState(false); const [scannerOpen, setScannerOpen] = useState(false); const [restockOpen, setRestockOpen] = useState(false); const [restockProductDetails, setRestockProductDetails] = useState(null); const [restockBarcode, setRestockBarcode] = useState(""); const [restockQuantity, setRestockQuantity] = useState(""); const [restockSaving, setRestockSaving] = useState(false); const [selected, setSelected] = useState(null); const [saving, setSaving] = useState(false);
  const load = useCallback(async (targetPage = page, keyword = search) => { setLoading(true); try { setError(""); if (keyword.trim()) { const matches = await searchProducts(keyword.trim()); setProducts(matches.slice(targetPage * PAGE_SIZE, (targetPage + 1) * PAGE_SIZE)); setTotalPages(Math.max(1, Math.ceil(matches.length / PAGE_SIZE))); } else { const result = await getProductPage(targetPage, PAGE_SIZE); setProducts(result.content); setTotalPages(Math.max(1, result.totalPages)); } } catch (requestError) { setError(errorMessage(requestError)); } finally { setLoading(false); } }, [page, search]);
  useEffect(() => { getCategories().then(setCategories).catch(() => setError("Categories could not be loaded. Add or restore a category before saving a product.")); }, []);
  useEffect(() => { const timer = setTimeout(load, 0); return () => clearTimeout(timer); }, [load]);
  useEffect(() => {
    if (!scannerOpen) return undefined;
    let scanner;
    let startTimer;
    let started = false;
    let cancelled = false;
    setCameraError("");
    const startCamera = async () => {
      try {
        const cameraElement = document.getElementById("product-camera-reader");
        if (!cameraElement) {
          setCameraError("Camera view is not ready. Close and open the scanner again, or enter the barcode below.");
          return;
        }
        scanner = new Html5Qrcode(cameraElement.id);
        await scanner.start(
          { facingMode: "environment" },
          { fps: 10, qrbox: { width: 260, height: 160 } },
          (decodedText) => handleScannedBarcode(decodedText),
          () => {},
        );
        started = true;
        if (cancelled) {
          await scanner.stop();
          started = false;
        }
      } catch {
        if (!cancelled) {
          setCameraError("Camera could not start. Allow camera access or enter the barcode below.");
        }
      }
    };
    startTimer = window.setTimeout(startCamera, 150);
    return () => {
      cancelled = true;
      window.clearTimeout(startTimer);
      if (scanner && started) {
        scanner.stop()
          .then(() => {
            started = false;
          })
          .catch(() => {});
      }
    };
  }, [scannerOpen]);
  function changeSearch(value) { setSearch(value); setPage(0); }
  function openScanner(mode) { setScanMode(mode); setScanValue(""); setScannerOpen(true); }
  async function handleScannedBarcode(value) {
    const barcode = value.trim();
    if (!barcode) return;
    setScannerOpen(false);
    if (scanMode === "search") {
      changeSearch(barcode);
      return;
    }
    setRestockBarcode(barcode);
    setRestockQuantity("");
    setRestockProductDetails(null);
    setRestockOpen(true);
    try {
      const matches = await searchProducts(barcode);
      const product = matches.find((entry) => String(entry.barcode).trim() === barcode);
      if (!product) throw new Error("No product found for this barcode.");
      setRestockProductDetails(product);
    } catch (requestError) {
      setError(requestError.message === "No product found for this barcode." ? requestError.message : errorMessage(requestError));
    }
  }
  async function submitRestock(event) {
    event.preventDefault();
    const quantity = Number(restockQuantity);
    if (!restockProductDetails || !quantity || quantity <= 0) {
      setError("Scan an existing product and enter a quantity greater than zero.");
      return;
    }
    setRestockSaving(true);
    try {
      await restockProduct(restockBarcode, quantity);
      setRestockOpen(false);
      setError("");
      await load(0, search);
      setPage(0);
    } catch (requestError) {
      setError(errorMessage(requestError));
    } finally {
      setRestockSaving(false);
    }
  }
  async function save(product) { setSaving(true); try { if (selected) await updateProduct(selected.id, product); else await createProduct(product); setDialog(false); await load(0, search); setPage(0); } catch (requestError) { setError(errorMessage(requestError)); } finally { setSaving(false); } }
  async function remove(id) { if (!window.confirm("Delete this product? This cannot be undone.")) return; try { await deleteProduct(id); await load(); } catch (requestError) { setError(errorMessage(requestError)); } }
  return <Stack spacing={3}><Stack direction={{ xs: "column", md: "row" }} spacing={2} sx={{ justifyContent: "space-between" }}><Box><Typography variant="h4">Products</Typography><Typography color="text.secondary">Manage inventory, dealer prices, selling prices, and stock levels.</Typography></Box><Stack direction={{ xs: "column", sm: "row" }} spacing={1}><Button variant="outlined" startIcon={<QrCodeScannerOutlinedIcon />} onClick={() => openScanner("restock")}>Restock by scan</Button><Button variant="contained" startIcon={<AddOutlinedIcon />} onClick={() => { setSelected(null); setDialog(true); }}>Add product</Button></Stack></Stack>
    {error && <Alert severity="error" onClose={() => setError("")}>{error}</Alert>}<Stack direction={{ xs: "column", sm: "row" }} spacing={1}><TextField fullWidth label="Search by product name or barcode" value={search} onChange={(e) => changeSearch(e.target.value)} slotProps={{ input: { startAdornment: <InputAdornment position="start"><SearchIcon /></InputAdornment> } }} /><Button variant="outlined" startIcon={<QrCodeScannerOutlinedIcon />} onClick={() => openScanner("search")} sx={{ minWidth: { sm: 150 } }}>Scan to find</Button></Stack>
    <Paper sx={{ overflowX: "auto" }}><Table><TableHead><TableRow><TableCell>Product</TableCell><TableCell>Barcode</TableCell><TableCell>Stock</TableCell><TableCell>Status</TableCell><TableCell align="right">Dealer price</TableCell><TableCell align="right">Selling price</TableCell><TableCell align="right">Actions</TableCell></TableRow></TableHead><TableBody>{loading ? <TableRow><TableCell colSpan={7} align="center"><CircularProgress size={24} /></TableCell></TableRow> : products.map((product) => { const [label, color] = stockStatus(product); return <TableRow key={product.id} hover><TableCell><Typography fontWeight={600}>{product.productName}</Typography><Typography variant="body2" color="text.secondary">{product.brand || "No brand"}</Typography></TableCell><TableCell>{product.barcode || "—"}</TableCell><TableCell><Typography fontWeight={650}>{product.quantity ?? 0} {product.unit || "units"}</Typography><Typography variant="body2" color="text.secondary">Minimum {product.minimumStock ?? 0}</Typography></TableCell><TableCell><Chip label={label} color={color} size="small" /></TableCell><TableCell align="right">{currency(product.purchasePrice)}</TableCell><TableCell align="right" sx={{ fontWeight: 650 }}>{currency(product.sellingPrice)}</TableCell><TableCell align="right"><IconButton aria-label="Edit product" onClick={() => { setSelected(product); setDialog(true); }}><EditOutlinedIcon /></IconButton><IconButton aria-label="Delete product" color="error" onClick={() => remove(product.id)}><DeleteOutlineIcon /></IconButton></TableCell></TableRow>; })}{!loading && !products.length && <TableRow><TableCell colSpan={7} align="center">No products found.</TableCell></TableRow>}</TableBody></Table></Paper>
    <Box sx={{ display: "flex", justifyContent: "center" }}><Pagination count={totalPages} page={page + 1} onChange={(_, value) => setPage(value - 1)} color="primary" /></Box>{dialog && <ProductFormDialog key={selected?.id ?? "new"} open={dialog} product={selected} categories={categories} saving={saving} onClose={() => setDialog(false)} onSubmit={save} />}
    <Dialog open={scannerOpen} onClose={() => setScannerOpen(false)} maxWidth="sm" fullWidth><DialogTitle>{scanMode === "restock" ? "Restock by barcode" : "Scan product barcode"}</DialogTitle><DialogContent><Stack spacing={2} sx={{ pt: 1 }}><Typography color="text.secondary">Point your phone camera at the barcode. Allow camera access when the browser asks.</Typography><Box id="product-camera-reader" sx={{ width: "100%", minHeight: 220, overflow: "hidden", borderRadius: 1, bgcolor: "grey.100", "& video": { width: "100%" } }} />{cameraError && <Alert severity="warning">{cameraError}</Alert>}<TextField autoFocus fullWidth label="Enter barcode manually" value={scanValue} onChange={(event) => setScanValue(event.target.value)} onKeyDown={(event) => { if (event.key === "Enter") { event.preventDefault(); handleScannedBarcode(event.currentTarget.value); } }} placeholder="Use this if camera scanning is unavailable" /></Stack></DialogContent><DialogActions><Button onClick={() => setScannerOpen(false)}>Close</Button><Button variant="contained" onClick={() => handleScannedBarcode(scanValue)}>{scanMode === "restock" ? "Load product" : "Find product"}</Button></DialogActions></Dialog>
    <Dialog open={restockOpen} onClose={() => !restockSaving && setRestockOpen(false)} maxWidth="sm" fullWidth slotProps={{ paper: { component: "form", onSubmit: submitRestock } }}><DialogTitle>Restock product</DialogTitle><DialogContent><Stack spacing={2} sx={{ pt: 1 }}>{restockProductDetails ? <><Typography variant="h6">{restockProductDetails.productName}</Typography><Typography color="text.secondary">Category: {categories.find((category) => Number(category.id) === Number(restockProductDetails.categoryId))?.name ?? "—"} · Variant: {restockProductDetails.variant || "—"}</Typography><Stack direction="row" spacing={1} sx={{ flexWrap: "wrap" }}><Chip label={`Dealer ${currency(restockProductDetails.purchasePrice)}`} /><Chip label={`Selling ${currency(restockProductDetails.sellingPrice)}`} /><Chip label={`Current stock ${restockProductDetails.quantity ?? 0} ${restockProductDetails.unit || "units"}`} /></Stack></> : <CircularProgress size={26} />}<TextField label="Restock quantity" required type="number" value={restockQuantity} onChange={(event) => setRestockQuantity(event.target.value)} slotProps={{ htmlInput: { min: 1 } }} disabled={!restockProductDetails} /><Typography variant="body2" color="text.secondary">Barcode: {restockBarcode}</Typography></Stack></DialogContent><DialogActions><Button onClick={() => setRestockOpen(false)}>Cancel</Button><Button type="submit" variant="contained" disabled={restockSaving || !restockProductDetails}>{restockSaving ? "Updating..." : "Update stock"}</Button></DialogActions></Dialog>
  </Stack>;
}
