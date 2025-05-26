// https://nuxt.com/docs/api/configuration/nuxt-config
import { config } from "dotenv"
import { resolve } from "path"

const path = resolve(__dirname, "../../.env")

config({ path })

export default defineNuxtConfig({
  compatibilityDate: '2025-05-15',
  devtools: { enabled: true },
  srcDir: ".",
  dir: {
    app: "app"
  },
  css: [

  ],
  runtimeConfig: {
    nitro: {
      envPrefix: "ANTBIZ_"
    },
    public: {
      apiBaseUrl: process.env.ANTBIZ_HOST_NAME || "http://localhost:8080"
    }
  },
  modules: [
    "@pinia/nuxt",
    "pinia-plugin-persistedstate/nuxt",
    "@nuxtjs/tailwindcss",
    "@vueuse/nuxt",
    "@nuxtjs/i18n",
    "@nuxt/eslint",
    "dayjs-nuxt",
    "@vesp/nuxt-fontawesome",
  ],
})