"use client";

import { useState } from "react";

export default function RegisterPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [companyName, setCompanyName] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    async function register() {
        setLoading(true);
        setError("");

        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/auth/register`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password, companyName })
        });

        setLoading(false);

        if (!res.ok) {
            setError("Échec de l'inscription. Vérifiez vos informations.");
            return;
        }

        alert("Compte créé avec succès. Vous pouvez maintenant vous connecter.");
        window.location.href = "/login";
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
                    <h2 className="text-base font-semibold" style={{ color: "var(--text)" }}>Créer un compte</h2>

                    {error && (
                        <p className="alert-error">{error}</p>
                    )}

                    <input
                        className="input"
                        placeholder="Nom de l'entreprise"
                        type="text"
                        value={companyName}
                        onChange={(e) => setCompanyName(e.target.value)}
                    />

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
                    />

                    <button
                        className="btn-primary w-full"
                        onClick={register}
                        disabled={loading}
                    >
                        {loading ? "Création..." : "S'inscrire"}
                    </button>
                </div>

                <p className="text-center mt-5 text-sm" style={{ color: "var(--text-muted)" }}>
                    Déjà un compte ?{" "}
                    <a href="/login" style={{ color: "var(--primary)" }} className="font-medium hover:underline">
                        Se connecter
                    </a>
                </p>
            </div>
        </div>
    );
}
