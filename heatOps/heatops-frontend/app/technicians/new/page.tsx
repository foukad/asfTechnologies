"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { api } from "@/lib/api";

export default function NewTechnicianPage() {
  const router = useRouter();
  const [form, setForm] = useState({ firstName: "", lastName: "", phone: "", email: "" });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  async function submit() {
    setLoading(true);
    setError("");
    try {
      await api("/technicians", {
        method: "POST",
        body: JSON.stringify(form),
      });
      router.push("/technicians");
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
        <h1 className="page-title">Nouveau technicien</h1>
      </div>

      <div className="card space-y-4">
        {error && <p className="alert-error">{error}</p>}

        <input className="input" placeholder="Prénom *" value={form.firstName}
          onChange={(e) => setForm({ ...form, firstName: e.target.value })} />
        <input className="input" placeholder="Nom *" value={form.lastName}
          onChange={(e) => setForm({ ...form, lastName: e.target.value })} />
        <input className="input" placeholder="Téléphone *" value={form.phone}
          onChange={(e) => setForm({ ...form, phone: e.target.value })} />
        <input className="input" placeholder="Email" type="email" value={form.email}
          onChange={(e) => setForm({ ...form, email: e.target.value })} />

        <div className="form-actions">
          <button className="btn-primary" onClick={submit} disabled={loading}>
            {loading ? "Création..." : "Créer le technicien"}
          </button>
          <button className="btn-secondary" onClick={() => router.back()}>Annuler</button>
        </div>
      </div>
    </div>
  );
}
