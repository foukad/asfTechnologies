"use client";

import { useEffect, useState } from "react";
import { api } from "@/lib/api";
import { Link2, ShieldCheck, Wrench } from "lucide-react";

type Equipment = {
  id: string;
  name: string;
  brand: string;
  model: string;
  serialNumber: string;
  year: number;
  client?: { id: string; firstName: string; lastName: string };
};

export default function EquipmentsPage() {
  const [equipments, setEquipments] = useState<Equipment[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api("/equipment")
      .then((data) => setEquipments(data.content || []))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  const linkedCount = equipments.filter((eq) => eq.client).length;
  const recentCount = equipments.filter((eq) => eq.year >= new Date().getFullYear() - 2).length;

  return (
    <div className="page-shell">
      <div className="page-header">
        <div className="page-heading">
          <p className="page-kicker">Parc technique</p>
          <h1 className="page-title">Équipements</h1>
          <p className="page-subtitle">
            Vue d&apos;ensemble du parc installe, des numeros de serie et des rattachements clients.
          </p>
        </div>
        <div className="page-actions">
          <a href="/equipments/new" className="btn-primary">+ Nouvel équipement</a>
        </div>
      </div>

      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-label">Parc total</div>
          <div className="stat-value">{equipments.length}</div>
          <div className="stat-meta">Equipements suivis</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Rattaches</div>
          <div className="stat-value">{linkedCount}</div>
          <div className="stat-meta">Associes a une fiche client</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Recents</div>
          <div className="stat-value">{recentCount}</div>
          <div className="stat-meta">Installes sur les 2 dernieres annees</div>
        </div>
      </div>

      <div className="card p-0 overflow-hidden">
        {loading ? (
          <div className="p-6 text-sm" style={{ color: "var(--text-muted)" }}>Chargement...</div>
        ) : equipments.length === 0 ? (
          <div className="empty-state">
            <div className="empty-state-icon"><Wrench size={24} /></div>
            <p className="empty-state-title">Aucun équipement pour le moment</p>
            <p className="empty-state-copy">
              Ajoute le premier equipement pour suivre le parc installe et le relier a tes clients.
            </p>
            <a href="/equipments/new" className="btn-primary mt-5">+ Nouvel équipement</a>
          </div>
        ) : (
          <table className="table-heatops">
            <thead>
              <tr>
                <th>Nom</th>
                <th>Marque / Modèle</th>
                <th>N° Série</th>
                <th>Année</th>
                <th>Client</th>
              </tr>
            </thead>
            <tbody>
              {equipments.map((eq) => (
                <tr key={eq.id}>
                  <td>
                    <div className="font-medium">{eq.name}</div>
                    <div className="mt-1 text-xs" style={{ color: "var(--text-muted)" }}>
                      Installation technique
                    </div>
                  </td>
                  <td>
                    <div className="inline-flex items-center gap-2" style={{ color: "var(--text-muted)" }}>
                      <ShieldCheck size={14} />
                      <span>{eq.brand} {eq.model}</span>
                    </div>
                  </td>
                  <td style={{ color: "var(--text-muted)" }}>{eq.serialNumber}</td>
                  <td>
                    <span className="badge-planned">{eq.year}</span>
                  </td>
                  <td>
                    {eq.client
                      ? <a href={`/clients/${eq.client.id}`} className="inline-flex items-center gap-2 font-medium hover:underline" style={{ color: "var(--primary)" }}><Link2 size={14} />{eq.client.firstName} {eq.client.lastName}</a>
                      : <span style={{ color: "var(--text-muted)" }}>—</span>}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
