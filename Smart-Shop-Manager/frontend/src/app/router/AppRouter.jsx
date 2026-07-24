import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import DashboardLayout from "../../layouts/DashboardLayout/DashboardLayout";
import Login from "../../pages/Login";
import DashboardPage from "../../pages/dashboard/DashboardPage";
import ProductPage from "../../pages/ProductPage";
import CustomerPage from "../../pages/CustomerPage";
import SupplierPage from "../../pages/SupplierPage";
import PurchasePage from "../../pages/PurchasePage";
import SalePage from "../../pages/SalePage";
import ReportsPage from "../../pages/ReportsPage";
import SettingsPage from "../../pages/SettingsPage";
import ProtectedRoute from "../../routes/ProtectedRoute";
import { paths } from "../../routes/paths";

export default function AppRouter() {
  return <BrowserRouter><Routes>
    <Route path={paths.login} element={<Login />} />
    <Route element={<ProtectedRoute />}>
      <Route element={<DashboardLayout />}>
        <Route path={paths.dashboard} element={<DashboardPage />} />
        <Route path={paths.products} element={<ProductPage />} />
        <Route path={paths.customers} element={<CustomerPage />} />
        <Route path={paths.suppliers} element={<SupplierPage />} />
        <Route path={paths.purchases} element={<PurchasePage />} />
        <Route path={paths.sales} element={<SalePage />} />
        <Route path={paths.reports} element={<ReportsPage />} />
        <Route path={paths.settings} element={<SettingsPage />} />
      </Route>
    </Route>
    <Route path="*" element={<Navigate to={paths.login} replace />} />
  </Routes></BrowserRouter>;
}
