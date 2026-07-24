const SETTINGS_KEY = "smart_shop_settings";

export const defaultSettings = {
  defaultReportType: "sales",
  compactTables: false,
};

const allowedReportTypes = new Set(["sales", "purchases", "products", "stock", "customers", "suppliers"]);

function normalizeSettings(settings) {
  const merged = { ...defaultSettings, ...(settings ?? {}) };
  if (!allowedReportTypes.has(merged.defaultReportType)) {
    merged.defaultReportType = defaultSettings.defaultReportType;
  }

  return merged;
}

export function loadSettings() {
  try {
    const stored = localStorage.getItem(SETTINGS_KEY);
    return normalizeSettings(stored ? JSON.parse(stored) : null);
  } catch {
    return defaultSettings;
  }
}

export function saveSettings(settings) {
  const normalized = normalizeSettings(settings);
  localStorage.setItem(SETTINGS_KEY, JSON.stringify(normalized));
  return normalized;
}

export function resetSettings() {
  localStorage.removeItem(SETTINGS_KEY);
  return defaultSettings;
}