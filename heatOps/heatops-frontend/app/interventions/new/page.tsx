"use client";

import { useEffect, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { api } from "@/lib/api";

type Client = { id: string; firstName: string; lastName: string };
type Equipment = { id: string; name: string; brand: string; model: string };
type Technician = { id: string; firstName: string; lastName: string };

export default function NewInterventionPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const preselectedClientId = searchParams.get("clientId") || "";

  const [clients, setClients] = useState<Client[]>([]);
  const [equipments, setEquipments] = useState<Equipment[]>([]);
  const [technicians, setTechnicians] = useState<Technician[]>([]);

  const [form, setForm] = useState({
    title: "",
    description: "",
    scheduledAt: "",
    clientId: preselectedClientId,
    equipmentId: "",
    technicianId: "",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    Promise.all([
      api("/clients").then((d) => setClients(d.content || [])),
      api("/technicians").then((d) => setTechnicians(d.content || [])),
    ]).catch(console.error);
  }, []);

  useEffect(() => {
    if (!form.clientId) {
      setEquipments([]);
      setForm((f) => ({ ...f, equipmentId: "" }));
      return;
    }
    api(`/equipment/client/${form.clientId}`)
      .then((data) => setEquipments(data.content || data || []))
      .catch(console.error);
  }, [form.clientId]);

  async function submit() {
    setLoading(true);
    setError("");
    try {
      await api("/interventions", {
        method: "POST",
        body: JSON.stringify({
          ...form,
          scheduledAt: form.scheduledAt ? new Date(form.scheduledAt).toISOString().replace("Z", "") : null,
          clientId: form.clientId || null,
          equipmentId: form.equipmentId || null,
          technicianId: form.technicianId || null,
        }),
      });
      router.push("/interventions");
    } catch (e: any) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="p-8 max-w-lg">
      <div className="flex items-center gap-3 mb-6">
        <button onClick={() => router.back()} className="btn-ghost">
          ← Retour
        </button>
        <h1 className="page-title">Nouvelle intervention</h1>
      </div>

      <div className="card space-y-4">
        {error && <p className="alert-error">{error}</p>}

        <input className="input" placeholder="Titre *" value={form.title}
          onChange={(e) => setForm({ ...form, title: e.target.value })} />

        <textarea className="input" placeholder="Description *" rows={3}
          value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />

        <div>
          <label className="block text-xs font-semibold uppercase tracking-[0.14em] mb-2" style={{ color: "var(--text-muted)" }}>Date prévue *</label>
          <input className="input" type="datetime-local" value={form.scheduledAt}
            onChange={(e) => setForm({ ...form, scheduledAt: e.target.value })} />
        </div>

        <select className="input" value={form.clientId}
          onChange={(e) => setForm({ ...form, clientId: e.target.value, equipmentId: "" })}>
          <option value="">— Client * —</option>
          {clients.map((c) => (
            <option key={c.id} value={c.id}>{c.firstName} {c.lastName}</option>
          ))}
        </select>

        <select className="input" value={form.equipmentId}
          onChange={(e) => setForm({ ...form, equipmentId: e.target.value })}
          disabled={!form.clientId}>
          <option value="">— Équipement{!form.clientId ? " (sélectionner un client d'abord)" : " *"} —</option>
          {equipments.map((eq) => (
            <option key={eq.id} value={eq.id}>{eq.name} — {eq.brand} {eq.model}</option>
          ))}
        </select>

        <select className="input" value={form.technicianId}
          onChange={(e) => setForm({ ...form, technicianId: e.target.value })}>
          <option value="">— Technicien * —</option>
          {technicians.map((t) => (
            <option key={t.id} value={t.id}>{t.firstName} {t.lastName}</option>
          ))}
        </select>

        <div className="form-actions">
          <button className="btn-primary" onClick={submit} disabled={loading}>
            {loading ? "Création..." : "Créer l'intervention"}
          </button>
          <button className="btn-secondary" onClick={() => router.back()}>Annuler</button>
        </div>
      </div>
    </div>
  );
}
