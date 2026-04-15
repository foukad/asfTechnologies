export async function api(path: string, options: RequestInit = {}) {
  // Lire les cookies (middleware + useAuth les utilisent)
  const token = getCookie("token");
  const tenantId = getCookie("tenantId");

  if (!token || !tenantId) {
    throw new Error("Utilisateur non authentifié");
  }

  const headers = {
    "Content-Type": "application/json",
    "Authorization": `Bearer ${token}`,
    "X-Tenant-ID": tenantId,
    ...options.headers
  };

  const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}${path}`, {
    ...options,
    headers
  });

  if (!res.ok) {
    const text = await res.text();
    throw new Error(`API ERROR ${res.status}: ${text}`);
  }

  // Si la réponse est vide → éviter erreur JSON
  const contentType = res.headers.get("content-type");
  if (contentType && contentType.includes("application/json")) {
    return res.json();
  }

  return res.text();
}

// Petite fonction utilitaire pour lire un cookie
function getCookie(name: string): string | null {
  if (typeof document === "undefined") return null;
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop()!.split(";").shift()!;
  return null;
}
