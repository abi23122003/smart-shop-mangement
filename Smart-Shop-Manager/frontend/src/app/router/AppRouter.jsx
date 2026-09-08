import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import DashboardLayout from "../../layouts/DashboardLayout/DashboardLayout";
import Login from "../../pages/Login";
import DashboardPage from "../../pages/dashboard/DashboardPage";
import ProductPage from "../../pages/ProductPage";
import CustomerPage from "../../pages/CustomerPage";
import SalePage from "../../pages/SalePage";
import ReportsPage from "../../pages/ReportsPage";
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
        <Route path="/suppliers" element={<Navigate to={paths.dashboard} replace />} />
        <Route path="/purchases" element={<Navigate to={paths.dashboard} replace />} />
        <Route path={paths.sales} element={<SalePage />} />
        <Route path={paths.reports} element={<ReportsPage />} />
        <Route path="/settings" element={<Navigate to={paths.dashboard} replace />} />
      </Route>
    </Route>
    <Route path="*" element={<Navigate to={paths.login} replace />} />
  </Routes></BrowserRouter>;
}
