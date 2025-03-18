// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  srcDir: "src/",
  compatibilityDate: "2024-11-01",
  devtools: { enabled: true },
  
  modules: [
    '@nuxtjs/tailwindcss',
    '@pinia/nuxt',
  ],
  
  runtimeConfig: {
    apiSecret: process.env.API_SECRET,
    public: {
      apiBaseUrl: process.env.NUXT_PUBLIC_API_BASE_URL || 'http://localhost:8080/api',
      appName: process.env.NUXT_PUBLIC_APP_NAME || 'Presteo',
      appVersion: process.env.NUXT_PUBLIC_APP_VERSION || '1.0.0',
      appEnv: process.env.NUXT_PUBLIC_APP_ENV || 'development',
      enableLogs: process.env.NUXT_PUBLIC_ENABLE_LOGS || 'false',
    }
  },
  
  app: {
    head: {
      title: 'Presteo',
      meta: [
        { charset: 'utf-8' },
        { name: 'viewport', content: 'width=device-width, initial-scale=1' },
        { name: 'description', content: 'Presteo - Plateforme de gestion de services' }
      ],
      link: [
        { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }
      ]
    }
  },
  
  typescript: {
    strict: true
  }
})
