"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { api } from "@/lib/api";
import { Mail, Phone, Wrench, ClipboardList } from "lucide-react";

type Client = {
  id: string;
  firstName: string;
  lastName: string;
  email?: string;
  phone: string;
  equipment: { id: string; name: string; brand: string; model: string; serialNumber: string }[];
  interventions: { id: string; title: string; status: string; scheduledAt: string }[];
};

const STATUS_BADGE: Record<string, { label: string; cls: string }> = {
  PLANNED:     { label: "Planifiée",   cls: "badge-planned" },
  IN_PROGRESS: { label: "En cours",    cls: "badge-in-progress" },
  DONE:        { label: "Terminée",    cls: "badge-done" },
};

export default function ClientDetailPage() {
  const { id } = useParams<{ id: string }>();
  const router = useRouter();
  const [client, setClient] = useState<Client | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    api(`/clients/${id}`)
      .then(setClient)
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false));
  }, [id]);

  async function handleDelete() {
    if (!confirm("Supprimer ce client ?")) return;
    try {
      await api(`/clients/${id}`, { method: "DELETE" });
      router.push("/clients");
    } catch (e: any) {
      alert(e.message);
    }
  }

  if (loading) return <div className="p-8 text-sm" style={{ color: "var(--text-muted)" }}>Chargement...</div>;
  if (error) return <div className="page-shell"><div className="alert-error">{error}</div></div>;
  if (!client) return null;

  return (
    <div className="page-shell max-w-5xl">
      <div className="page-header">
        <div className="page-heading">
          <p className="page-kicker">Fiche client</p>
          <h1 className="page-title">{client.firstName} {client.lastName}</h1>
          <p className="page-subtitle">
            Consulte l&apos;historique technique, les equipements lies et les interventions planifiees.
          </p>
        </div>

        <div className="page-actions">
          <a
            href={`/equipments/new?clientId=${client.id}`}
            className="btn-primary"
          >
            + Équipement
          </a>
          <a
            href={`/interventions/new?clientId=${client.id}`}
            className="btn-secondary"
          >
            + Intervention
          </a>
          <button
            onClick={handleDelete}
            className="btn-danger"
          >
            Supprimer
          </button>
        </div>
      </div>

      <div className="detail-grid">
        <div className="stat-card">
          <div className="stat-label">Telephone</div>
          <div className="stat-meta inline-flex items-center gap-2">
            <Phone size={14} />
            <span>{client.phone || "Non renseigne"}</span>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Email</div>
          <div className="stat-meta inline-flex items-center gap-2">
            <Mail size={14} />
            <span>{client.email || "Non renseigne"}</span>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Activite</div>
          <div className="stat-value">{(client.equipment?.length ?? 0) + (client.interventions?.length ?? 0)}</div>
          <div className="stat-meta">Equipements et interventions lies</div>
        </div>
      </div>

      <section className="mb-6">
        <h2 className="section-title">
          Équipements ({client.equipment?.length ?? 0})
        </h2>
        <div className="card p-0 overflow-hidden">
          {!client.equipment?.length ? (
            <div className="empty-state">
              <div className="empty-state-icon"><Wrench size={24} /></div>
              <p className="empty-state-title">Aucun équipement lié</p>
              <p className="empty-state-copy">
                Ajoute un equipement pour rattacher une installation a ce client.
              </p>
            </div>
          ) : (
            <table className="table-heatops">
              <thead>
                <tr>
                  <th>Nom</th>
                  <th>Marque / Modèle</th>
                  <th>N° Série</th>
                </tr>
              </thead>
              <tbody>
                {client.equipment.map((eq) => (
                  <tr key={eq.id}>
                    <td>
                      <div className="font-medium">{eq.name}</div>
                      <div className="mt-1 text-xs" style={{ color: "var(--text-muted)" }}>
                        Equipement client
                      </div>
                    </td>
                    <td style={{ color: "var(--text-muted)" }}>{eq.brand} {eq.model}</td>
                    <td style={{ color: "var(--text-muted)" }}>{eq.serialNumber}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </section>

      <section>
        <h2 className="section-title">
          Interventions ({client.interventions?.length ?? 0})
        </h2>
        <div className="card p-0 overflow-hidden">
          {!client.interventions?.length ? (
            <div className="empty-state">
              <div className="empty-state-icon"><ClipboardList size={24} /></div>
              <p className="empty-state-title">Aucune intervention liée</p>
              <p className="empty-state-copy">
                Planifie une premiere mission pour ce client afin de suivre l&apos;historique terrain.
              </p>
            </div>
          ) : (
            <table className="table-heatops">
              <thead>
                <tr>
                  <th>Titre</th>
                  <th>Statut</th>
                  <th>Date prévue</th>
                </tr>
              </thead>
              <tbody>
                {client.interventions.map((iv) => {
                  const s = STATUS_BADGE[iv.status] ?? { label: iv.status, cls: "" };
                  return (
                    <tr key={iv.id}>
                      <td>
                        <div className="font-medium">{iv.title}</div>
                        <div className="mt-1 text-xs" style={{ color: "var(--text-muted)" }}>
                          Intervention client
                        </div>
                      </td>
                      <td><span className={s.cls}>{s.label}</span></td>
                      <td style={{ color: "var(--text-muted)" }}>
                        {new Date(iv.scheduledAt).toLocaleDateString("fr-FR")}
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          )}
        </div>
      </section>
    </div>
  );
}
