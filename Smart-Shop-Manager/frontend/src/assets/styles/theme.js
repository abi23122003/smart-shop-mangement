import { createTheme } from "@mui/material/styles";

export default createTheme({
  palette: {
    primary: { main: "#1d4ed8" },
    secondary: { main: "#0f766e" },
    background: { default: "#eef3fb", paper: "#ffffff" },
    text: { primary: "#0f172a", secondary: "#475569" },
  },
  shape: { borderRadius: 14 },
  typography: {
    fontFamily: "Inter, Segoe UI, Roboto, Arial, sans-serif",
    h4: { fontWeight: 800, letterSpacing: "-0.02em" },
    h5: { fontWeight: 700, letterSpacing: "-0.01em" },
    h6: { fontWeight: 700 },
    button: { textTransform: "none", fontWeight: 600 },
  },
  components: {
    MuiCssBaseline: {
      styleOverrides: {
        body: {
          backgroundImage:
            "radial-gradient(circle at top left, rgba(29, 78, 216, 0.12), transparent 32%), radial-gradient(circle at bottom right, rgba(15, 118, 110, 0.08), transparent 24%)",
          backgroundAttachment: "fixed",
        },
      },
    },
    MuiAppBar: {
      styleOverrides: {
        root: {
          backgroundImage: "linear-gradient(135deg, rgba(15, 23, 42, 0.96), rgba(29, 78, 216, 0.88))",
          backdropFilter: "blur(14px)",
          boxShadow: "0 12px 30px rgba(15, 23, 42, 0.18)",
          borderBottom: "1px solid rgba(255, 255, 255, 0.08)",
        },
      },
    },
    MuiPaper: {
      defaultProps: { elevation: 0 },
      styleOverrides: {
        root: {
          border: "1px solid rgba(15, 23, 42, 0.08)",
          boxShadow: "0 18px 45px rgba(15, 23, 42, 0.08)",
        },
      },
    },
    MuiButton: {
      styleOverrides: {
        root: { borderRadius: 12 },
      },
    },
  },
});
