import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
    proxy: {
      "/api": {
        target: "http://138.2.144.234:8765",
        changeOrigin: true,
        secure: false,
      },
      "/oauth2": {
        target: "http://138.2.144.234:8765",
        changeOrigin: true,
        secure: false,
      },
    },
  },
});
