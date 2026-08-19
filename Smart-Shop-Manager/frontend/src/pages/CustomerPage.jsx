import { useEffect, useMemo, useState } from "react";
import AddOutlinedIcon from "@mui/icons-material/AddOutlined";
import EditOutlinedIcon from "@mui/icons-material/EditOutlined";
import MessageOutlinedIcon from "@mui/icons-material/MessageOutlined";
import VisibilityOutlinedIcon from "@mui/icons-material/VisibilityOutlined";
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  Chip,
  CircularProgress,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  IconButton,
  MenuItem,
  Paper,
  Stack,
  Tab,
  Tabs,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TextField,
  Typography,
} from "@mui/material";
import api from "../services/api";

const currency = (value) =>
  `₹${Number(value ?? 0).toLocaleString("en-IN", { maximumFractionDigits: 2 })}`;
const emptyCustomer = { customerName: "", phone: "", creditEnabled: false };
const errorMessage = (error) =>
  error.response?.data?.message ?? "Could not load customer records.";

export default function CustomerPage() {
  const [customers, setCustomers] = useState([]);
  const [sales, setSales] = useState([]);
  const [products, setProducts] = useState([]);
  const [credits, setCredits] = useState([]);
  const [search, setSearch] = useState("");
  const [tab, setTab] = useState("credit");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [selected, setSelected] = useState(null);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [customerDialogOpen, setCustomerDialogOpen] = useState(false);
  const [purchaseOpen, setPurchaseOpen] = useState(false);
  const [paymentOpen, setPaymentOpen] = useState(false);
  const [purchaseCustomer, setPurchaseCustomer] = useState(null);
  const [paymentCustomer, setPaymentCustomer] = useState(null);
  const [paymentAmount, setPaymentAmount] = useState("");
  const [paymentSaving, setPaymentSaving] = useState(false);
  const [purchaseForm, setPurchaseForm] = useState({
    productId: "",
    quantity: 1,
    paymentMethod: "Cash",
  });
  const [form, setForm] = useState(emptyCustomer);
  const [saving, setSaving] = useState(false);
  const [purchaseSaving, setPurchaseSaving] = useState(false);

  async function load() {
    setLoading(true);
    try {
      const [customerResponse, salesResponse, productResponse, creditResponse] =
        await Promise.all([
          api.get("/customers"),
          api.get("/sales"),
          api.get("/products"),
          api.get("/credits"),
        ]);
      setCustomers(customerResponse.data ?? []);
      setSales(salesResponse.data ?? []);
      setProducts(productResponse.data ?? []);
      setCredits(creditResponse.data ?? []);
      setError("");
    } catch (requestError) {
      setError(errorMessage(requestError));
    } finally {
      setLoading(false);
    }
  }
  useEffect(() => {
    load();
  }, []);

  const rows = useMemo(() => {
    const keyword = search.trim().toLowerCase();
    return customers
      .map((customer) => {
        const customerSales = sales.filter(
          (sale) => Number(sale.customerId) === Number(customer.id),
        );
        const credit = credits.find(
          (item) => Number(item.customerId) === Number(customer.id),
        );
        const hasCreditSale = customerSales.some(
          (sale) => sale.paymentMethod?.toLowerCase() === "credit",
        );
        const instantSales = customerSales.filter(
          (sale) => sale.paymentMethod?.toLowerCase() !== "credit",
        );
        const productsBought = customerSales.flatMap((sale) =>
          (sale.saleItems ?? []).map((item) => {
            const product = products.find(
              (entry) => Number(entry.id) === Number(item.productId),
            );
            return `${product?.productName ?? `Product #${item.productId}`} x${item.quantity}`;
          }),
        );
        const paymentMethods = [
          ...new Set(customerSales.map((sale) => sale.paymentMethod || "Cash")),
        ];
        return {
          ...customer,
          customerSales,
          credit,
          productsBought,
          paymentMethods,
          hasCredit:
            Boolean(customer.creditEnabled) ||
            hasCreditSale ||
            Number(credit?.balance ?? 0) > 0,
          hasInstant: instantSales.length > 0,
          totalSpent: customerSales.reduce(
            (sum, sale) => sum + Number(sale.totalAmount ?? 0),
            0,
          ),
          lastPurchase: [...customerSales].sort(
            (a, b) => new Date(b.saleDate ?? 0) - new Date(a.saleDate ?? 0),
          )[0],
        };
      })
      .filter(
        (customer) =>
          !keyword ||
          `${customer.customerName} ${customer.phone}`
            .toLowerCase()
            .includes(keyword),
      )
      .filter(
        (customer) =>
          tab === "all" ||
          (tab === "credit" ? customer.hasCredit : customer.hasInstant),
      );
  }, [customers, sales, products, credits, search, tab]);

  const summary = useMemo(
    () => ({
      creditCustomers: customers.filter((customer) => {
        const credit = credits.find(
          (item) => Number(item.customerId) === Number(customer.id),
        );
        return (
          Boolean(customer.creditEnabled) ||
          Number(credit?.balance ?? 0) > 0 ||
          sales.some(
            (sale) =>
              Number(sale.customerId) === Number(customer.id) &&
              sale.paymentMethod?.toLowerCase() === "credit",
          )
        );
      }).length,
      outstanding: credits.reduce(
        (sum, credit) => sum + Number(credit.balance ?? 0),
        0,
      ),
      instantSales: sales.filter(
        (sale) => sale.paymentMethod?.toLowerCase() !== "credit",
      ).length,
    }),
    [customers, sales, credits],
  );

  function openEdit(customer) {
    setForm({
      ...emptyCustomer,
      ...customer,
      creditEnabled:
        customer.creditEnabled ?? Number(customer.creditLimit ?? 0) > 0,
    });
    setSelected(customer);
    setCustomerDialogOpen(true);
  }
  function openCreate() {
    setForm(emptyCustomer);
    setSelected(null);
    setCustomerDialogOpen(true);
  }
  async function saveCustomer(event) {
    event.preventDefault();
    const phone = form.phone.replace(/\D/g, "");
    if (!/^\d{10}$/.test(phone)) {
      setError("Phone number must contain exactly 10 digits.");
      return;
    }
    setSaving(true);
    try {
      const payload = { ...form, phone, active: true, creditLimit: 0 };
      if (selected) await api.put(`/customers/${selected.id}`, payload);
      else await api.post("/customers", payload);
      setCustomerDialogOpen(false);
      await load();
    } catch (requestError) {
      setError(errorMessage(requestError));
    } finally {
      setSaving(false);
    }
  }
  function whatsapp(customer, sale = null) {
    const credit = customer.credit;
    const message = sale
      ? `Hello ${customer.customerName}, your purchase ${sale.saleCode} on ${sale.saleDate} was ${currency(sale.totalAmount)} on credit. Your current balance is ${currency(credit?.balance)}.`
      : `Hello ${customer.customerName}, this is a reminder that your current credit balance is ${currency(credit?.balance)}. Please make the payment at your convenience.`;
    window.open(
      `https://wa.me/${String(customer.phone ?? "").replace(/\D/g, "")}?text=${encodeURIComponent(message)}`,
      "_blank",
      "noopener,noreferrer",
    );
  }
  function openDailyPurchase(customer = null) {
    const creditEnabled = customer
      ? (customer.creditEnabled ?? Number(customer.creditLimit ?? 0) > 0)
      : false;
    setPurchaseCustomer(customer ? { ...customer, creditEnabled } : null);
    setPurchaseForm({ productId: "", quantity: 1, paymentMethod: "Cash" });
    setPurchaseOpen(true);
  }
  function openPayment(customer = null) {
    setPaymentCustomer(customer);
    setPaymentAmount("");
    setPaymentOpen(true);
  }
  async function savePayment(event) {
    event.preventDefault();
    const amount = Number(paymentAmount);
    if (!paymentCustomer || !amount || amount <= 0) {
      setError("Choose a credit customer and enter a payment amount.");
      return;
    }
    setPaymentSaving(true);
    try {
      await api.post("/credits/payment", {
        customerId: paymentCustomer.id,
        amount,
        remarks: "Customer payment",
      });
      setPaymentOpen(false);
      setError("");
      await load();
    } catch (requestError) {
      setError(errorMessage(requestError));
    } finally {
      setPaymentSaving(false);
    }
  }
  function selectPurchaseProduct(productId) {
    const product = products.find(
      (entry) => Number(entry.id) === Number(productId),
    );
    setPurchaseForm({
      ...purchaseForm,
      productId,
      sellingPrice: product?.sellingPrice ?? 0,
    });
  }
  async function saveDailyPurchase(event) {
    event.preventDefault();
    const product = products.find(
      (entry) => Number(entry.id) === Number(purchaseForm.productId),
    );
    const quantity = Number(purchaseForm.quantity);
    if (
      !purchaseCustomer ||
      !product ||
      quantity <= 0 ||
      quantity > Number(product.quantity ?? 0)
    ) {
      setError("Choose a customer, available product, and valid quantity.");
      return;
    }
    setPurchaseSaving(true);
    try {
      await api.post("/sales", {
        saleCode: `CUS-${Date.now()}`,
        saleDate: new Date().toISOString().slice(0, 10),
        customerId: purchaseCustomer.id,
        paymentMethod: purchaseForm.paymentMethod,
        saleItems: [
          {
            productId: product.id,
            quantity,
            sellingPrice: Number(product.sellingPrice ?? 0),
            totalPrice: quantity * Number(product.sellingPrice ?? 0),
          },
        ],
      });
      setPurchaseOpen(false);
      setError("");
      await load();
    } catch (requestError) {
      setError(errorMessage(requestError));
    } finally {
      setPurchaseSaving(false);
    }
  }

  return (
    <Stack spacing={3}>
      <Stack
        direction={{ xs: "column", sm: "row" }}
        spacing={2}
        sx={{ justifyContent: "space-between", alignItems: { sm: "center" } }}
      >
        <Box>
          <Typography variant="h4">Customers</Typography>
          <Typography color="text.secondary">
            See what each customer bought, how they paid, and what remains due.
          </Typography>
        </Box>
        <Stack direction="row" spacing={1}>
          <Button variant="outlined" onClick={() => openDailyPurchase()}>
            Add daily purchase
          </Button>
          <Button variant="outlined" onClick={() => openPayment()}>
            Record payment
          </Button>
          <Button
            variant="contained"
            startIcon={<AddOutlinedIcon />}
            onClick={openCreate}
          >
            Add customer
          </Button>
        </Stack>
      </Stack>
      {error && (
        <Alert severity="error" onClose={() => setError("")}>
          {error}
        </Alert>
      )}
      <Stack direction={{ xs: "column", sm: "row" }} spacing={2}>
        <Card sx={{ flex: 1 }}>
          <CardContent>
            <Typography color="text.secondary">Credit customers</Typography>
            <Typography variant="h5">{summary.creditCustomers}</Typography>
          </CardContent>
        </Card>
        <Card sx={{ flex: 1 }}>
          <CardContent>
            <Typography color="text.secondary">Outstanding credit</Typography>
            <Typography variant="h5">
              {currency(summary.outstanding)}
            </Typography>
          </CardContent>
        </Card>
        <Card sx={{ flex: 1 }}>
          <CardContent>
            <Typography color="text.secondary">Instant purchases</Typography>
            <Typography variant="h5">{summary.instantSales}</Typography>
          </CardContent>
        </Card>
      </Stack>
      <Stack direction={{ xs: "column", sm: "row" }} spacing={2}>
        <TextField
          fullWidth
          label="Search name or phone"
          value={search}
          onChange={(event) => setSearch(event.target.value)}
        />
        <Tabs
          value={tab}
          onChange={(_, value) => setTab(value)}
          variant="scrollable"
        >
          <Tab value="credit" label="Credit customers" />
          <Tab value="instant" label="Cash / UPI" />
          <Tab value="all" label="All customers" />
        </Tabs>
      </Stack>
      <Paper sx={{ overflowX: "auto" }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Customer</TableCell>
              <TableCell>Phone</TableCell>
              <TableCell>Products bought</TableCell>
              <TableCell>Payment</TableCell>
              <TableCell>Purchases</TableCell>
              <TableCell>Last purchase</TableCell>
              <TableCell align="right">Total spent</TableCell>
              <TableCell align="right">Balance</TableCell>
              <TableCell align="right">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {loading ? (
              <TableRow>
                <TableCell colSpan={9} align="center">
                  <CircularProgress size={25} />
                </TableCell>
              </TableRow>
            ) : (
              rows.map((customer) => (
                <TableRow key={customer.id} hover>
                  <TableCell>
                    <Typography fontWeight={650}>
                      {customer.customerName}
                    </Typography>
                  </TableCell>
                  <TableCell>{customer.phone || "—"}</TableCell>
                  <TableCell sx={{ minWidth: 220 }}>
                    {customer.productsBought.length
                      ? customer.productsBought.join(", ")
                      : "—"}
                  </TableCell>
                  <TableCell>
                    {customer.paymentMethods.length
                      ? customer.paymentMethods.map((method) => (
                          <Chip
                            key={method}
                            size="small"
                            label={method}
                            color={
                              method.toLowerCase() === "credit"
                                ? "warning"
                                : "success"
                            }
                            sx={{ mr: 0.5, mb: 0.5 }}
                          />
                        ))
                      : "—"}
                  </TableCell>
                  <TableCell>{customer.customerSales.length}</TableCell>
                  <TableCell>
                    {customer.lastPurchase?.saleDate ?? "—"}
                  </TableCell>
                  <TableCell align="right">
                    {currency(customer.totalSpent)}
                  </TableCell>
                  <TableCell align="right">
                    <Chip
                      label={currency(customer.credit?.balance)}
                      color={
                        Number(customer.credit?.balance ?? 0) > 0
                          ? "warning"
                          : "success"
                      }
                      size="small"
                    />
                  </TableCell>
                  <TableCell align="right">
                    <IconButton
                      aria-label="View purchases"
                      onClick={() => {
                        setSelected(customer);
                        setDialogOpen(true);
                      }}
                    >
                      <VisibilityOutlinedIcon />
                    </IconButton>
                    <IconButton
                      aria-label="Edit customer"
                      onClick={() => openEdit(customer)}
                    >
                      <EditOutlinedIcon />
                    </IconButton>
                    {customer.hasCredit && (
                      <Button
                        size="small"
                        startIcon={<MessageOutlinedIcon />}
                        onClick={() => whatsapp(customer)}
                      >
                        Send reminder
                      </Button>
                    )}
                  </TableCell>
                </TableRow>
              ))
            )}
            {!loading && !rows.length && (
              <TableRow>
                <TableCell colSpan={9} align="center">
                  No customers in this section.
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </Paper>
      <Dialog
        open={dialogOpen}
        onClose={() => setDialogOpen(false)}
        maxWidth="md"
        fullWidth
      >
        <DialogTitle>{selected?.customerName} purchase history</DialogTitle>
        <DialogContent>
          <Stack spacing={2} sx={{ pt: 1 }}>
            <Stack direction="row" spacing={1} sx={{ flexWrap: "wrap" }}>
              <Chip
                label={`Balance ${currency(selected?.credit?.balance)}`}
                color="warning"
              />
              <Chip
                label={`${selected?.customerSales?.length ?? 0} purchases`}
                variant="outlined"
              />
              {selected?.hasCredit && (
                <Button
                  size="small"
                  startIcon={<MessageOutlinedIcon />}
                  onClick={() => whatsapp(selected)}
                >
                  WhatsApp reminder
                </Button>
              )}
            </Stack>
            <Paper variant="outlined" sx={{ overflowX: "auto" }}>
              <Table size="small">
                <TableHead>
                  <TableRow>
                    <TableCell>Date</TableCell>
                    <TableCell>Invoice</TableCell>
                    <TableCell>Products</TableCell>
                    <TableCell>Payment</TableCell>
                    <TableCell align="right">Total</TableCell>
                    <TableCell align="right">Action</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {selected?.customerSales?.map((sale) => (
                    <TableRow key={sale.id}>
                      <TableCell>{sale.saleDate}</TableCell>
                      <TableCell>{sale.saleCode}</TableCell>
                      <TableCell>
                        {sale.saleItems
                          ?.map((item) => {
                            const product = products.find(
                              (entry) =>
                                Number(entry.id) === Number(item.productId),
                            );
                            return `${product?.productName ?? `Product #${item.productId}`} x${item.quantity}`;
                          })
                          .join(", ") || "—"}
                      </TableCell>
                      <TableCell>
                        <Chip
                          size="small"
                          label={sale.paymentMethod || "Cash"}
                          color={
                            sale.paymentMethod?.toLowerCase() === "credit"
                              ? "warning"
                              : "success"
                          }
                        />
                      </TableCell>
                      <TableCell align="right">
                        {currency(sale.totalAmount)}
                      </TableCell>
                      <TableCell align="right">
                        {sale.paymentMethod?.toLowerCase() === "credit" && (
                          <IconButton
                            aria-label="Send purchase WhatsApp message"
                            onClick={() => whatsapp(selected, sale)}
                          >
                            <MessageOutlinedIcon />
                          </IconButton>
                        )}
                      </TableCell>
                    </TableRow>
                  ))}
                  {!selected?.customerSales?.length && (
                    <TableRow>
                      <TableCell colSpan={6} align="center">
                        No purchases recorded.
                      </TableCell>
                    </TableRow>
                  )}
                </TableBody>
              </Table>
            </Paper>
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDialogOpen(false)}>Close</Button>
        </DialogActions>
      </Dialog>
      <Dialog
        open={customerDialogOpen}
        onClose={() => !saving && setCustomerDialogOpen(false)}
        maxWidth="sm"
        fullWidth
        slotProps={{ paper: { component: "form", onSubmit: saveCustomer } }}
      >
        <DialogTitle>{selected ? "Edit customer" : "Add customer"}</DialogTitle>
        <DialogContent>
          <Stack spacing={2} sx={{ pt: 1 }}>
            <TextField
              label="Customer name"
              required
              value={form.customerName}
              onChange={(event) =>
                setForm({ ...form, customerName: event.target.value })
              }
            />
            <TextField
              label="Phone (10 digits)"
              required
              value={form.phone}
              onChange={(event) =>
                setForm({
                  ...form,
                  phone: event.target.value.replace(/\D/g, "").slice(0, 10),
                })
              }
              slotProps={{ htmlInput: { maxLength: 10, inputMode: "numeric" } }}
            />
            <TextField
              label="Customer type"
              select
              value={String(form.creditEnabled)}
              onChange={(event) =>
                setForm({
                  ...form,
                  creditEnabled: event.target.value === "true",
                })
              }
            >
              <MenuItem value="false">Non-credit customer</MenuItem>
              <MenuItem value="true">Credit customer</MenuItem>
            </TextField>
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setCustomerDialogOpen(false)}>Cancel</Button>
          <Button type="submit" variant="contained" disabled={saving}>
            {saving ? "Saving..." : "Save customer"}
          </Button>
        </DialogActions>
      </Dialog>
      <Dialog
        open={purchaseOpen}
        onClose={() => !purchaseSaving && setPurchaseOpen(false)}
        maxWidth="sm"
        fullWidth
        slotProps={{
          paper: { component: "form", onSubmit: saveDailyPurchase },
        }}
      >
        <DialogTitle>Add daily purchase</DialogTitle>
        <DialogContent>
          <Stack spacing={2} sx={{ pt: 1 }}>
            <TextField
              label="Customer"
              required
              select
              value={purchaseCustomer?.id ?? ""}
              onChange={(event) => {
                const customer = customers.find(
                  (entry) => Number(entry.id) === Number(event.target.value),
                );
                const creditEnabled =
                  customer?.creditEnabled ??
                  Number(customer?.creditLimit ?? 0) > 0;
                setPurchaseCustomer(
                  customer ? { ...customer, creditEnabled } : null,
                );
                setPurchaseForm({
                  ...purchaseForm,
                  paymentMethod: creditEnabled
                    ? purchaseForm.paymentMethod
                    : "Cash",
                });
              }}
            >
              {customers.map((customer) => (
                <MenuItem key={customer.id} value={customer.id}>
                  {customer.customerName}
                </MenuItem>
              ))}
            </TextField>
            <TextField
              label="Product"
              required
              select
              value={purchaseForm.productId}
              onChange={(event) => selectPurchaseProduct(event.target.value)}
            >
              {products
                .filter(
                  (product) =>
                    product.active !== false &&
                    Number(product.quantity ?? 0) > 0,
                )
                .map((product) => (
                  <MenuItem key={product.id} value={product.id}>
                    {product.productName} ({product.quantity}{" "}
                    {product.unit || "units"})
                  </MenuItem>
                ))}
            </TextField>
            <TextField
              label="Quantity"
              required
              type="number"
              value={purchaseForm.quantity}
              onChange={(event) =>
                setPurchaseForm({
                  ...purchaseForm,
                  quantity: event.target.value,
                })
              }
              slotProps={{ htmlInput: { min: 1 } }}
            />
            <TextField
              label="Payment method"
              required
              select
              value={purchaseForm.paymentMethod}
              onChange={(event) =>
                setPurchaseForm({
                  ...purchaseForm,
                  paymentMethod: event.target.value,
                })
              }
            >
              <MenuItem value="Cash">Cash</MenuItem>
              <MenuItem value="UPI">UPI</MenuItem>
              {purchaseCustomer?.creditEnabled && (
                <MenuItem value="Credit">Credit</MenuItem>
              )}
            </TextField>
            {purchaseCustomer?.creditEnabled && (
              <Typography color="text.secondary">
                Credit limit: {currency(purchaseCustomer.creditLimit)}. Current
                due: {currency(purchaseCustomer.credit?.balance)}
              </Typography>
            )}
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setPurchaseOpen(false)}>Cancel</Button>
          <Button type="submit" variant="contained" disabled={purchaseSaving}>
            {purchaseSaving ? "Saving..." : "Save purchase"}
          </Button>
        </DialogActions>
      </Dialog>
      <Dialog
        open={paymentOpen}
        onClose={() => !paymentSaving && setPaymentOpen(false)}
        maxWidth="sm"
        fullWidth
        slotProps={{ paper: { component: "form", onSubmit: savePayment } }}
      >
        <DialogTitle>Record credit payment</DialogTitle>
        <DialogContent>
          <Stack spacing={2} sx={{ pt: 1 }}>
            <TextField
              label="Credit customer"
              required
              select
              value={paymentCustomer?.id ?? ""}
              onChange={(event) =>
                setPaymentCustomer(
                  customers.find(
                    (customer) =>
                      Number(customer.id) === Number(event.target.value),
                  ),
                )
              }
            >
              {customers
                .filter(
                  (customer) =>
                    customer.creditEnabled ||
                    Number(
                      credits.find(
                        (credit) =>
                          Number(credit.customerId) === Number(customer.id),
                      )?.balance ?? 0,
                    ) > 0,
                )
                .map((customer) => (
                  <MenuItem key={customer.id} value={customer.id}>
                    {customer.customerName}
                  </MenuItem>
                ))}
            </TextField>
            <TextField
              label="Payment amount"
              required
              type="number"
              value={paymentAmount}
              onChange={(event) => setPaymentAmount(event.target.value)}
              slotProps={{ htmlInput: { min: 0.01, step: "0.01" } }}
            />
            <Typography color="text.secondary">
              Current balance:{" "}
              {currency(
                credits.find(
                  (credit) =>
                    Number(credit.customerId) === Number(paymentCustomer?.id),
                )?.balance,
              )}
            </Typography>
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setPaymentOpen(false)}>Cancel</Button>
          <Button type="submit" variant="contained" disabled={paymentSaving}>
            {paymentSaving ? "Saving..." : "Record payment"}
          </Button>
        </DialogActions>
      </Dialog>
    </Stack>
  );
}
