"use client";

import { useEffect, useState } from "react";
import { api } from "@/lib/api";
import { CalendarDays, ClipboardList, UserRound } from "lucide-react";

type Intervention = {
  id: string;
  title: string;
  status: "PLANNED" | "IN_PROGRESS" | "DONE";
  scheduledAt: string;
  client?: { id: string; firstName: string; lastName: string };
  technician?: { id: string; firstName: string; lastName: string };
  equipment?: { id: string; name: string };
};

const STATUS_BADGE: Record<string, { label: string; cls: string }> = {
  PLANNED:     { label: "Planifiée",   cls: "badge-planned" },
  IN_PROGRESS: { label: "En cours",    cls: "badge-in-progress" },
  DONE:        { label: "Terminée",    cls: "badge-done" },
};

export default function InterventionsPage() {
  const [interventions, setInterventions] = useState<Intervention[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api("/interventions")
      .then((data) => setInterventions(data.content || []))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  const plannedCount = interventions.filter((intervention) => intervention.status === "PLANNED").length;
  const inProgressCount = interventions.filter((intervention) => intervention.status === "IN_PROGRESS").length;
  const doneCount = interventions.filter((intervention) => intervention.status === "DONE").length;

  return (
    <div className="page-shell">
      <div className="page-header">
        <div className="page-heading">
          <p className="page-kicker">Pilotage terrain</p>
          <h1 className="page-title">Interventions</h1>
          <p className="page-subtitle">
            Suivi des missions planifiees, de l&apos;assignation terrain et de l&apos;avancement d&apos;execution.
          </p>
        </div>
        <div className="page-actions">
          <a href="/interventions/new" className="btn-primary">+ Nouvelle intervention</a>
        </div>
      </div>

      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-label">Planifiees</div>
          <div className="stat-value">{plannedCount}</div>
          <div className="stat-meta">Interventions a venir</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">En cours</div>
          <div className="stat-value">{inProgressCount}</div>
          <div className="stat-meta">Operations en execution</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Terminees</div>
          <div className="stat-value">{doneCount}</div>
          <div className="stat-meta">Dossiers clotures</div>
        </div>
      </div>

      <div className="card p-0 overflow-hidden">
        {loading ? (
          <div className="p-6 text-sm" style={{ color: "var(--text-muted)" }}>Chargement...</div>
        ) : interventions.length === 0 ? (
          <div className="empty-state">
            <div className="empty-state-icon"><ClipboardList size={24} /></div>
            <p className="empty-state-title">Aucune intervention pour le moment</p>
            <p className="empty-state-copy">
              Planifie une premiere mission pour commencer a suivre ton activite terrain.
            </p>
            <a href="/interventions/new" className="btn-primary mt-5">+ Nouvelle intervention</a>
          </div>
        ) : (
          <table className="table-heatops">
            <thead>
              <tr>
                <th>Titre</th>
                <th>Statut</th>
                <th>Date prévue</th>
                <th>Client</th>
                <th>Technicien</th>
                <th>Équipement</th>
              </tr>
            </thead>
            <tbody>
              {interventions.map((i) => {
                const s = STATUS_BADGE[i.status] ?? { label: i.status, cls: "" };
                return (
                  <tr key={i.id}>
                    <td>
                      <div className="font-medium">{i.title}</div>
                      <div className="mt-1 text-xs" style={{ color: "var(--text-muted)" }}>
                        Mission terrain
                      </div>
                    </td>
                    <td><span className={s.cls}>{s.label}</span></td>
                    <td>
                      <div className="inline-flex items-center gap-2" style={{ color: "var(--text-muted)" }}>
                        <CalendarDays size={14} />
                        <span>{new Date(i.scheduledAt).toLocaleDateString("fr-FR", { day: "2-digit", month: "short", year: "numeric", hour: "2-digit", minute: "2-digit" })}</span>
                      </div>
                    </td>
                    <td>
                      {i.client
                        ? <a href={`/clients/${i.client.id}`} className="font-medium hover:underline" style={{ color: "var(--primary)" }}>{i.client.firstName} {i.client.lastName}</a>
                        : <span style={{ color: "var(--text-muted)" }}>—</span>}
                    </td>
                    <td>
                      <div className="inline-flex items-center gap-2" style={{ color: "var(--text-muted)" }}>
                        <UserRound size={14} />
                        <span>{i.technician ? `${i.technician.firstName} ${i.technician.lastName}` : "Non assigné"}</span>
                      </div>
                    </td>
                    <td style={{ color: "var(--text-muted)" }}>{i.equipment?.name || "—"}</td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
