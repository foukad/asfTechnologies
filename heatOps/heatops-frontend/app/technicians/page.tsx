"use client";

import { useEffect, useState } from "react";
import { api } from "@/lib/api";
import { Hammer, Mail, Phone } from "lucide-react";

type Technician = {
  id: string;
  firstName: string;
  lastName: string;
  phone: string;
  email?: string;
};

export default function TechniciansPage() {
  const [technicians, setTechnicians] = useState<Technician[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api("/technicians")
      .then((data) => setTechnicians(data.content || []))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  const withEmail = technicians.filter((technician) => technician.email).length;

  return (
    <div className="page-shell">
      <div className="page-header">
        <div className="page-heading">
          <p className="page-kicker">Équipe terrain</p>
          <h1 className="page-title">Techniciens</h1>
          <p className="page-subtitle">
            Gestion des ressources terrain, des coordonnees et des disponibilites d&apos;intervention.
          </p>
        </div>
        <div className="page-actions">
          <a href="/technicians/new" className="btn-primary">+ Nouveau technicien</a>
        </div>
      </div>

      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-label">Effectif</div>
          <div className="stat-value">{technicians.length}</div>
          <div className="stat-meta">Techniciens actifs</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Emails renseignes</div>
          <div className="stat-value">{withEmail}</div>
          <div className="stat-meta">Contacts avec adresse email</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Joignables</div>
          <div className="stat-value">{technicians.length}</div>
          <div className="stat-meta">Tous les profils ont un telephone</div>
        </div>
      </div>

      <div className="card p-0 overflow-hidden">
        {loading ? (
          <div className="p-6 text-sm" style={{ color: "var(--text-muted)" }}>Chargement...</div>
        ) : technicians.length === 0 ? (
          <div className="empty-state">
            <div className="empty-state-icon"><Hammer size={24} /></div>
            <p className="empty-state-title">Aucun technicien pour le moment</p>
            <p className="empty-state-copy">
              Cree le premier profil technicien pour organiser tes affectations terrain.
            </p>
            <a href="/technicians/new" className="btn-primary mt-5">+ Nouveau technicien</a>
          </div>
        ) : (
          <table className="table-heatops">
            <thead>
              <tr>
                <th>Nom</th>
                <th>Téléphone</th>
                <th>Email</th>
              </tr>
            </thead>
            <tbody>
              {technicians.map((t) => (
                <tr key={t.id}>
                  <td>
                    <div className="font-medium">{t.firstName} {t.lastName}</div>
                    <div className="mt-1 text-xs" style={{ color: "var(--text-muted)" }}>
                      Ressource terrain
                    </div>
                  </td>
                  <td>
                    <div className="inline-flex items-center gap-2" style={{ color: "var(--text-muted)" }}>
                      <Phone size={14} />
                      <span>{t.phone}</span>
                    </div>
                  </td>
                  <td>
                    <div className="inline-flex items-center gap-2" style={{ color: "var(--text-muted)" }}>
                      <Mail size={14} />
                      <span>{t.email || "Non renseigne"}</span>
                    </div>
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
