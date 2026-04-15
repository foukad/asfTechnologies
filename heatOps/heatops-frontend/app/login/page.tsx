"use client";

import { useState } from "react";
import { useAuth } from "@/hooks/useAuth";

export default function LoginPage() {
    const { login } = useAuth();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);

    async function handleLogin() {
        setLoading(true);
        try {
            await login(email, password);
            window.location.href = "/clients";
        } catch {
            alert("Identifiants incorrects");
        }
        setLoading(false);
    }

    return (
        <div className="p-6 max-w-sm mx-auto">
            <h1 className="text-xl font-bold mb-4">Connexion</h1>

            <input
                className="border p-2 mb-2 w-full rounded"
                placeholder="Email"
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
            />

            <input
                className="border p-2 mb-4 w-full rounded"
                placeholder="Mot de passe"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />

            <button
                className="bg-primary text-white px-4 py-2 w-full rounded disabled:opacity-50"
                onClick={handleLogin}
                disabled={loading}
            >
                {loading ? "Connexion..." : "Se connecter"}
            </button>

            <p className="text-center mt-4 text-sm">
                Pas encore de compte ?{" "}
                <a href="/register" className="text-primary underline">
                    S'inscrire
                </a>
            </p>
        </div>
    );
}
``