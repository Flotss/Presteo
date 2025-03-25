<template>
  <header
    id="header"
    class="w-full bg-white/95 backdrop-blur-sm z-50 border-b border-gray-100"
  >
    <div class="container mx-auto px-4">
      <div class="flex items-center justify-between h-20">
        <div class="flex items-center">
          <span class="text-2xl font-bold text-blue-600 cursor-pointer">
            Presteo
          </span>
        </div>
        <div class="hidden md:flex justify-evenly items-center w-full">
          <nav class="space-x-8 items-center">
            <span
              v-for="route in routes"
              :key="route.name"
              class="text-gray-600 hover:text-blue-600 transition duration-150"
            >
              <NuxtLink :to="route.path">{{ route.name }}</NuxtLink>
            </span>
          </nav>
        </div>
        <div class="hidden md:flex items-center space-x-4 text-nowrap">
          <button class="hidden md:block text-gray-600 hover:text-blue-600 transition duration-150">
            Login
          </button>
          <button
            class="bg-blue-600 text-white px-6 py-2 rounded-full hover:bg-blue-700 transition duration-150"
          >
            Sign Up
          </button>
        </div>

        <div class="flex items-center space-x-4">
          <button
            class="md:hidden text-gray-600 hover:text-blue-600 focus:outline-none transition duration-150"
            @click="toggleMenu"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="h-6 w-6"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M4 6h16M4 12h16m-7 6h7"
              />
            </svg>
          </button>
        </div>
      </div>
      <div v-if="isMenuOpen" class="md:hidden mt-4 space-y-2">
        <span
          v-for="route in routes"
          :key="route.name"
          class="block text-gray-600 hover:text-blue-600 transition-all duration-150"
        >
          <NuxtLink :to="route.path">{{ route.name }}</NuxtLink>
        </span>
        <button
          class="block bg-gray-200 text-gray-600 px-6 py-2 rounded-full hover:text-blue-600 w-full text-center transition duration-150"
        >
          Login
        </button>
        <button
          class="block bg-blue-600 text-white px-6 py-2 rounded-full hover:bg-blue-700 w-full text-center transition duration-150"
        >
          Sign Up
        </button>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { ref } from "vue";

const isMenuOpen = ref(false);
const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value;
};
const closeMenu = () => {
  isMenuOpen.value = false;
};

const routes = [
  { name: "Service", path: "/service" },
  { name: "How it Works", path: "/how-it-works" },
  { name: "Providers", path: "/providers" },
  { name: "About Us", path: "/about-us" },
];
</script>
