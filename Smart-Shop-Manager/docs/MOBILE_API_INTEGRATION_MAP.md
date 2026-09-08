# Smart Shop Mobile API Integration Map

Phase 0 inspection date: 2026-09-08

This document records the verified contract between the future Flutter app and the existing Spring Boot application. The Flutter app must use these APIs and must not connect directly to MySQL or duplicate transactional business rules.

## Architecture

```text
Flutter Android app -> Spring Boot REST API -> MySQL database
React web app      -> Spring Boot REST API -> MySQL database
```

- Backend API prefix: `/api`
- Current local backend URL: `http://localhost:8081/api`
- Frontend configuration: `VITE_API_BASE_URL`, defaulting to `http://localhost:8081/api`
- Physical Android devices cannot use `localhost` for the development computer. Use the computer's reachable LAN IP during development and HTTPS in production.

## Authentication

| Method | Endpoint | Request | Response | Auth |
|---|---|---|---|---|
| POST | `/auth/login` | `{ "username": string, "password": string }` | `{ "token": string }` | Public |

JWT details verified in `security/JwtService.java`:

- Send `Authorization: Bearer <token>` on every protected request.
- Tokens expire after 24 hours.
- The backend is stateless and returns authentication failures as HTTP 401.
- `/api/auth/**` and CORS preflight requests are public; all other API routes require authentication.
- The current JWT signing secret is hardcoded in backend source and must be externalized before production deployment. Flutter must never contain it.

## Endpoint Inventory

### Dashboard

| Method | Endpoint | Response |
|---|---|---|
| GET | `/dashboard` | `totalProducts`, `totalCategories`, `totalCustomers`, `totalSuppliers`, `todaySales`, `todayPurchases`, `lowStockProducts` |

The dashboard currently returns counts and today's totals only. It does not return recent sales or low-stock product rows.

### Categories

| Method | Endpoint | Request/query | Response |
|---|---|---|---|
| POST | `/categories` | `CategoryDTO` | `CategoryDTO` |
| GET | `/categories` | none | `CategoryDTO[]` |
| GET | `/categories/{id}` | path `id` | optional `CategoryDTO` |
| PUT | `/categories/{id}` | `CategoryDTO` | `CategoryDTO` |
| DELETE | `/categories/{id}` | path `id` | text confirmation |
| GET | `/categories/search?keyword=` | keyword | `CategoryDTO[]` |
| GET | `/categories/page?page=&size=` | page, size | Spring `Page<CategoryDTO>` |
| GET | `/categories/sort?field=` | field | `CategoryDTO[]` |
| GET | `/categories/filter?keyword=&page=&size=&sortField=` | filters | Spring `Page<CategoryDTO>` |
| GET | `/categories/statistics` | none | total/active/inactive counts |

`CategoryDTO`: `id`, `name`, `description`, `active`. Name, description, and active are required on create/update.

### Products and inventory

| Method | Endpoint | Request/query | Response |
|---|---|---|---|
| POST | `/products` | `ProductDTO` | `ProductDTO` |
| GET | `/products` | none | `ProductDTO[]` |
| GET | `/products/{id}` | path `id` | optional `ProductDTO` |
| PUT | `/products/{id}` | `ProductDTO` | `ProductDTO` |
| DELETE | `/products/{id}` | path `id` | text confirmation; service deactivates product |
| POST | `/products/restock` | `{ barcode, quantity }` | `ProductDTO` |
| GET | `/products/search?keyword=` | keyword, including barcode | `ProductDTO[]` |
| GET | `/products/browse` | keyword, categoryId, subcategory, brand, stockStatus, page, size, sort, direction | Spring `Page<ProductDTO>` |
| GET | `/products/page?page=&size=` | page, size | Spring `Page<ProductDTO>` |
| GET | `/products/filter?keyword=&page=&size=&sortField=` | filters | Spring `Page<ProductDTO>` |
| GET | `/products/low-stock` | none | `ProductDTO[]` |
| GET | `/products/expiring?date=` | ISO date | `ProductDTO[]` |
| GET | `/products/sort?field=&direction=` | sort options | `ProductDTO[]` |
| GET | `/products/statistics` | none | product statistics DTO |
| GET | `/products/charts/stock` | none | chart data DTOs |
| GET | `/products/charts/inventory-value` | none | chart data DTOs |

