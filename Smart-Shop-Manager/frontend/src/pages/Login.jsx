import { useState } from "react";
import StorefrontIcon from "@mui/icons-material/Storefront";
import VisibilityOffOutlinedIcon from "@mui/icons-material/VisibilityOffOutlined";
import VisibilityOutlinedIcon from "@mui/icons-material/VisibilityOutlined";
import {
  Alert,
  Box,
  Button,
  CircularProgress,
  IconButton,
  InputAdornment,
  Paper,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import { Navigate, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../context/useAuth";
import { paths } from "../routes/paths";
import { getApiErrorMessage } from "../services/apiErrors";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [fieldErrors, setFieldErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  const { login, isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const destination = location.state?.from?.pathname ?? paths.dashboard;

  if (isAuthenticated) return <Navigate to={paths.dashboard} replace />;

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");

    const errors = {};
    if (!username.trim()) {
      errors.username = "Username is required";
    }
    if (!password) {
      errors.password = "Password is required";
    }

    if (Object.keys(errors).length > 0) {
      setFieldErrors(errors);
      return;
    }

    setFieldErrors({});
    setIsSubmitting(true);
    try {
      await login(username.trim(), password);
      navigate(destination, { replace: true });
    } catch (requestError) {
      setError(
        requestError.response?.status === 401
          ? "Invalid username or password. Please try again."
          : getApiErrorMessage(requestError, "Sign-in failed. Please try again.")
      );
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <Box
      sx={{
        minHeight: "100vh",
        display: "grid",
        placeItems: "center",
        p: { xs: 2, sm: 3 },
        bgcolor: "#f1f5f9",
      }}
    >
      <Paper
        component="form"
        noValidate
        onSubmit={handleSubmit}
        sx={{
          width: "100%",
          maxWidth: 960,
          borderRadius: 4,
          overflow: "hidden",
          boxShadow: "0 24px 48px -12px rgba(15, 23, 42, 0.14), 0 0 0 1px rgba(15, 23, 42, 0.06)",
          display: "grid",
          gridTemplateColumns: { xs: "1fr", md: "1.05fr 0.95fr" },
        }}
      >
        {/* Branding side (Left panel) */}
        <Box
          sx={{
            p: { xs: 4, sm: 5, md: 6 },
            color: "common.white",
            position: "relative",
            overflow: "hidden",
            display: "flex",
            flexDirection: "column",
            justifyContent: "space-between",
            minHeight: { xs: 240, md: 540 },
            backgroundImage: `
              radial-gradient(circle at 85% 15%, rgba(59, 130, 246, 0.3) 0%, transparent 45%),
              radial-gradient(circle at 15% 85%, rgba(13, 148, 136, 0.25) 0%, transparent 50%),
              linear-gradient(135deg, #091224 0%, #1e3a8a 52%, #0d9488 100%)
            `,
          }}
        >
          {/* Subtle decorative geometric overlay */}
          <Box
            sx={{
              position: "absolute",
              inset: 0,
              opacity: 0.08,
              backgroundImage: `radial-gradient(#ffffff 1px, transparent 1px)`,
              backgroundSize: "24px 24px",
              pointerEvents: "none",
            }}
          />

          <Box sx={{ position: "relative", zIndex: 1 }}>
            {/* Store Icon Badge */}
            <Box
              sx={{
                width: 52,
                height: 52,
                borderRadius: 2.5,
                bgcolor: "rgba(255, 255, 255, 0.12)",
                backdropFilter: "blur(10px)",
                border: "1px solid rgba(255, 255, 255, 0.22)",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                mb: 2.5,
                boxShadow: "0 8px 20px -4px rgba(0, 0, 0, 0.25)",
              }}
            >
              <StorefrontIcon sx={{ fontSize: 30, color: "#93c5fd" }} />
            </Box>

            <Typography
              variant="overline"
              sx={{
                letterSpacing: 3.5,
                fontWeight: 700,
                fontSize: { xs: "0.8rem", sm: "0.85rem" },
                color: "#bfdbfe",
                display: "block",
                lineHeight: 1.2,
              }}
            >
              SUTHARSHAN STORES
            </Typography>

            <Typography
              variant="body2"
              sx={{
                color: "rgba(255, 255, 255, 0.8)",
                mt: 1,
                fontSize: { xs: "0.875rem", sm: "0.95rem" },
                fontWeight: 400,
                maxWidth: 320,
              }}
            >
              Manage your store, simplified.
            </Typography>
          </Box>

          <Box
            sx={{
              position: "relative",
              zIndex: 1,
              display: { xs: "none", md: "block" },
              pt: 3,
              borderTop: "1px solid rgba(255, 255, 255, 0.15)",
            }}
          >
            <Typography
              variant="caption"
              sx={{
                color: "rgba(255, 255, 255, 0.65)",
                display: "block",
                lineHeight: 1.6,
              }}
            >
              Smart inventory, point of sale, customer billing, and credit management in one unified platform.
            </Typography>
          </Box>
        </Box>

        {/* Form side (Right panel) */}
        <Box
          sx={{
            p: { xs: 3.5, sm: 4.5, md: 5 },
            display: "flex",
            alignItems: "center",
            bgcolor: "#ffffff",
          }}
        >
          <Stack spacing={3} sx={{ width: "100%" }}>
            <Box>
              <Typography variant="h4" sx={{ fontWeight: 700, fontSize: { xs: "1.6rem", sm: "1.9rem" }, color: "#0f172a" }}>
                Welcome back
              </Typography>
              <Typography color="text.secondary" sx={{ mt: 0.5, fontSize: "0.95rem" }}>
                Sign in to continue managing your store.
              </Typography>
            </Box>

            {error && (
              <Alert severity="error" sx={{ borderRadius: 2 }}>
                {error}
              </Alert>
            )}

            <TextField
              label="Username"
              id="username"
              value={username}
              onChange={(event) => {
                setUsername(event.target.value);
                if (fieldErrors.username) {
                  setFieldErrors((prev) => ({ ...prev, username: "" }));
                }
              }}
              error={Boolean(fieldErrors.username)}
              helperText={fieldErrors.username}
              autoFocus
              fullWidth
              disabled={isSubmitting}
            />

            <TextField
              label="Password"
              id="password"
              type={showPassword ? "text" : "password"}
              value={password}
              onChange={(event) => {
                setPassword(event.target.value);
                if (fieldErrors.password) {
                  setFieldErrors((prev) => ({ ...prev, password: "" }));
                }
              }}
              error={Boolean(fieldErrors.password)}
              helperText={fieldErrors.password}
              fullWidth
              disabled={isSubmitting}
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton
                      aria-label={showPassword ? "Hide password" : "Show password"}
                      onClick={() => setShowPassword((prev) => !prev)}
                      edge="end"
                      size="small"
                      tabIndex={-1}
                    >
                      {showPassword ? (
                        <VisibilityOffOutlinedIcon fontSize="small" />
                      ) : (
                        <VisibilityOutlinedIcon fontSize="small" />
                      )}
                    </IconButton>
                  </InputAdornment>
                ),
              }}
            />

            <Button
              type="submit"
              variant="contained"
              size="large"
              disabled={isSubmitting}
              sx={{
                py: 1.4,
                fontWeight: 600,
                textTransform: "none",
                fontSize: "1rem",
                borderRadius: 2,
                boxShadow: "0 4px 12px rgba(37, 99, 235, 0.25)",
              }}
            >
              {isSubmitting ? (
                <Stack direction="row" spacing={1.5} alignItems="center" justifyContent="center">
                  <CircularProgress size={20} color="inherit" />
                  <span>Signing in…</span>
                </Stack>
              ) : (
                "Sign In"
              )}
            </Button>
          </Stack>
        </Box>
      </Paper>
    </Box>
  );
}
