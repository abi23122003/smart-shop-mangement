import AssessmentOutlinedIcon from "@mui/icons-material/AssessmentOutlined";
import DashboardOutlinedIcon from "@mui/icons-material/DashboardOutlined";
import GroupsOutlinedIcon from "@mui/icons-material/GroupsOutlined";
import Inventory2OutlinedIcon from "@mui/icons-material/Inventory2Outlined";
import LocalShippingOutlinedIcon from "@mui/icons-material/LocalShippingOutlined";
import PointOfSaleOutlinedIcon from "@mui/icons-material/PointOfSaleOutlined";
import ReceiptLongOutlinedIcon from "@mui/icons-material/ReceiptLongOutlined";
import SettingsOutlinedIcon from "@mui/icons-material/SettingsOutlined";
import { Box, Drawer, List, ListItemButton, ListItemIcon, ListItemText, Toolbar } from "@mui/material";
import { NavLink } from "react-router-dom";
import { paths } from "../../routes/paths";

export const drawerWidth = 248;
const items = [
  ["Dashboard", paths.dashboard, <DashboardOutlinedIcon />], ["Products", paths.products, <Inventory2OutlinedIcon />],
  ["Customers", paths.customers, <GroupsOutlinedIcon />], ["Suppliers", paths.suppliers, <LocalShippingOutlinedIcon />],
  ["Purchases", paths.purchases, <ReceiptLongOutlinedIcon />], ["Sales", paths.sales, <PointOfSaleOutlinedIcon />],
  ["Reports", paths.reports, <AssessmentOutlinedIcon />], ["Settings", paths.settings, <SettingsOutlinedIcon />],
];
export default function AppSidebar({ mobileOpen, onMobileClose }) {
  const navigation = <Box sx={{ overflow: "auto", pb: 2 }}><Toolbar /><List sx={{ p: 1 }}>{items.map(([label, path, icon]) => <ListItemButton key={path} component={NavLink} to={path} onClick={onMobileClose} sx={{ borderRadius: 2, mb: .5, px: 1.5, py: 1.25, transition: "background-color 150ms ease, transform 150ms ease", "&.active": { bgcolor: "primary.50", color: "primary.main", "& .MuiListItemIcon-root": { color: "primary.main" } }, "&:hover": { transform: "translateX(2px)" } }}><ListItemIcon sx={{ minWidth: 40 }}>{icon}</ListItemIcon><ListItemText primary={label} slotProps={{ primary: { sx: { fontWeight: 600 } } }} /></ListItemButton>)}</List></Box>;
  const drawerStyle = { "& .MuiDrawer-paper": { width: drawerWidth, boxSizing: "border-box", borderRight: "1px solid", borderColor: "divider" } };
  return <><Drawer variant="temporary" open={mobileOpen} onClose={onMobileClose} ModalProps={{ keepMounted: true }} sx={{ display: { xs: "block", md: "none" }, ...drawerStyle }}>{navigation}</Drawer><Drawer variant="permanent" sx={{ width: drawerWidth, flexShrink: 0, display: { xs: "none", md: "block" }, ...drawerStyle }}>{navigation}</Drawer></>;
}
