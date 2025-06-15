<template>
  <header id="header" class="w-full z-50 shadow-md bg-white">
    <div class="container mx-auto px-4">
      <div class="flex items-center justify-between h-20">
        <div class="flex items-center">
          <span class="text-2xl font-bold text-blue-600 cursor-pointer">
            <NuxtLink to="/">Presteo</NuxtLink>
          </span>
        </div>
        <div class="hidden md:flex justify-evenly items-center w-full">
          <nav class="space-x-8 items-center">
            <span
              v-for="navItem in routes"
              v-show="
                (!navItem.showWhenLoggedIn && !navItem.showWhenAdmin) ||
                (navItem.showWhenLoggedIn && authStore.isLoggedIn) ||
                (navItem.showWhenAdmin && isAdmin)
              "
              :key="navItem.name"
              class="text-gray-600 hover:text-blue-600 transition duration-150"
            >
              <NuxtLink :to="navItem.path">{{ navItem.name }}</NuxtLink>
            </span>
          </nav>
        </div>
        <div class="hidden md:flex items-center space-x-4 text-nowrap">
          <UserProfileLink :should-have-menu="true" />
          <NuxtLink
            v-if="!isLoginPage && !authStore.isLoggedIn"
            to="/login"
            class="hidden md:block text-gray-600 hover:text-blue-600 transition duration-150"
            :class="{
              'bg-blue-600 text-white px-6 py-2 rounded-full hover:text-white hover:bg-blue-700':
                isSignUpPage,
            }"
          >
            Login
          </NuxtLink>
          <NuxtLink
            v-if="!isSignUpPage && !authStore.isLoggedIn"
            to="/signup"
            class="bg-blue-600 text-white px-6 py-2 rounded-full hover:bg-blue-700 transition duration-150"
          >
            Sign Up
          </NuxtLink>
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
      <TransitionOpen :show-content="isMenuOpen">
        <div
          class="md:hidden mt-4 space-y-2 overflow-hidden hamburger-menu pb-2"
        >
          <span
            v-for="(menuItem, index) in routes"
            :key="menuItem.name"
            class="block text-gray-600 text-center hover:text-blue-600 transition duration-150 openAnimation"
            :class="{ 'text-blue-600': menuItem.isActualPage }"
            :style="{ animationDelay: `${index * 0.1}s` }"
          >
            <NuxtLink :to="menuItem.path">{{ menuItem.name }}</NuxtLink>
            <div class="w-full h-0.5 bg-gray-200 my-2" />
          </span>
          <div
            v-if="!authStore.isLoggedIn"
            class="flex justify-evenly space-x-4"
          >
            <NuxtLink
              v-if="!isLoginPage"
              to="/login"
              class="bg-gray-200 text-gray-600 px-6 py-2 rounded-full hover:text-blue-600 w-full text-center transition duration-150 openAnimation opacity-0"
              :style="{
                animationDelay: `${routes.length * 0.1}s`,
                animationFillMode: 'forwards',
              }"
            >
              Login
            </NuxtLink>
            <NuxtLink
              v-if="!isSignUpPage"
              to="/signup"
              class="bg-blue-600 text-white px-6 py-2 rounded-full hover:bg-blue-700 w-full text-center transition duration-150 openAnimation opacity-0"
              :style="{
                animationDelay: `${routes.length * 0.1 + 0.1}s`,
                animationFillMode: 'forwards',
              }"
            >
              Sign Up
            </NuxtLink>
          </div>
          <div v-else class="flex justify-between items-center space-x-40 mt-4">
            <UserProfileLink />
            <button
              class="bg-blue-600 text-white px-6 py-2 rounded-full hover:bg-blue-700 text-center transition duration-150 openAnimation opacity-0"
              :style="{
                animationDelay: `${routes.length * 0.1 + 0.1}s`,
                animationFillMode: 'forwards',
              }"
              @click="authStore.logout()"
            >
              Logout
            </button>
          </div>
        </div>
      </TransitionOpen>
    </div>
  </header>
</template>

<script setup lang="ts">
import { ref, watch } from "vue";

defineOptions({
  name: "AppHeader",
});

const isMenuOpen = ref(false);
const authStore = useAuthStore();
const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value;
};

const isAdmin = ref(false);

watch(
  () => authStore.user?.role?.name,
  (roleName) => {
    isAdmin.value = roleName === "ADMIN";
  },
  { immediate: true }
);

const route = useRoute();
const routePath = computed(() => {
  return route.path;
});

watch(routePath, () => {
  isMenuOpen.value = false;
});

const routes = [
  // Public pages
  {
    name: "Home",
    path: "/",
    isActualPage: computed(() => routePath.value === "/"),
  },
  {
    name: "Services",
    path: "/services",
    isActualPage: computed(() => routePath.value === "/services"),
  },
  {
    name: "How it Works",
    path: "/how-it-works",
    isActualPage: computed(() => routePath.value === "/how-it-works"),
  },
  {
    name: "About Us",
    path: "/about-us",
    isActualPage: computed(() => routePath.value === "/about-us"),
  },
  // Logged in user pages
  {
    name: "My Bookings",
    path: "/bookings/mybookings",
    isActualPage: computed(() => routePath.value === "/bookings/mybookings"),
    showWhenLoggedIn: true,
  },

  {
    name: "Search",
    path: "/search",
    isActualPage: computed(() => routePath.value === "/search"),
    showWhenLoggedIn: true,
  },
  // Admin pages
  {
    name: "Admin Panel",
    path: "/admin/users",
    isActualPage: computed(() => routePath.value.startsWith("/admin")),
    showWhenAdmin: true,
  },
];

const isSignUpPage = computed(() => {
  return routePath.value == "/signup";
});

const isLoginPage = computed(() => {
  return routePath.value === "/login";
});
</script>

<style scoped>
.openAnimation {
  animation: slideIn 0.3s ease-in-out;
}

.hamburger-menu {
  overflow: hidden;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}
</style>
