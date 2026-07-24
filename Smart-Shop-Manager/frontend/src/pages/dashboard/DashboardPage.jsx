import CategoryOutlinedIcon from "@mui/icons-material/CategoryOutlined";
import GroupsOutlinedIcon from "@mui/icons-material/GroupsOutlined";
import Inventory2OutlinedIcon from "@mui/icons-material/Inventory2Outlined";
import LocalShippingOutlinedIcon from "@mui/icons-material/LocalShippingOutlined";
import TrendingDownOutlinedIcon from "@mui/icons-material/TrendingDownOutlined";
import TrendingUpOutlinedIcon from "@mui/icons-material/TrendingUpOutlined";
import WarningAmberOutlinedIcon from "@mui/icons-material/WarningAmberOutlined";
import { Alert, Box, Button, CircularProgress, Grid, Paper, Stack, Typography } from "@mui/material";
import MetricCard from "../../modules/dashboard/components/MetricCard";
import { useDashboard } from "../../modules/dashboard/hooks/useDashboard";

const currency = new Intl.NumberFormat("en-IN", { style: "currency", currency: "INR", maximumFractionDigits: 0 });
export default function DashboardPage() {
  const { data, error, loading, refresh } = useDashboard();
  if (loading) return <Box sx={{ display: "grid", placeItems: "center", minHeight: 300 }}><CircularProgress /></Box>;
  if (error) return <Alert severity="error" action={<Button color="inherit" size="small" onClick={refresh}>Retry</Button>}>{error}</Alert>;
  const metrics = [
    ["Total products", data.totalProducts, <Inventory2OutlinedIcon />, "#1565c0"], ["Categories", data.totalCategories, <CategoryOutlinedIcon />, "#7b1fa2"],
    ["Customers", data.totalCustomers, <GroupsOutlinedIcon />, "#00897b"], ["Suppliers", data.totalSuppliers, <LocalShippingOutlinedIcon />, "#ef6c00"],
    ["Today's sales", currency.format(data.todaySales), <TrendingUpOutlinedIcon />, "#2e7d32"], ["Today's purchases", currency.format(data.todayPurchases), <TrendingDownOutlinedIcon />, "#d32f2f"],
  ];
  return <Stack spacing={3}><Box><Typography variant="h4">Dashboard</Typography><Typography color="text.secondary">A snapshot of your shop today.</Typography></Box>
    <Grid container spacing={2}>{metrics.map(([label, value, icon, color]) => <Grid key={label} size={{ xs: 12, sm: 6, lg: 4 }}><MetricCard label={label} value={value} icon={icon} color={color} /></Grid>)}</Grid>
    <Paper sx={{ p: 3 }}><Stack direction="row" spacing={2} sx={{ alignItems: "center" }}><WarningAmberOutlinedIcon color={data.lowStockProducts ? "warning" : "success"} /><Box><Typography variant="h6">Low-stock alert</Typography><Typography color="text.secondary">{data.lowStockProducts} product{data.lowStockProducts === 1 ? "" : "s"} need stock attention.</Typography></Box></Stack></Paper>
  </Stack>;
}
