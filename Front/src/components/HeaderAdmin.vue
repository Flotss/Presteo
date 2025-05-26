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
        <div class="flex items-center space-x-4">
          <NuxtLink to="/" class="text-gray-300 hover:text-white transition duration-150">Retour au site</NuxtLink>
          <button @click="authStore.logout()" class="bg-red-600 text-white px-4 py-2 rounded-full hover:bg-red-700 transition duration-150">
            Logout
          </button>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
defineOptions({
  name: "HeaderAdmin",
});
const authStore = useAuthStore();
const route = useRoute();
const routePath = computed(() => {
  return route.path;
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
#header-admin {
  background: #1a202c;
}
</style> 