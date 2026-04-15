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
