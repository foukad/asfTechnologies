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
