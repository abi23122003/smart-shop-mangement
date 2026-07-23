# Frontend Architecture

This structure is designed for a production React 19 + Vite frontend that talks to an existing Spring Boot REST API secured with JWT.

## Goals

- Keep features isolated and easy to scale.
- Make shared UI reusable across modules.
- Keep auth, routing, and API concerns centralized.
- Avoid business logic leakage into page components.
- Leave room for a Flutter client to reuse the same backend contracts later.

## Recommended Folder Structure

```text
src/
  app/
    providers/
    router/
  assets/
    images/
    icons/
    styles/
  components/
    charts/
    common/
    feedback/
    forms/
    navigation/
  context/
    auth/
  hooks/
    auth/
    data/
    ui/
  layouts/
    AuthLayout/
    DashboardLayout/
      components/
  modules/
    login/
    dashboard/
    products/
    customers/
    suppliers/
    purchases/
    sales/
    creditBook/
    reports/
    settings/
  pages/
    auth/
    dashboard/
    products/
    customers/
    suppliers/
    purchases/
    sales/
    creditBook/
    reports/
    settings/
  routes/
  services/
    api/
    interceptors/
    storage/
    auth/
  utils/
    constants/
    formatters/
    storage/
    validation/
```

## Folder Purpose

### `app/`

Application bootstrap concerns live here. Use this area for app-level providers, router wiring, theme setup, and any global composition logic.

### `assets/`

Static assets used by the frontend such as brand images, icons, and shared style resources.

### `components/`

Reusable UI building blocks that are shared across multiple modules. Keep these generic and presentation-focused.

### `context/`

React Context providers and hooks for global application state. JWT authentication belongs here so the auth state is centralized.

### `hooks/`

Reusable hooks that are not tied to a single module. Keep cross-cutting behavior here.

### `layouts/`

Page shells and frame components. The `DashboardLayout` should own the sidebar, header, and main content region. The `AuthLayout` should isolate login-related screens.

### `modules/`

Feature slices for each business domain. Each module can later own its own components, hooks, services, and utilities without polluting global folders.

### `pages/`

Route-level screens. Pages should stay thin and compose module features instead of holding heavy business logic.

### `routes/`

Route definitions, protected route guards, and route path constants.

### `services/`

All API-related concerns. Use this layer for the Axios client, interceptors, endpoint groups, JWT token handling, and request helpers.

### `utils/`

Pure helper code such as constants, formatters, local storage helpers, and validation utilities.

## Module Coverage

The required business areas are represented as separate modules:

- `login`
- `dashboard`
- `products`
- `customers`
- `suppliers`
- `purchases`
- `sales`
- `creditBook`
- `reports`
- `settings`

## JWT Auth Strategy

Use the Context API for session state and expose auth actions from a dedicated auth provider. Keep token persistence behind a storage abstraction so the implementation can prefer secure cookie-based storage when available and avoid scattering token logic through the app.

## Routing Strategy

- Public routes: login and any unauthenticated entry points.
- Protected routes: dashboard and all business modules.
- Nested routes: keep the dashboard shell mounted while the content area changes.

## Axios Strategy

- One shared Axios client.
- Request interceptor for auth headers and request normalization.
- Response interceptor for session expiry and error normalization.
- Service modules grouped by backend domain.

## Reusability Rules

- Keep presentational components stateless when possible.
- Keep data fetching in services or hooks, not in UI primitives.
- Keep page components thin.
- Prefer module-local code unless something is genuinely shared.

## Flutter Readiness

This structure keeps API communication isolated so a future Flutter app can reuse the same backend endpoints, DTO shapes, pagination conventions, and auth rules without being coupled to React-specific UI code.