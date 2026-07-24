export const reportDefinitions = {
  sales: {
    label: "Sales report",
    columns: [
      ["saleCode", "Sale code"],
      ["customerName", "Customer"],
      ["saleDate", "Date"],
      ["totalAmount", "Total", "currency"],
    ],
  },
  purchases: {
    label: "Purchases report",
    columns: [
      ["purchaseCode", "Purchase code"],
      ["supplierName", "Supplier"],
      ["purchaseDate", "Date"],
      ["totalAmount", "Total", "currency"],
    ],
  },
  products: {
    label: "Products report",
    columns: [
      ["productCode", "Code"],
      ["productName", "Product"],
      ["categoryName", "Category"],
      ["brand", "Brand"],
      ["quantity", "Stock"],
      ["sellingPrice", "Selling price", "currency"],
    ],
  },
  stock: {
    label: "Stock report",
    columns: [
      ["productCode", "Code"],
      ["productName", "Product"],
      ["quantity", "Quantity"],
      ["minimumStock", "Minimum stock"],
      ["stockStatus", "Status"],
    ],
  },
  customers: {
    label: "Customers report",
    columns: [
      ["customerName", "Customer"],
      ["phone", "Phone"],
      ["email", "Email"],
    ],
  },
  suppliers: {
    label: "Suppliers report",
    columns: [
      ["supplierName", "Supplier"],
      ["phone", "Phone"],
      ["email", "Email"],
    ],
  },
};

export const reportOptions = Object.entries(reportDefinitions).map(([value, report]) => ({
  value,
  label: report.label,
}));

export function getReportDefinition(type) {
  return reportDefinitions[type] ?? reportDefinitions.sales;
}