`ProductDTO`: `id`, `productCode`, `barcode`, `productName`, `brand`, `subcategory`, `categoryId`, `variant`, `unit`, `quantity`, `purchasePrice`, `sellingPrice`, `minimumStock`, `expiryDate`, `expiryApplicable`, `active`, `createdAt`, `updatedAt`.

Barcode is validated as unique by the backend. There is no dedicated barcode-scan API: the mobile camera should decode a barcode, then call product search or restock with the decoded value.

### Customers

| Method | Endpoint | Request/query | Response |
|---|---|---|---|
| POST | `/customers` | `CustomerDTO` | `CustomerDTO`, 201 |
| GET | `/customers` | none | `CustomerDTO[]` |
| GET | `/customers/{id}` | path `id` | `CustomerDTO` |
| PUT | `/customers/{id}` | `CustomerDTO` | `CustomerDTO` |
| DELETE | `/customers/{id}` | path `id` | text confirmation |
| GET | `/customers/search?keyword=` | keyword | `CustomerDTO[]` |
| GET | `/customers/page?page=&size=` | page, size | Spring `Page<CustomerDTO>` |
| GET | `/customers/sort?field=` | field | `CustomerDTO[]` |
| GET | `/customers/filter?keyword=&page=&size=&sortField=` | filters | Spring `Page<CustomerDTO>` |
| GET | `/customers/statistics` | none | customer statistics DTO |

`CustomerDTO`: `id`, `customerCode`, `customerName`, `phone`, `email`, `address`, `creditLimit`, `creditEnabled`, `active`. Name and exactly 10 digits for phone are required; credit limit is required; email is validated when supplied.

### Credits and payments

| Method | Endpoint | Request | Response |
|---|---|---|---|
| POST | `/credits/purchase` | `{ customerId, amount, remarks }` | `CreditDTO` |
| POST | `/credits/payment` | `{ customerId, amount, remarks }` | `CreditDTO` |
| GET | `/credits` | none | `CreditDTO[]` |
| GET | `/credits/customer/{customerId}` | path customerId | `CreditDTO` |
| GET | `/credits/customer/{customerId}/history` | path customerId | `CreditTransactionDTO[]` |

`CreditDTO`: `id`, `customerId`, `customerName`, `totalCredit`, `totalPaid`, `balance`, `status`.

`CreditTransactionDTO`: `id`, `creditId`, `transactionDate`, `type`, `amount`, `remarks`.

Credit sales are handled transactionally by `/sales`; Flutter must not update stock or due balances itself.

### Suppliers

| Method | Endpoint | Request/query | Response |
|---|---|---|---|
| POST | `/suppliers` | `SupplierDTO` | `SupplierDTO` |
| GET | `/suppliers` | none | `SupplierDTO[]` |
| GET | `/suppliers/{id}` | path `id` | optional `SupplierDTO` |
| PUT | `/suppliers/{id}` | `SupplierDTO` | `SupplierDTO` |
| DELETE | `/suppliers/{id}` | path `id` | text confirmation |
| GET | `/suppliers/search?keyword=` | keyword | `SupplierDTO[]` |
| GET | `/suppliers/page?page=&size=` | page, size | Spring `Page<SupplierDTO>` |
| GET | `/suppliers/sort?field=` | field | `SupplierDTO[]` |
| GET | `/suppliers/filter?keyword=&page=&size=&sortField=` | filters | Spring `Page<SupplierDTO>` |
| GET | `/suppliers/statistics` | none | supplier statistics DTO |

