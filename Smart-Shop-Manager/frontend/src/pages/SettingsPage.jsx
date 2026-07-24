import LogoutOutlinedIcon from "@mui/icons-material/LogoutOutlined";
import RestartAltOutlinedIcon from "@mui/icons-material/RestartAltOutlined";
import { Alert, Box, Button, Card, CardContent, Divider, FormControlLabel, MenuItem, Select, Stack, Switch, Typography } from "@mui/material";
import { reportOptions } from "../modules/reports/constants";
import { useAuth } from "../context/useAuth";
import { useSettings } from "../modules/settings/hooks/useSettings";

export default function SettingsPage() {
  const { logout, isAuthenticated } = useAuth();
  const { settings, updateSettings, resetToDefaults } = useSettings();

  return <Stack spacing={3}><Box><Typography variant="h4">Settings</Typography><Typography color="text.secondary">Adjust the preferences that shape your dashboard experience.</Typography></Box><Card><CardContent><Stack spacing={2}><Box><Typography variant="h6">General preferences</Typography><Typography color="text.secondary" variant="body2">These settings are stored locally in this browser.</Typography></Box><Divider /><Stack spacing={2} direction={{ xs: "column", md: "row" }} useFlexGap sx={{ flexWrap: "wrap" }}><Box sx={{ minWidth: 260, flex: 1 }}><Typography variant="subtitle2" sx={{ mb: 1 }}>Default report</Typography><Select fullWidth value={settings.defaultReportType} onChange={(event) => updateSettings({ defaultReportType: event.target.value })}>{reportOptions.map((option) => <MenuItem key={option.value} value={option.value}>{option.label}</MenuItem>)}</Select></Box><FormControlLabel sx={{ alignItems: "flex-start", pt: { xs: 1, md: 3 } }} control={<Switch checked={settings.compactTables} onChange={(event) => updateSettings({ compactTables: event.target.checked })} />} label={<Box><Typography variant="subtitle2">Compact tables</Typography><Typography color="text.secondary" variant="body2">Show denser tables in report views.</Typography></Box>} /></Stack><Stack direction={{ xs: "column", sm: "row" }} spacing={2}><Button fullWidth={false} variant="outlined" startIcon={<RestartAltOutlinedIcon />} onClick={resetToDefaults}>Reset to defaults</Button><Button fullWidth={false} variant="contained" color="error" startIcon={<LogoutOutlinedIcon />} onClick={logout} disabled={!isAuthenticated}>Sign out</Button></Stack></Stack></CardContent></Card>{!isAuthenticated && <Alert severity="info">You are currently signed out. Settings remain available for the next login session.</Alert>}</Stack>;
}
