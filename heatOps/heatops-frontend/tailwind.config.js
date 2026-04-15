/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./app/**/*.{ts,tsx}",
    "./components/**/*.{ts,tsx}"
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: "#3B82F6",
          dark: "#2563EB",
          light: "#EFF6FF",
        },
        info: "#3B82F6",
        success: "#22C55E",
        warning: "#F59E0B",
        danger: "#EF4444",
        dark: "#1E293B",
        muted: "#64748B",
        surface: "#FFFFFF",
        bg: "#F9FAFB",
        border: "#E5E7EB",
        hover: "#F1F5F9",
      },
      fontFamily: {
        sans: ["Inter", "system-ui", "sans-serif"],
      },
      borderRadius: {
        xl: "12px",
        "2xl": "14px",
      },
      boxShadow: {
        card: "0 1px 3px rgba(0,0,0,0.06)",
        modal: "0 20px 48px rgba(15,23,42,0.12)",
      },
    },
  },
  plugins: [],
};
