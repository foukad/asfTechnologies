"use client";

import { useState } from "react";

export default function LoginPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    async function login() {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/auth/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        if (!res.ok) {
            alert("Échec de la connexion");
            return;
        }

        const data = await res.json();

        // Stockage du token et du tenant
        localStorage.setItem("token", data.token);
        localStorage.setItem("tenantId", data.tenantId);

        // Redirection vers les clients
        window.location.href = "/clients";
    }

    return (
        <div className="p-6 max-w-sm mx-auto">
            <h1 className="text-xl font-bold mb-4">Connexion</h1>

            <input
                className="border p-2 mb-2 w-full"
                placeholder="Email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
            />

            <input
                className="border p-2 mb-2 w-full"
                placeholder="Mot de passe"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />

            <button
                className="bg-primary text-white px-4 py-2 w-full"
                onClick={login}
            >
                Se connecter
            </button>
        </div>
    );
}
