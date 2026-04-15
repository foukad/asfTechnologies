"use client";

import { useState } from "react";

export default function RegisterPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);

    async function register() {
        setLoading(true);

        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/login/register`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        setLoading(false);

        if (!res.ok) {
            alert("Échec de l'inscription");
            return;
        }

        alert("Compte créé avec succès. Vous pouvez maintenant vous connecter.");
        window.location.href = "/login";
    }

    return (
        <div className="p-6 max-w-sm mx-auto">
            <h1 className="text-xl font-bold mb-4">Créer un compte</h1>

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
                onClick={register}
                disabled={loading}
            >
                {loading ? "Création..." : "S'inscrire"}
            </button>

            <p className="text-center mt-4 text-sm">
                Déjà un compte ?{" "}
                <a href="/login" className="text-primary underline">
                    Se connecter
                </a>
            </p>
        </div>
    );
}
