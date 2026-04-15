"use client";

import { useAuth } from "@/hooks/useAuth";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { ClipboardList, Flame, Hammer, LogOut, Users, Wrench } from "lucide-react";

const links = [
  {
    href: "/clients",
    label: "Clients",
    icon: Users,
  },
  {
    href: "/equipments",
    label: "Équipements",
    icon: Wrench,
  },
  {
    href: "/technicians",
    label: "Techniciens",
    icon: Hammer,
  },
  {
    href: "/interventions",
    label: "Interventions",
    icon: ClipboardList,
  },
];

const sections = [
  { match: "/clients/new", title: "Nouveau client", subtitle: "Creation et qualification" },
  { match: "/clients/", title: "Fiche client", subtitle: "Historique et equipements" },
  { match: "/clients", title: "Clients", subtitle: "Portefeuille et relationnel" },
  { match: "/equipments/new", title: "Nouvel equipement", subtitle: "Mise en parc" },
  { match: "/equipments", title: "Equipements", subtitle: "Suivi des installations" },
  { match: "/technicians/new", title: "Nouveau technicien", subtitle: "Ressources terrain" },
  { match: "/technicians", title: "Techniciens", subtitle: "Capacites et affectations" },
  { match: "/interventions/new", title: "Nouvelle intervention", subtitle: "Planification terrain" },
  { match: "/interventions", title: "Interventions", subtitle: "Operations en cours" },
  { match: "/login", title: "Connexion", subtitle: "Acces securise" },
  { match: "/register", title: "Inscription", subtitle: "Configuration initiale" },
];

function getSection(pathname: string) {
  return sections.find((section) => pathname.startsWith(section.match)) ?? {
    title: "Centre d'exploitation",
    subtitle: "Pilotage HeatOps",
  };
}

export default function NavigationBar() {
  const pathname = usePathname();
  const { isAuthenticated, logout } = useAuth();
  const section = getSection(pathname);

  return (
    <header
      className="overflow-x-auto overflow-y-hidden rounded-[18px] px-4 py-3"
      style={{
        background: "var(--surface)",
        border: "1px solid var(--border)",
        boxShadow: "var(--shadow-soft)",
      }}
    >
      <nav className="flex min-w-max flex-nowrap items-center gap-2">
        <Link href="/clients" className="mr-3 flex shrink-0 items-center gap-3 rounded-[14px] px-2 py-1">
          <div
            className="flex h-10 w-10 items-center justify-center rounded-2xl text-white"
            style={{
              background: "linear-gradient(135deg, var(--primary), var(--primary-hover))",
              boxShadow: "0 1px 2px rgba(0, 0, 0, 0.05)",
            }}
          >
            <Flame size={20} />
          </div>
      
        </Link>

        {links.map(({ href, label, icon: Icon }) => {
          const active = pathname === href || pathname.startsWith(`${href}/`);
          return (
            <Link
              key={href}
              href={href}
              className="group flex shrink-0 items-center gap-2 whitespace-nowrap rounded-[14px] px-4 py-2.5 text-sm font-semibold transition-all"
              style={
                active
                  ? {
                      background: "var(--primary-light)",
                      color: "var(--text)",
                    }
                  : {
                      background: "transparent",
                      color: "var(--text-muted)",
                    }
              }
            >
              <div
                className="flex h-8 w-8 items-center justify-center rounded-[10px] transition-all"
                style={{
                  background: active ? "rgba(59, 130, 246, 0.12)" : "rgba(148, 163, 184, 0.12)",
                  color: active ? "var(--primary)" : "var(--text-muted)",
                }}
              >
                <Icon size={16} />
              </div>
              <span className="whitespace-nowrap tracking-tight">{label}</span>
            </Link>
          );
        })}

        <div
          className="ml-3 flex shrink-0 items-center gap-3 rounded-[14px] px-3 py-2"
          style={{
            background: "var(--surface-hover)",
            border: "1px solid var(--border)",
          }}
        >
          <span className="whitespace-nowrap text-sm font-semibold" style={{ color: "var(--text)" }}>
            {section.title}
          </span>
          <span
            className="whitespace-nowrap rounded-full px-2.5 py-1 text-[11px] font-medium"
            style={{
              background: "var(--primary-light)",
              color: "var(--primary)",
            }}
          >
            {section.subtitle}
          </span>
        </div>

        <div
          className="ml-1 flex shrink-0 items-center gap-2 rounded-full px-3 py-2 text-xs font-medium"
          style={{
            background: "var(--surface-hover)",
            color: "var(--text-muted)",
            border: "1px solid var(--border)",
          }}
        >
          <span
            className="h-2 w-2 rounded-full"
            style={{ background: "var(--primary)", boxShadow: "0 0 0 4px rgba(59, 130, 246, 0.12)" }}
          />
          Flux metier
        </div>

        {isAuthenticated && (
          <button onClick={logout} className="btn-secondary ml-3 shrink-0">
            <LogOut size={16} />
            Déconnexion
          </button>
        )}
      </nav>
    </header>
  );
}
