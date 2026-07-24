import AccountCircleOutlinedIcon from "@mui/icons-material/AccountCircleOutlined";
import LogoutOutlinedIcon from "@mui/icons-material/LogoutOutlined";
import MenuIcon from "@mui/icons-material/Menu";
import NotificationsNoneOutlinedIcon from "@mui/icons-material/NotificationsNoneOutlined";
import StorefrontIcon from "@mui/icons-material/Storefront";
import { AppBar, Badge, Divider, IconButton, ListItemIcon, Menu, MenuItem, Toolbar, Tooltip, Typography } from "@mui/material";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/useAuth";
import { paths } from "../../routes/paths";

export default function AppNavbar({ onMenuClick }) {
  const { logout } = useAuth(); const navigate = useNavigate();
  const [userMenuAnchor, setUserMenuAnchor] = useState(null);
  const [notificationAnchor, setNotificationAnchor] = useState(null);
  function handleLogout() { logout(); navigate(paths.login, { replace: true }); }
  return <AppBar position="fixed" sx={{ zIndex: (theme) => theme.zIndex.drawer + 1, borderBottom: 0 }}>
    <Toolbar sx={{ minHeight: 72, gap: 1.5 }}><IconButton color="inherit" onClick={onMenuClick} sx={{ display: { md: "none" }, mr: 0.5 }} aria-label="Open navigation"><MenuIcon /></IconButton><StorefrontIcon sx={{ mr: 0.5 }} /><Typography variant="h6" sx={{ flexGrow: 1, display: { xs: "none", sm: "block" }, fontWeight: 700, letterSpacing: "0.02em" }}>Smart Shop Manager</Typography>
      <Tooltip title="Notifications"><IconButton color="inherit" aria-label="Notifications" onClick={(event) => setNotificationAnchor(event.currentTarget)}><Badge variant="dot" color="error"><NotificationsNoneOutlinedIcon /></Badge></IconButton></Tooltip>
      <Tooltip title="Account menu"><IconButton color="inherit" aria-label="Account menu" onClick={(event) => setUserMenuAnchor(event.currentTarget)}><AccountCircleOutlinedIcon /></IconButton></Tooltip>
      <Menu anchorEl={notificationAnchor} open={Boolean(notificationAnchor)} onClose={() => setNotificationAnchor(null)}><MenuItem disabled>No new notifications</MenuItem></Menu>
      <Menu anchorEl={userMenuAnchor} open={Boolean(userMenuAnchor)} onClose={() => setUserMenuAnchor(null)}><MenuItem disabled>Signed in as administrator</MenuItem><Divider /><MenuItem onClick={handleLogout}><ListItemIcon><LogoutOutlinedIcon fontSize="small" /></ListItemIcon>Logout</MenuItem></Menu>
    </Toolbar>
  </AppBar>;
}
