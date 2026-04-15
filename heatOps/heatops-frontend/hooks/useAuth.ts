"use client";

import { useEffect, useState } from "react";

function getCookie(name: string) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop()!.split(";").shift()!;
    return null;
}

export function useAuth() {
    const [token, setToken] = useState<string | null>(null);
    const [tenantId, setTenantId] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);

    // Chargement initial depuis les cookies
    useEffect(() => {
        const cookieToken = getCookie("token");
        const cookieTenant = getCookie("tenantId");

        setToken(cookieToken);
        setTenantId(cookieTenant);

        setLoading(false);
    }, []);

    // Login (appelé depuis la page login)
    async function login(email: string, password: string) {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/auth/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        if (!res.ok) {
            throw new Error("Login failed");
        }

        const token = await res.text();

        // Décodage du JWT pour extraire le tenant
        const payload = JSON.parse(atob(token.split(".")[1]));
        const tenant = payload.tenant;

        // Stockage dans les cookies (pour le proxy)
        document.cookie = `token=${token}; path=/`;
        document.cookie = `tenantId=${tenant}; path=/`;

        // Mise à jour du hook
        setToken(token);
        setTenantId(tenant);

        return true;
    }

    // Logout
    function logout() {
        document.cookie = "token=; Max-Age=0; path=/";
        document.cookie = "tenantId=; Max-Age=0; path=/";

        setToken(null);
        setTenantId(null);

        window.location.href = "/login";
    }

    return {
        token,
        tenantId,
        loading,
        isAuthenticated: !!token,
        login,
        logout
    };
}
