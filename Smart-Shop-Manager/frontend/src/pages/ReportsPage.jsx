import { Alert, Box, Button, MenuItem, Stack, TextField, Typography } from "@mui/material";
import RefreshOutlinedIcon from "@mui/icons-material/RefreshOutlined";
import ReportSummaryCards from "../modules/reports/components/ReportSummaryCards";
import ReportTable from "../modules/reports/components/ReportTable";
import { useReports } from "../modules/reports/hooks/useReports";
import { useSettings } from "../modules/settings/hooks/useSettings";

export default function ReportsPage() {
  const { settings } = useSettings();
  const { type, setType, reportRows, profit, loading, error, lastUpdated, refresh, config, reportOptions } = useReports(settings.defaultReportType);

  return <Stack spacing={3}><Box><Stack direction={{ xs: "column", sm: "row" }} spacing={2} sx={{ alignItems: { xs: "flex-start", sm: "center" }, justifyContent: "space-between" }}><Box sx={{ minWidth: 0 }}><Typography variant="h4">Reports</Typography><Typography color="text.secondary">View live business summaries from your shop data.</Typography></Box><Button startIcon={<RefreshOutlinedIcon />} variant="outlined" onClick={refresh} disabled={loading} sx={{ alignSelf: { xs: "stretch", sm: "auto" } }}>Refresh</Button></Stack>{lastUpdated && <Typography color="text.secondary" variant="body2" sx={{ mt: 1 }}>Last updated {lastUpdated.toLocaleString()}</Typography>}</Box><ReportSummaryCards loading={loading} profit={profit} /><TextField label="Report type" select value={type} onChange={(event) => setType(event.target.value)} sx={{ maxWidth: { xs: "100%", sm: 320 } }}>{reportOptions.map((option) => <MenuItem key={option.value} value={option.value}>{option.label}</MenuItem>)}</TextField>{error && <Alert severity="error" action={<Button color="inherit" size="small" onClick={refresh}>Retry</Button>}>{error}</Alert>}<ReportTable columns={config.columns} rows={reportRows} loading={loading} compact={settings.compactTables} /></Stack>;
}