`SupplierDTO`: `id`, `supplierCode`, `supplierName`, `contactPerson`, `phone`, `email`, `address`, `gstNumber`, `active`. Supplier code/name/phone are required; email is validated when supplied.

### Purchases

| Method | Endpoint | Request | Response |
|---|---|---|---|
| POST | `/purchases` | `PurchaseDTO` | `PurchaseDTO`, 201 |
| GET | `/purchases` | none | `PurchaseDTO[]` |

`PurchaseDTO`: `id`, `purchaseCode`, `purchaseDate`, `supplierId`, `totalAmount`, `purchaseItems[]`.

`PurchaseItemDTO`: `id`, `productId`, `quantity`, `purchasePrice`, `totalPrice`.

The backend increases product stock and calculates the purchase total. There is currently no purchase detail, update, delete, pagination, or date-filter endpoint.

### Sales and receipts

| Method | Endpoint | Request | Response |
|---|---|---|---|
| POST | `/sales` | `SaleDTO` | `SaleDTO`, 201 |
| GET | `/sales` | none | `SaleDTO[]` |
| GET | `/sales/{id}` | path `id` | `SaleDTO` |
| PUT | `/sales/{id}` | `SaleDTO` | currently unsupported; returns an operation error |
| DELETE | `/sales/{id}` | path `id` | 204; restores stock and reverses credit sale effects |
| GET | `/sales/{id}/invoice` | path `id` | PDF invoice stream |

`SaleDTO`: `id`, `saleCode`, `saleDate`, `customerId`, `paymentMethod`, `totalAmount`, `saleItems[]`.

`SaleItemDTO`: `id`, `productId`, `quantity`, `sellingPrice`, `totalPrice`.

The backend calculates totals, decreases stock, and processes credit sales. Sale date is required; sale item product, quantity, and selling price are required. There is no backend pagination, search, date filter, or payment filter for sales.

### Reports

| Method | Endpoint | Response |
|---|---|---|
| GET | `/reports/sales` | sales report rows: saleCode, customerName, totalAmount, saleDate |
| GET | `/reports/purchases` | purchase report rows: purchaseCode, supplierName, totalAmount, purchaseDate |
| GET | `/reports/products` | product report rows |
| GET | `/reports/stock` | stock report rows |
| GET | `/reports/customers` | customer report rows |
| GET | `/reports/suppliers` | supplier report rows |
| GET | `/reports/profit` | `totalSales`, `totalPurchases`, `totalProfit` |

Reports have no date-range query parameters. The mobile app must not pretend that Today/Week/Month filters are server-backed until a backend API is added.

## Error and Validation Contract

- Controller request validation uses Jakarta Bean Validation on most create/update DTOs.
- Invalid requests should be rendered as actionable validation errors in Flutter.
- Protected requests can return HTTP 401; clear the token and return to login.
- Other HTTP failures and network timeouts need explicit retry/error states.
- Some service failures currently use generic `RuntimeException`; response shape is not standardized and should be parsed defensively.
- No global exception response DTO was found during inspection.

## Verified Gaps Before Flutter Feature Work

1. No dedicated barcode scan endpoint is needed initially; camera decoding plus product search/restock is sufficient.
2. No mobile-specific API or pagination for sales/purchases/reports exists.
3. Purchases cannot currently be viewed by ID, edited, or deleted.
4. Reports do not support date filtering.
5. Dashboard does not provide recent sale rows or low-stock product details.
6. There is no backend settings/shop-information endpoint in the inspected controllers.
7. There is no password-change endpoint.
8. Receipt PDF download exists for sales, but sharing/printing is a mobile responsibility.
9. JWT signing configuration and database credentials must be externalized for production; neither belongs in Flutter.

## Phase 0 Decision

The existing APIs are sufficient to begin Flutter foundation, authentication, navigation, dashboard, products, customers, credits, suppliers, purchases, sales, receipts, and basic reports. Do not modify the backend for the initial mobile implementation. Revisit the listed gaps only when a required mobile workflow cannot be completed with the verified contracts.