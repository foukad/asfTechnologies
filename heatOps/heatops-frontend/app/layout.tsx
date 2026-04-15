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
