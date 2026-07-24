import { useState } from "react";
import { Alert, Box, Button, Divider, Paper, Stack, TextField, Typography } from "@mui/material";
import { Navigate, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../context/useAuth";
import { paths } from "../routes/paths";
import { getApiErrorMessage } from "../services/apiErrors";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const { login, isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const destination = location.state?.from?.pathname ?? paths.dashboard;
  if (isAuthenticated) return <Navigate to={paths.dashboard} replace />;

  async function handleSubmit(event) {
    event.preventDefault(); setError(""); setIsSubmitting(true);
    try { await login(username, password); navigate(destination, { replace: true }); }
    catch (requestError) { setError(requestError.response?.status === 401 ? "Invalid username or password. Please try again." : getApiErrorMessage(requestError, "Sign-in failed. Please try again.")); }
    finally { setIsSubmitting(false); }
  }

  return <Box sx={{ minHeight: "100vh", display: "grid", placeItems: "center", p: 2 }}>
    <Paper component="form" onSubmit={handleSubmit} sx={{ width: "100%", maxWidth: 980, overflow: "hidden", display: "grid", gridTemplateColumns: { xs: "1fr", md: "1.05fr 0.95fr" } }}>
      <Box sx={{ p: { xs: 3, sm: 4, md: 5 }, color: "common.white", backgroundImage: "linear-gradient(145deg, #0f172a, #1d4ed8 60%, #0f766e)", display: "flex", flexDirection: "column", justifyContent: "space-between", minHeight: { md: 520 } }}>
        <Box>
          <Typography variant="overline" sx={{ letterSpacing: 3, opacity: 0.8 }}>SMART SHOP MANAGER</Typography>
          <Typography variant="h3" sx={{ mt: 2, fontWeight: 800, lineHeight: 1.05 }}>Run your shop from a single dashboard.</Typography>
          <Typography sx={{ mt: 2, maxWidth: 420, color: "rgba(255,255,255,0.85)" }}>Track inventory, customers, purchases, sales, and reports in one place without losing the details that matter.</Typography>
        </Box>
        <Box sx={{ mt: 4 }}>
          <Stack direction="row" spacing={3} divider={<Divider flexItem orientation="vertical" sx={{ borderColor: "rgba(255,255,255,0.18)" }} />}>
            <Box><Typography variant="h5" sx={{ fontWeight: 800 }}>6</Typography><Typography variant="body2" sx={{ color: "rgba(255,255,255,0.72)" }}>core modules</Typography></Box>
            <Box><Typography variant="h5" sx={{ fontWeight: 800 }}>1</Typography><Typography variant="body2" sx={{ color: "rgba(255,255,255,0.72)" }}>unified workflow</Typography></Box>
            <Box><Typography variant="h5" sx={{ fontWeight: 800 }}>JWT</Typography><Typography variant="body2" sx={{ color: "rgba(255,255,255,0.72)" }}>secured session</Typography></Box>
          </Stack>
        </Box>
      </Box>
      <Box sx={{ p: { xs: 3, sm: 4, md: 5 }, display: "flex", alignItems: "center" }}>
        <Stack spacing={3} sx={{ width: "100%" }}>
          <Box><Typography variant="h4">Welcome back</Typography><Typography color="text.secondary">Sign in to continue managing your store.</Typography></Box>
          {error && <Alert severity="error">{error}</Alert>}
          <TextField label="Username" value={username} onChange={(event) => setUsername(event.target.value)} required autoFocus />
          <TextField label="Password" type="password" value={password} onChange={(event) => setPassword(event.target.value)} required />
          <Button type="submit" variant="contained" size="large" disabled={isSubmitting}>{isSubmitting ? "Signing in…" : "Sign in"}</Button>
        </Stack>
      </Box>
    </Paper>
  </Box>;
}
