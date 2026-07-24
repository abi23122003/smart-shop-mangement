import AssessmentOutlinedIcon from "@mui/icons-material/AssessmentOutlined";
import PaidOutlinedIcon from "@mui/icons-material/PaidOutlined";
import RequestQuoteOutlinedIcon from "@mui/icons-material/RequestQuoteOutlined";
import { Card, CardContent, Grid, Stack, Typography } from "@mui/material";

const money = (value) => `₹${Number(value ?? 0).toLocaleString("en-IN", { minimumFractionDigits: 2 })}`;

const metrics = [
  ["Total sales", "totalSales", PaidOutlinedIcon, "#1565c0"],
  ["Total purchases", "totalPurchases", RequestQuoteOutlinedIcon, "#ef6c00"],
  ["Total profit", "totalProfit", AssessmentOutlinedIcon, "#2e7d32"],
];

export default function ReportSummaryCards({ loading, profit }) {
  return <Grid container spacing={2}>{metrics.map(([label, key, Icon, color]) => <Grid key={label} size={{ xs: 12, sm: 4 }}><Card sx={{ height: "100%" }}><CardContent><Stack direction="row" spacing={2} sx={{ alignItems: "center" }}><Icon sx={{ color, fontSize: 32 }} /><div><Typography color="text.secondary" variant="body2">{label}</Typography><Typography variant="h5">{loading ? "—" : money(profit?.[key])}</Typography></div></Stack></CardContent></Card></Grid>)}</Grid>;
}
