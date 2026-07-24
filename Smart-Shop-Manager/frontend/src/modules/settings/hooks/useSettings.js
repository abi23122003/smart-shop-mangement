import { useCallback, useState } from "react";
import { defaultSettings, loadSettings, resetSettings as resetStoredSettings, saveSettings } from "../../../services/storage/settingsStorage";

export function useSettings() {
  const [settings, setSettings] = useState(() => loadSettings());

  const updateSettings = useCallback((patch) => {
    let nextSettings;
    setSettings((current) => {
      nextSettings = saveSettings({ ...current, ...patch });
      return nextSettings;
    });
    return nextSettings;
  }, []);

  const resetToDefaults = useCallback(() => {
    const next = resetStoredSettings();
    setSettings(next);
    return next;
  }, []);

  return {
    settings,
    defaultSettings,
    updateSettings,
    resetToDefaults,
  };
}