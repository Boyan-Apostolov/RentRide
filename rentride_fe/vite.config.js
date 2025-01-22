import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';
import fs from 'fs';

export default defineConfig(({ mode }) => {
  // Load environment variables based on the mode (development, staging, production)
  const env = loadEnv(mode, process.cwd());

  return {
    base: "/",
    plugins: [react()],
    preview: {
      port: 5173,
      strictPort: true,
    },
    server: {
      https: {
        key: fs.readFileSync('./localhost.key'), // Path to the private key
        cert: fs.readFileSync('./localhost.crt'), // Path to the certificate
      },
      port: 5173,
      strictPort: true,
      host: true,
      proxy: {
        '/api': {
          target: env.VITE_BACKEND_URL, // Dynamic backend URL based on profile
          changeOrigin: true,
          secure: false, // Bypass SSL certificate validation
        },
      },
      origin: "https://0.0.0.0:5173",
    },
  };
});