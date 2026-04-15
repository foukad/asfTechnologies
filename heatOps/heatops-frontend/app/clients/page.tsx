"use client";

import { useEffect, useState } from "react";
import { api } from "@/lib/api";
import { useAuth } from "@/hooks/useAuth";

export default function ClientsPage() {
    const { isAuthenticated, loading } = useAuth();
    const [clients, setClients] = useState<any[]>([]);
    const [loadingClients, setLoadingClients] = useState(true);

    // 1) Le middleware protège déjà la page
    // Donc ici on attend juste que le hook charge les cookies
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
        return <div className="p-6">Chargement...</div>;
    }

    if (!isAuthenticated) {
        return null; // Le middleware redirige déjà
    }

    return (
        <div className="p-6">
            <h1 className="text-xl font-bold mb-4">Clients</h1>

            <a
                href="/clients/new"
                className="text-primary underline mb-4 inline-block"
            >
                + Nouveau client
            </a>

            {loadingClients ? (
                <div>Chargement des clients...</div>
            ) : clients.length === 0 ? (
                <div>Aucun client pour le moment.</div>
            ) : (
                <ul className="mt-4 space-y-2">
                    {clients.map((client) => (
                        <li
                            key={client.id}
                            className="p-3 border rounded bg-white hover:bg-gray-50"
                        >
                            <a href={`/clients/${client.id}`} className="font-medium">
                                {client.name}
                            </a>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}
