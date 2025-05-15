<template>
  <footer class="py-8 border-gray-100 border-t shadow-2xl shadow-gray-900">
    <div class="container mx-auto px-4">
      <div class="flex flex-wrap justify-between">
        <div class="w-full md:w-1/3 mb-6 md:mb-0">
          <h2 class="text-2xl font-bold mb-2">{{ env.appName }}</h2>
          <p class="text-gray-400 mb-4">
            Innovative service management platform for businesses and
            individuals.
          </p>
          <p class="text-sm text-gray-500">
            Version {{ env.appVersion }} -
            {{ env.appEnv === "development" ? "Development" : "Production" }}
          </p>
        </div>

        <!-- Quick Links -->
        <div class="w-full md:w-1/4 mb-6 md:mb-0">
          <h3 class="text-lg font-semibold mb-3 border-b border-gray-700 pb-1">
            Quick Links
          </h3>
          <ul>
            <li v-for="route in routes" :key="route.name" class="mb-2">
              <NuxtLink
                :to="route.path"
                class="text-gray-600 hover:text-blue-600 transition duration-150"
              >
                {{ route.name }}
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
                class="h-5 w-5 mr-2 text-gray-600"
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
                class="text-gray-600 hover:text-blue-600 transition duration-150"
              >
                contact@presteo.com
              </a>
            </li>
            <li class="mb-2 flex items-center">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-5 w-5 mr-2 text-gray-600"
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
                class="text-gray-600 hover:text-blue-600 transition duration-150"
              >
                +33 1 23 45 67 89
              </a>
            </li>
            <li class="flex items-center">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-5 w-5 mr-2 text-gray-600"
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
              <span class="text-gray-600"> 10 Rue de Paris, 75001 Paris </span>
            </li>
          </ul>
        </div>
      </div>

      <!-- Social Media -->
      <div class="mt-8 pt-6 border-t border-gray-700">
        <div class="flex justify-between items-center flex-col md:flex-row">
          <div class="text-sm text-gray-600 mb-4 md:mb-0">
            &copy; {{ currentYear }} {{ env.appName }}. All rights reserved.
          </div>
          <div class="flex space-x-4">
            <!-- Social Media Icons -->
            <template v-for="icon in socialMediaIcons" :key="icon.label">
              <NuxtLink
                :to="icon.link"
                target="_blank"
                class="text-gray-600 hover:text-blue-600 transition duration-150"
                :aria-label="icon.label"
              >
                <font-awesome-icon class="text-xl" :icon="icon.icon" />
              </NuxtLink>
            </template>
          </div>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
defineOptions({
  name: "AppFooter",
});

// Retrieve environment variables
const env = useEnvironment();

// Current year for copyright
const currentYear = computed(() => new Date().getFullYear());
const routes = [
  { name: "Home", path: "/" },
  { name: "Service", path: "/service" },
  { name: "How it Works", path: "/how-it-works" },
  { name: "Providers", path: "/providers" },
  { name: "About Us", path: "/about-us" },
];

const socialMediaIcons = [
  {
    icon: ["fab", "facebook"],
    link: "https://www.facebook.com",
    label: "Facebook",
  },
  {
    icon: ["fab", "twitter"],
    link: "https://www.twitter.com",
    label: "Twitter",
  },
  {
    icon: ["fab", "linkedin"],
    link: "https://www.linkedin.com",
    label: "LinkedIn",
  },
  {
    icon: ["fab", "github"],
    link: "https://www.github.com",
    label: "GitHub",
  },
];
</script>
