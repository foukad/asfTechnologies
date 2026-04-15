"use client";

import { useState } from "react";
import { useAuth } from "@/hooks/useAuth";

export default function LoginPage() {
    const { login } = useAuth();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    async function handleLogin() {
        setLoading(true);
        setError("");
        try {
            await login(email, password);
            window.location.href = "/clients";
        } catch {
            setError("Email ou mot de passe incorrect.");
        }
        setLoading(false);
    }

    return (
        <div className="min-h-screen flex items-center justify-center" style={{ background: "var(--bg)" }}>
            <div className="w-full max-w-sm">
                {/* Logo */}
                <div className="text-center mb-8">
                    <div className="text-4xl mb-2">🔥</div>
                    <h1 className="text-2xl font-bold" style={{ color: "var(--text)" }}>HeatOps</h1>
                    <p className="text-sm mt-1" style={{ color: "var(--text-muted)" }}>Gestion d’interventions CVC</p>
                </div>

                <div className="card space-y-4">
                    <h2 className="text-base font-semibold" style={{ color: "var(--text)" }}>Connexion</h2>

                    {error && (
                        <p className="alert-error">{error}</p>
                    )}

                    <input
                        className="input"
                        placeholder="Email"
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />

                    <input
                        className="input"
                        placeholder="Mot de passe"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        onKeyDown={(e) => e.key === "Enter" && handleLogin()}
                    />

                    <button
                        className="btn-primary w-full"
                        onClick={handleLogin}
                        disabled={loading}
                    >
                        {loading ? "Connexion..." : "Se connecter"}
                    </button>
                </div>

                <p className="text-center mt-5 text-sm" style={{ color: "var(--text-muted)" }}>
                    Pas encore de compte ?{" "}
                    <a href="/register" style={{ color: "var(--primary)" }} className="font-medium hover:underline">
                        S’inscrire
                    </a>
                </p>
            </div>
        </div>
    );
}
