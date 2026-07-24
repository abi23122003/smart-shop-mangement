import { useState } from "react";
import { Box, Toolbar } from "@mui/material";
import { Outlet } from "react-router-dom";
import AppNavbar from "../../components/layout/AppNavbar";
import AppSidebar, { drawerWidth } from "../../components/layout/AppSidebar";

export default function DashboardLayout() {
  const [mobileOpen, setMobileOpen] = useState(false);
  return <Box sx={{ display: "flex", minHeight: "100vh" }}>
    <AppNavbar onMenuClick={() => setMobileOpen(true)} />
    <AppSidebar mobileOpen={mobileOpen} onMobileClose={() => setMobileOpen(false)} />
    <Box component="main" sx={{ flexGrow: 1, width: { sm: `calc(100% - ${drawerWidth}px)` }, p: { xs: 1.5, sm: 2.5, md: 3 }, maxWidth: "100vw", overflowX: "clip" }}>
      <Toolbar />
      <Outlet />
    </Box>
  </Box>;
}
