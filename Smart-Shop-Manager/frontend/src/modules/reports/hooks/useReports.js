import { useCallback, useEffect, useMemo, useState } from "react";
import { getReport } from "../services/reportService";
import { getReportDefinition, reportOptions } from "../constants";
import { getApiErrorMessage } from "../../../services/apiErrors";

export function useReports(initialType = "sales") {
  const [type, setType] = useState(initialType);
  const [reportRows, setReportRows] = useState([]);
  const [profit, setProfit] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [lastUpdated, setLastUpdated] = useState(null);

  const load = useCallback(async (selectedType = type) => {
    setLoading(true);
    setError("");
    setReportRows([]);
    setProfit(null);

    try {
      const [report, profitData] = await Promise.all([getReport(selectedType), getReport("profit")]);
      setReportRows(Array.isArray(report) ? report : []);
      setProfit(profitData ?? null);
      setLastUpdated(new Date());
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "The report could not be loaded."));
    } finally {
      setLoading(false);
    }
  }, [type]);

  useEffect(() => {
    const timer = setTimeout(() => load(type), 0);
    return () => clearTimeout(timer);
  }, [load, type]);

  const refresh = useCallback(() => load(type), [load, type]);
  const config = useMemo(() => getReportDefinition(type), [type]);

  return {
    type,
    setType,
    reportRows,
    profit,
    loading,
    error,
    lastUpdated,
    refresh,
    config,
    reportOptions,
  };
}