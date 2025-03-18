<template>
  <footer class="bg-gray-800 text-white py-8">
    <div class="container mx-auto px-4">
      <div class="flex flex-wrap justify-between">
        <div class="w-full md:w-1/3 mb-6 md:mb-0">
          <h2 class="text-2xl font-bold mb-2">{{ env.appName }}</h2>
          <p class="text-gray-400 mb-4">
            Plateforme de gestion de services innovante pour les entreprises et
            les particuliers.
          </p>
          <p class="text-sm text-gray-500">
            Version {{ env.appVersion }} -
            {{ env.appEnv === "development" ? "Développement" : "Production" }}
          </p>
        </div>

        <!-- Liens rapides -->
        <div class="w-full md:w-1/4 mb-6 md:mb-0">
          <h3 class="text-lg font-semibold mb-3 border-b border-gray-700 pb-1">
            Liens rapides
          </h3>
          <ul>
            <li class="mb-2">
              <NuxtLink
                to="/"
                class="text-gray-400 hover:text-white transition duration-150"
              >
                Accueil
              </NuxtLink>
            </li>
            <li class="mb-2">
              <NuxtLink
                to="/services"
                class="text-gray-400 hover:text-white transition duration-150"
              >
                Services
              </NuxtLink>
            </li>
            <li class="mb-2">
              <NuxtLink
                to="/a-propos"
                class="text-gray-400 hover:text-white transition duration-150"
              >
                À propos
              </NuxtLink>
            </li>
            <li class="mb-2">
              <NuxtLink
                to="/contact"
                class="text-gray-400 hover:text-white transition duration-150"
              >
                Contact
              </NuxtLink>
            </li>
          </ul>
        </div>

        <!-- Contact -->
        <div class="w-full md:w-1/4">
          <h3 class="text-lg font-semibold mb-3 border-b border-gray-700 pb-1">
            Contact
          </h3>
          <ul>
            <li class="mb-2 flex items-center">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-5 w-5 mr-2 text-gray-400"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"
                />
              </svg>
              <a
                href="mailto:contact@presteo.com"
                class="text-gray-400 hover:text-white transition duration-150"
              >
                contact@presteo.com
              </a>
            </li>
            <li class="mb-2 flex items-center">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-5 w-5 mr-2 text-gray-400"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"
                />
              </svg>
              <a
                href="tel:+33123456789"
                class="text-gray-400 hover:text-white transition duration-150"
              >
                +33 1 23 45 67 89
              </a>
            </li>
            <li class="flex items-center">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-5 w-5 mr-2 text-gray-400"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"
                />
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"
                />
              </svg>
              <span class="text-gray-400"> 10 Rue de Paris, 75001 Paris </span>
            </li>
          </ul>
        </div>
      </div>

      <!-- Réseaux sociaux -->
      <div class="mt-8 pt-6 border-t border-gray-700">
        <div class="flex justify-between items-center flex-col md:flex-row">
          <div class="text-sm text-gray-400 mb-4 md:mb-0">
            &copy; {{ currentYear }} {{ env.appName }}. Tous droits réservés.
          </div>
          <div class="flex space-x-4">
            <!-- Icônes de réseaux sociaux -->
            <a
              href="#"
              class="text-gray-400 hover:text-white transition duration-150"
              aria-label="Facebook"
            >
              <svg class="h-5 w-5" fill="currentColor" viewBox="0 0 24 24">
                <path
                  d="M22 12c0-5.523-4.477-10-10-10S2 6.477 2 12c0 4.991 3.657 9.128 8.438 9.878v-6.987h-2.54V12h2.54V9.797c0-2.506 1.492-3.89 3.777-3.89 1.094 0 2.238.195 2.238.195v2.46h-1.26c-1.243 0-1.63.771-1.63 1.562V12h2.773l-.443 2.89h-2.33v6.988C18.343 21.128 22 16.991 22 12z"
                />
              </svg>
            </a>
            <a
              href="#"
              class="text-gray-400 hover:text-white transition duration-150"
              aria-label="Twitter"
            >
              <svg class="h-5 w-5" fill="currentColor" viewBox="0 0 24 24">
                <path
                  d="M8.29 20.251c7.547 0 11.675-6.253 11.675-11.675 0-.178 0-.355-.012-.53A8.348 8.348 0 0022 5.92a8.19 8.19 0 01-2.357.646 4.118 4.118 0 001.804-2.27 8.224 8.224 0 01-2.605.996 4.107 4.107 0 00-6.993 3.743 11.65 11.65 0 01-8.457-4.287 4.106 4.106 0 001.27 5.477A4.072 4.072 0 012.8 9.713v.052a4.105 4.105 0 003.292 4.022 4.095 4.095 0 01-1.853.07 4.108 4.108 0 003.834 2.85A8.233 8.233 0 012 18.407a11.616 11.616 0 006.29 1.84"
                />
              </svg>
            </a>
            <a
              href="#"
              class="text-gray-400 hover:text-white transition duration-150"
              aria-label="LinkedIn"
            >
              <svg class="h-5 w-5" fill="currentColor" viewBox="0 0 24 24">
                <path
                  d="M19 0h-14c-2.761 0-5 2.239-5 5v14c0 2.761 2.239 5 5 5h14c2.762 0 5-2.239 5-5v-14c0-2.761-2.238-5-5-5zm-11 19h-3v-11h3v11zm-1.5-12.268c-.966 0-1.75-.79-1.75-1.764s.784-1.764 1.75-1.764 1.75.79 1.75 1.764-.783 1.764-1.75 1.764zm13.5 12.268h-3v-5.604c0-3.368-4-3.113-4 0v5.604h-3v-11h3v1.765c1.396-2.586 7-2.777 7 2.476v6.759z"
                />
              </svg>
            </a>
            <a
              href="#"
              class="text-gray-400 hover:text-white transition duration-150"
              aria-label="GitHub"
            >
              <svg class="h-5 w-5" fill="currentColor" viewBox="0 0 24 24">
                <path
                  d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z"
                />
              </svg>
            </a>
          </div>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { computed } from "vue";

// Récupération des variables d'environnement
const env = useEnvironment();

// Année courante pour le copyright
const currentYear = computed(() => new Date().getFullYear());
</script>

<style scoped>
footer a {
  text-decoration: none;
}
</style>
