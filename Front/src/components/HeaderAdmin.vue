<template>
  <header id="header-admin" class="w-full z-50 shadow-md bg-gray-900">
    <div class="container mx-auto px-4">
      <div class="flex items-center justify-between h-20">
        <div class="flex items-center">
          <span class="text-2xl font-bold text-white cursor-pointer">
            <NuxtLink to="/admin/users">Presteo Admin</NuxtLink>
          </span>
        </div>
        <div class="hidden md:flex justify-evenly items-center">
          <nav class="space-x-8 items-center">
            <span
              v-for="navItem in routes"
              :key="navItem.name"
              class="hover:text-white transition duration-150"
              :class="{ 'text-white': navItem.isActualPage.value, 'text-gray-300': !navItem.isActualPage.value }"
            >
              <NuxtLink :to="navItem.path">{{ navItem.name }}</NuxtLink>
            </span>
          </nav>
        </div>
        <div class="hidden md:flex items-center space-x-4">
          <NuxtLink to="/" class="text-gray-300 hover:text-white transition duration-150">Retour au site</NuxtLink>
          <button @click="authStore.logout()" class="bg-red-600 text-white px-4 py-2 rounded-full hover:bg-red-700 transition duration-150">
            Logout
          </button>
        </div>
        <div class="flex items-center md:hidden">
          <button
            class="text-gray-300 hover:text-white focus:outline-none transition duration-150"
            @click="toggleMenu"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-7 w-7" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16m-7 6h7" />
            </svg>
          </button>
        </div>
      </div>
      <transition name="slide-fade">
        <div v-if="isMenuOpen" class="md:hidden mt-4 space-y-2 overflow-hidden hamburger-menu pb-2 bg-gray-900 rounded-lg shadow-lg">
          <span
            v-for="(menuItem, index) in routes"
            :key="menuItem.name"
            class="block text-gray-300 text-center hover:text-white transition duration-150 openAnimation"
            :class="{ 'text-white': menuItem.isActualPage.value }"
            :style="{ animationDelay: `${index * 0.1}s` }"
          >
            <NuxtLink :to="menuItem.path" @click="closeMenu">{{ menuItem.name }}</NuxtLink>
            <div class="w-full h-0.5 bg-gray-700 my-2" />
          </span>
          <div class="flex flex-col items-center space-y-2 mt-2">
            <NuxtLink to="/" class="text-gray-300 hover:text-white transition duration-150" @click="closeMenu">Retour au site</NuxtLink>
            <button @click="logoutAndClose" class="bg-red-600 text-white px-6 py-2 rounded-full hover:bg-red-700 transition duration-150 openAnimation opacity-0" :style="{ animationDelay: `${routes.length * 0.1 + 0.1}s`, animationFillMode: 'forwards' }">
              Logout
            </button>
          </div>
        </div>
      </transition>
    </div>
  </header>
</template>

<script setup lang="ts">
defineOptions({
  name: "HeaderAdmin",
});
const authStore = useAuthStore();
const route = useRoute();
const routePath = computed(() => route.path);

const isMenuOpen = ref(false);
const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value;
};
const closeMenu = () => {
  isMenuOpen.value = false;
};
const logoutAndClose = () => {
  closeMenu();
  authStore.logout();
};

watch(routePath, () => {
  isMenuOpen.value = false;
});

const routes = [
  {
    name: "Dashboard",
    path: "/admin",
    isActualPage: computed(() => routePath.value === "/admin"),
  },
  {
    name: "Users",
    path: "/admin/users",
    isActualPage: computed(() => routePath.value === "/admin/users"),
  },
  // reports
  {
    name: "Reports",
    path: "/admin/reports",
    isActualPage: computed(() => routePath.value === "/admin/reports"),
  },
];
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
.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 0.3s ease;
}
.slide-fade-enter-from,
.slide-fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
#header-admin {
  background: #1a202c;
}
</style> 