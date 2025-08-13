import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    outDir: '../../../target/classes/public',
    emptyOutDir: true,
    rollupOptions: {
      output: {
        entryFileNames: 'bundle.js',
        chunkFileNames: 'bundle.js',
        assetFileNames: '[name][extname]'
      }
    }
  },
  server: {
    proxy: {
      '/rest': 'http://localhost:8080'
    }
  }
})