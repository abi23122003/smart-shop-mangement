# Mobile Performance Review

Phase 23 review date: 2026-09-08

## Improvements applied

- Product browse requests are bounded to 20 records.
- Customer and supplier initial loads use the existing paginated backend endpoints with a 20-record page.
- Product and customer search remains debounced at 350 ms in the UI.
- Screens use lazy `ListView` rendering rather than building large desktop tables.
- Dashboard and reports load only when their route is opened.
- The API client uses bounded connect, send, and receive timeouts.

## Current backend limits

- Sales, purchases, and reports do not expose pagination.
- Product/customer search endpoints return lists rather than paged responses.
- The current mobile screens should not claim full-dataset support until those backend contracts are extended.

## Follow-up thresholds

For a shop with thousands of products or customers, add server-side pagination or cursor-based search for sales, purchases, reports, and search results before increasing mobile page sizes. Do not solve this by loading the entire database into Flutter memory.
