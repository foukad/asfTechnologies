import "./globals.css";
import NavigationBar from "@/components/navigation-bar";

export default function RootLayout({ children }) {
  return (
    <html lang="fr">
      <body>
        <div className="flex min-h-screen flex-col gap-3 p-3">
          <NavigationBar />
          <div
            className="flex min-w-0 flex-1 flex-col overflow-hidden rounded-[28px]"
            style={{
              background: "rgba(255, 255, 255, 0.88)",
              border: "1px solid var(--border)",
              boxShadow: "var(--shadow-soft)",
            }}
          >
            <main className="flex-1 overflow-y-auto" style={{ background: "transparent" }}>
              {children}
            </main>
          </div>
        </div>
      </body>
    </html>
  );
}
