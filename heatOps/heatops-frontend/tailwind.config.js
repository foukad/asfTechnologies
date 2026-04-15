/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    fontFamily: {
      sans: ["Inter", "ui-sans-serif", "system-ui"],
    },
    extend: {
      colors: {
        primary: {
          DEFAULT: "#3B82F6",
          hover: "#2563EB",
          light: "#EFF6FF",
        },
        surface: {
          DEFAULT: "#FFFFFF",
          hover: "#F1F5F9",
        },
        text: {
          DEFAULT: "#1E293B",
          secondary: "#64748B",
          disabled: "#94A3B8",
        },
        border: "#E5E7EB",
        background: "#F9FAFB",
        success: "#22C55E",
        warning: "#F59E0B",
        danger: "#EF4444",
        info: "#3B82F6",
      },
      borderRadius: {
        card: "18px",
        button: "14px",
        input: "14px",
        pill: "12px",
        modal: "20px",
      },
      boxShadow: {
        card: "0 1px 3px rgba(0,0,0,0.06)",
        soft: "0 2px 6px rgba(0,0,0,0.08)",
      },
      fontSize: {
        base: "16px",
        nav: "17px",
        title: "22px",
        section: "18px",
      },
    },
  },
  plugins: [],
};
