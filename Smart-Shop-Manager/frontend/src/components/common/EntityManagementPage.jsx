import { useCallback, useEffect, useMemo, useState } from "react";
import AddOutlinedIcon from "@mui/icons-material/AddOutlined";
import DeleteIcon from "@mui/icons-material/Delete";
import EditOutlinedIcon from "@mui/icons-material/EditOutlined";
import { Alert, Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, IconButton, MenuItem, Paper, Stack, Table, TableBody, TableCell, TableHead, TableRow, TextField, Typography } from "@mui/material";
import api from "../../services/api";
import { getApiErrorMessage } from "../../services/apiErrors";

const emptyFrom = (fields) => Object.fromEntries(fields.map(({ key, type }) => [key, type === "checkbox" ? true : ""]));
const apiError = (error) => getApiErrorMessage(error, "The request could not be completed. Please check the values and try again.");

export default function EntityManagementPage({ title, endpoint, fields, columns }) {
  const [rows, setRows] = useState([]); const [error, setError] = useState(""); const [dialogOpen, setDialogOpen] = useState(false); const [form, setForm] = useState(() => emptyFrom(fields)); const [editingId, setEditingId] = useState(null); const [saving, setSaving] = useState(false);
  const load = useCallback(async () => { try { setError(""); const { data } = await api.get(endpoint); setRows(data); } catch (requestError) { setError(apiError(requestError)); } }, [endpoint]);
  useEffect(() => { const timer = setTimeout(load, 0); return () => clearTimeout(timer); }, [load]);
  const isEditing = useMemo(() => editingId !== null, [editingId]);
  function openCreate() { setEditingId(null); setForm(emptyFrom(fields)); setDialogOpen(true); }
  function openEdit(row) { setEditingId(row.id); setForm(Object.fromEntries(fields.map(({ key, type }) => [key, type === "date" && row[key] ? String(row[key]).slice(0, 10) : row[key] ?? (type === "checkbox" ? true : "")] ))); setDialogOpen(true); }
  async function submit(event) { event.preventDefault(); setSaving(true); try { if (isEditing) await api.put(`${endpoint}/${editingId}`, form); else await api.post(endpoint, form); setDialogOpen(false); await load(); } catch (requestError) { setError(apiError(requestError)); } finally { setSaving(false); } }
  async function remove(id) { if (!window.confirm("Delete this record? This cannot be undone.")) return; try { await api.delete(`${endpoint}/${id}`); await load(); } catch (requestError) { setError(apiError(requestError)); } }
  return <Stack spacing={3}><Stack direction={{ xs: "column", sm: "row" }} spacing={1} sx={{ justifyContent: "space-between", alignItems: { sm: "center" } }}><Box><Typography variant="h4">{title}</Typography><Typography color="text.secondary">Create, update, and manage your {title.toLowerCase()}.</Typography></Box><Button variant="contained" startIcon={<AddOutlinedIcon />} onClick={openCreate}>Add new</Button></Stack>
    {error && <Alert severity="error" onClose={() => setError("")}>{error}</Alert>}
    <Paper sx={{ overflowX: "auto" }}><Table><TableHead><TableRow>{columns.map((column) => <TableCell key={column.key}>{column.label}</TableCell>)}<TableCell align="right">Actions</TableCell></TableRow></TableHead><TableBody>{rows.map((row) => <TableRow key={row.id} hover>{columns.map((column) => <TableCell key={column.key}>{column.render ? column.render(row[column.key], row) : String(row[column.key] ?? "—")}</TableCell>)}<TableCell align="right"><IconButton aria-label="edit" onClick={() => openEdit(row)}><EditOutlinedIcon /></IconButton><IconButton aria-label="delete" color="error" onClick={() => remove(row.id)}><DeleteIcon /></IconButton></TableCell></TableRow>)}{!rows.length && <TableRow><TableCell colSpan={columns.length + 1} align="center">No records found.</TableCell></TableRow>}</TableBody></Table></Paper>
    <Dialog open={dialogOpen} onClose={() => !saving && setDialogOpen(false)} fullWidth maxWidth="sm" slotProps={{ paper: { component: "form", onSubmit: submit } }}><DialogTitle>{isEditing ? "Edit" : "Add"} {title.slice(0, -1)}</DialogTitle><DialogContent><Stack spacing={2} sx={{ pt: 1 }}>{fields.map(({ key, label, type = "text", required, options = [] }) => type === "checkbox" ? <TextField key={key} label={label} select value={String(form[key])} onChange={(event) => setForm({ ...form, [key]: event.target.value === "true" })}><MenuItem value="true">Active</MenuItem><MenuItem value="false">Inactive</MenuItem></TextField> : <TextField key={key} label={label} type={type} required={required} select={Boolean(options.length)} value={form[key]} onChange={(event) => setForm({ ...form, [key]: type === "number" && event.target.value !== "" ? Number(event.target.value) : event.target.value })}>{options.map((option) => <MenuItem key={option.value} value={option.value}>{option.label}</MenuItem>)}</TextField>)}</Stack></DialogContent><DialogActions><Button onClick={() => setDialogOpen(false)}>Cancel</Button><Button type="submit" variant="contained" disabled={saving}>{saving ? "Saving…" : "Save"}</Button></DialogActions></Dialog>
  </Stack>;
}
