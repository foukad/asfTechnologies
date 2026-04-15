"use client";

import { useEffect, useState } from "react";
import { api } from "@/lib/api";
import { useAuth } from "@/hooks/useAuth";
import { Mail, Phone, Users } from "lucide-react";

export default function ClientsPage() {
    const { isAuthenticated, loading } = useAuth();
    const [clients, setClients] = useState<any[]>([]);
    const [loadingClients, setLoadingClients] = useState(true);

    useEffect(() => {
        if (!loading && isAuthenticated) {
            loadClients();
        }
    }, [loading, isAuthenticated]);

    async function loadClients() {
        try {
            const data = await api("/clients");
            setClients(data.content || []);
        } catch (err) {
            console.error("Erreur API:", err);
        }
        setLoadingClients(false);
    }

    if (loading) {
        return <div className="p-8 text-sm" style={{ color: "var(--text-muted)" }}>Chargement...</div>;
    }

    if (!isAuthenticated) {
        return null;
    }

    const clientsWithEmail = clients.filter((client) => client.email).length;
    const clientsWithPhone = clients.filter((client) => client.phone).length;

    return (
        <div className="page-shell">
            <div className="page-header">
                <div className="page-heading">
                    <p className="page-kicker">Relation client</p>
                    <h1 className="page-title">Clients</h1>
                    <p className="page-subtitle">
                        Suivi de votre portefeuille client, des coordonnees et des fiches d&apos;intervention.
                    </p>
                </div>
                <div className="page-actions">
                    <a href="/clients/new" className="btn-primary">+ Nouveau client</a>
                </div>
            </div>

            <div className="stats-grid">
                <div className="stat-card">
                    <div className="stat-label">Portefeuille</div>
                    <div className="stat-value">{clients.length}</div>
                    <div className="stat-meta">Clients suivis dans HeatOps</div>
                </div>
                <div className="stat-card">
                    <div className="stat-label">Emails renseignes</div>
                    <div className="stat-value">{clientsWithEmail}</div>
                    <div className="stat-meta">Contacts joignables par email</div>
                </div>
                <div className="stat-card">
                    <div className="stat-label">Telephones renseignes</div>
                    <div className="stat-value">{clientsWithPhone}</div>
                    <div className="stat-meta">Contacts joignables rapidement</div>
                </div>
            </div>

            <div className="card p-0 overflow-hidden">
                {loadingClients ? (
                    <div className="p-6 text-sm" style={{ color: "var(--text-muted)" }}>Chargement des clients...</div>
                ) : clients.length === 0 ? (
                    <div className="empty-state">
                        <div className="empty-state-icon"><Users size={24} /></div>
                        <p className="empty-state-title">Aucun client pour le moment</p>
                        <p className="empty-state-copy">
                            Cree ton premier client pour commencer a centraliser les equipements et les interventions.
                        </p>
                        <a href="/clients/new" className="btn-primary mt-5">+ Creer le premier client</a>
                    </div>
                ) : (
                    <table className="table-heatops">
                        <thead>
                            <tr>
                                <th>Client</th>
                                <th>Contact</th>
                                <th>Téléphone</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {clients.map((client) => (
                                <tr key={client.id}>
                                    <td>
                                        <div className="font-medium">{client.firstName} {client.lastName}</div>
                                        <div className="mt-1 text-xs" style={{ color: "var(--text-muted)" }}>
                                            Fiche client
                                        </div>
                                    </td>
                                    <td>
                                        <div className="inline-flex items-center gap-2" style={{ color: "var(--text-muted)" }}>
                                            <Mail size={14} />
                                            <span>{client.email || "Non renseigne"}</span>
                                        </div>
                                    </td>
                                    <td>
                                        <div className="inline-flex items-center gap-2" style={{ color: "var(--text-muted)" }}>
                                            <Phone size={14} />
                                            <span>{client.phone || "Non renseigne"}</span>
                                        </div>
                                    </td>
                                    <td className="text-right">
                                        <a href={`/clients/${client.id}`} className="btn-ghost">
                                            Ouvrir
                                        </a>
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
