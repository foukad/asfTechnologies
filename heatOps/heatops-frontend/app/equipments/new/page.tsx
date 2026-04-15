"use client";

import { useEffect, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { api } from "@/lib/api";

type Client = { id: string; firstName: string; lastName: string };

export default function NewEquipmentPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const preselectedClientId = searchParams.get("clientId") || "";

  const [clients, setClients] = useState<Client[]>([]);
  const [form, setForm] = useState({
    clientId: preselectedClientId,
    name: "",
    brand: "",
    model: "",
    serialNumber: "",
    year: new Date().getFullYear().toString(),
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    api("/clients")
      .then((data) => setClients(data.content || []))
      .catch(console.error);
  }, []);

  async function submit() {
    if (!form.clientId) {
      setError("Veuillez sélectionner un client.");
      return;
    }
    setLoading(true);
    setError("");
    try {
      await api(`/equipment/client/${form.clientId}`, {
        method: "POST",
        body: JSON.stringify({
          name: form.name,
          brand: form.brand,
          model: form.model,
          serialNumber: form.serialNumber,
          year: parseInt(form.year),
        }),
      });
      router.push("/equipments");
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
        <h1 className="page-title">Nouvel équipement</h1>
      </div>

      <div className="card space-y-4">
        {error && <p className="alert-error">{error}</p>}

        <select
          className="input"
          value={form.clientId}
          onChange={(e) => setForm({ ...form, clientId: e.target.value })}
        >
          <option value="">— Sélectionner un client * —</option>
          {clients.map((c) => (
            <option key={c.id} value={c.id}>{c.firstName} {c.lastName}</option>
          ))}
        </select>

        <input className="input" placeholder="Nom de l'équipement *" value={form.name}
          onChange={(e) => setForm({ ...form, name: e.target.value })} />
        <input className="input" placeholder="Marque *" value={form.brand}
          onChange={(e) => setForm({ ...form, brand: e.target.value })} />
        <input className="input" placeholder="Modèle *" value={form.model}
          onChange={(e) => setForm({ ...form, model: e.target.value })} />
        <input className="input" placeholder="Numéro de série *" value={form.serialNumber}
          onChange={(e) => setForm({ ...form, serialNumber: e.target.value })} />
        <input className="input" placeholder="Année *" type="number"
          value={form.year} onChange={(e) => setForm({ ...form, year: e.target.value })} />

        <div className="form-actions">
          <button className="btn-primary" onClick={submit} disabled={loading}>
            {loading ? "Création..." : "Créer l'équipement"}
          </button>
          <button className="btn-secondary" onClick={() => router.back()}>Annuler</button>
        </div>
      </div>
    </div>
  );
}
