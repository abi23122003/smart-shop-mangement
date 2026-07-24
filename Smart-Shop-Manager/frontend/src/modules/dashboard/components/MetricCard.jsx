import { Card, CardContent, Stack, Typography } from "@mui/material";

export default function MetricCard({ label, value, icon, color = "primary.main" }) {
  return <Card><CardContent><Stack direction="row" sx={{ alignItems: "center", justifyContent: "space-between" }}><div><Typography color="text.secondary" variant="body2">{label}</Typography><Typography variant="h4" sx={{ mt: .5 }}>{value}</Typography></div><Stack sx={{ alignItems: "center", justifyContent: "center", width: 48, height: 48, borderRadius: 2, bgcolor: color, color: "common.white" }}>{icon}</Stack></Stack></CardContent></Card>;
}
