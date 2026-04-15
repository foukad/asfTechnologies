import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

export function proxy(request: NextRequest) {
    const token = request.cookies.get("token")?.value || null;
    const tenantId = request.cookies.get("tenantId")?.value || null;

    const isAuthPage =
        request.nextUrl.pathname.startsWith("/login") ||
        request.nextUrl.pathname.startsWith("/register");

    // Si déjà connecté → empêcher d'aller sur /login ou /register
    if (isAuthPage && token) {
        return NextResponse.redirect(new URL("/clients", request.url));
    }

    // Pages privées
    const isPrivatePage =
        request.nextUrl.pathname.startsWith("/clients") ||
        request.nextUrl.pathname.startsWith("/equipments") ||
        request.nextUrl.pathname.startsWith("/technicians") ||
        request.nextUrl.pathname.startsWith("/interventions") ||
        request.nextUrl.pathname.startsWith("/dashboard");

    // Si pas connecté → redirection vers /login
    if (isPrivatePage && (!token || !tenantId)) {
        return NextResponse.redirect(new URL("/login", request.url));
    }

    return NextResponse.next();
}

export const config = {
    matcher: [
        "/clients/:path*",
        "/equipments/:path*",
        "/technicians/:path*",
        "/interventions/:path*",
        "/dashboard/:path*",
        "/login",
        "/register"
    ],
};
