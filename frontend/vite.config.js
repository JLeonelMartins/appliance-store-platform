import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/product': {
        target: 'http://localhost:8088',
        changeOrigin: true
      },
      '/cart': {
        target: 'http://localhost:8088',
        changeOrigin: true
      },
      '/sales': {
        target: 'http://localhost:8088',
        changeOrigin: true
      }
    }
  }
})