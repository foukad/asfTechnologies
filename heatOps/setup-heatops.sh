#!/bin/bash

echo "🔥 Création du projet Next.js HeatOps..."

# 1. Créer le projet Next.js
npx create-next-app@latest heatops-frontend --ts --eslint --app --no-tailwind --src-dir=false --import-alias="@/*"

cd heatops-frontend

echo "📦 Installation des dépendances supplémentaires..."

# 2. Installer Tailwind + shadcn + libs utiles
npm install tailwindcss postcss autoprefixer
npm install clsx lucide-react
npm install @radix-ui/react-dropdown-menu @radix-ui/react-dialog @radix-ui/react-label @radix-ui/react-slot

# 3. Initialiser Tailwind
npx tailwindcss init -p

echo "🎨 Configuration Tailwind..."

# 4. Remplacer tailwind.config.js
cat > tailwind.config.js << 'EOF'
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./app/**/*.{ts,tsx}",
    "./components/**/*.{ts,tsx}"
  ],
  theme: {
    extend: {
      colors: {
        primary: "#FF6A3D",
        dark: "#1A2A3A"
      }
    }
  },
  plugins: []
};
EOF

echo "🧱 Ajout du CSS global..."

# 5. Remplacer globals.css
cat > app/globals.css << 'EOF'
@tailwind base;
@tailwind components;
@tailwind utilities;

body {
  background: #f7f7f7;
  color: #1A2A3A;
}
EOF

echo "📁 Génération de la structure HeatOps..."

# 6. Créer les dossiers
mkdir -p app/clients app/clients/new app/clients/

\[id\]


mkdir -p app/equipments app/equipments/new
mkdir -p app/technicians app/technicians/new
mkdir -p app/interventions app/interventions/new
mkdir -p app/api/auth
mkdir -p components/ui
mkdir -p lib

echo "🧩 Ajout des fichiers pré-remplis..."

# 7. Layout global
cat > app/layout.tsx << 'EOF'
import "./globals.css";
import Sidebar from "@/components/sidebar";
import Navbar from "@/components/navbar";

export default function RootLayout({ children }) {
  return (
    <html lang="fr">
      <body className="flex h-screen">
        <Sidebar />
        <div className="flex-1 flex flex-col">
          <Navbar />
          <main className="p-6">{children}</main>
        </div>
      </body>
    </html>
  );
}
EOF

# 8. Navbar
cat > components/navbar.tsx << 'EOF'
export default function Navbar() {
  return (
    <div className="h-14 bg-white border-b flex items-center px-4 justify-between">
      <h1 className="font-bold text-lg">HeatOps</h1>
      <div className="text-sm opacity-70">MVP</div>
    </div>
  );
}
EOF

# 9. Sidebar
cat > components/sidebar.tsx << 'EOF'
import Link from "next/link";

export default function Sidebar() {
  return (
    <div className="w-56 bg-dark text-white flex flex-col p-4 space-y-4">
      <Link href="/clients">Clients</Link>
      <Link href="/equipments">Équipements</Link>
      <Link href="/technicians">Techniciens</Link>
      <Link href="/interventions">Interventions</Link>
    </div>
  );
}
EOF

# 10. API client
cat > lib/api.ts << 'EOF'
export async function api(path: string, options: RequestInit = {}) {
  const token = typeof window !== "undefined" ? localStorage.getItem("token") : null;
  const tenantId = typeof window !== "undefined" ? localStorage.getItem("tenantId") : null;

  const headers = {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
    ...(tenantId && { "X-Tenant-ID": tenantId }),
    ...options.headers
  };

  const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}${path}`, {
    ...options,
    headers
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json().catch(() => null);
}
EOF

# 11. Page Clients
cat > app/clients/page.tsx << 'EOF'
import { api } from "@/lib/api";

export default async function ClientsPage() {
  const data = await api("/clients");

  return (
    <div>
      <h1 className="text-xl font-bold mb-4">Clients</h1>

      <a href="/clients/new" className="text-primary underline">+ Nouveau client</a>

      <ul className="mt-4 space-y-2">
        {data.content.map((c) => (
          <li key={c.id} className="p-3 border rounded bg-white">
            <a href={`/clients/${c.id}`} className="font-medium">
              {c.name}
            </a>
          </li>
        ))}
      </ul>
    </div>
  );
}
EOF

# 12. Nouveau client
cat > app/clients/new/page.tsx << 'EOF'
"use client";

import { api } from "@/lib/api";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function NewClientPage() {
  const router = useRouter();
  const [form, setForm] = useState({ name: "", email: "", phone: "" });

  async function submit() {
    await api("/clients", {
      method: "POST",
      body: JSON.stringify(form)
    });
    router.push("/clients");
  }

  return (
    <div>
      <h1 className="text-xl font-bold mb-4">Nouveau client</h1>

      <input
        className="input"
        placeholder="Nom"
        value={form.name}
        onChange={(e) => setForm({ ...form, name: e.target.value })}
      />

      <input
        className="input"
        placeholder="Email"
        value={form.email}
        onChange={(e) => setForm({ ...form, email: e.target.value })}
      />

      <input
        className="input"
        placeholder="Téléphone"
        value={form.phone}
        onChange={(e) => setForm({ ...form, phone: e.target.value })}
      />

      <button className="btn mt-4" onClick={submit}>
        Créer
      </button>
    </div>
  );
}
EOF

echo "✅ HeatOps Frontend est prêt !"
echo "👉 Lance : cd heatops-frontend && npm run dev"
