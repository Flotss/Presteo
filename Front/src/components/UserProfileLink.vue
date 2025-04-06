<template>
  <div>
    <div
      v-if="authStore.isLoggedIn && authStore.user"
      class="relative flex items-center justify-center space-x-4 rounded-full bg-blue-50 hover:bg-blue-100 transition pr-3"
      @mouseover="showMenu = true"
      @mouseleave="showMenu = false"
    >
      <NuxtLink to="/profile" class="flex items-center">
        <div class="h-10 w-10">
          <img
            :src="'https://api.dicebear.com/9.x/dylan/svg?seed=' + authStore.user.id"
            alt="Avatar"
            class="h-10 w-10 rounded-full border border-gray-200"
            :class="{ hidden: !imageLoaded }"
            @load="imageLoaded = true"
          />
          <div
            v-if="!imageLoaded"
            class="rounded-full bg-gray-200 animate-pulse border"
          ></div>
        </div>
        <div class="flex justify-center items-center h-10">
          <span class="font-medium text-gray-800">
            {{ authStore.user.firstName }} {{ authStore.user.lastName }}
          </span>
        </div>
      </NuxtLink>
      <div
        v-if="showMenu && shouldHaveMenu"
        class="absolute top-full right-0 bg-white border rounded shadow-lg w-40"
      >
        <NuxtLink
          to="/profile"
          class="block px-4 py-2 text-gray-800 hover:bg-gray-100"
        >
            <font-awesome-icon :icon="['fas', 'user']" class="mr-2" />
          Profile
        </NuxtLink>
        <button
          @click="handleLogout"
          class="block w-full text-left px-4 py-2 text-gray-800 hover:bg-gray-100"
        >
            <font-awesome-icon :icon="['fas', 'right-from-bracket']" class="mr-2" />
          Logout
        </button>
      </div>
    </div>
    <div
      v-if="authStore.isLoggedIn && !authStore.user"
      class="flex items-center space-x-2 rounded-full bg-blue-50 hover:text-blue-600 transition pr-3"
    >
      <div class="h-10 w-10 rounded-full border bg-gray-200"></div>
      <div class="h-4 w-24 bg-gray-300 rounded"></div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  shouldHaveMenu: {
    type: Boolean,
    default: false,
  },
});

import { useAuthStore } from "~/stores/auth";

const authStore = useAuthStore();
const imageLoaded = ref(false);
const showMenu = ref(false);

const handleLogout = () => {
  try {
    authStore.logout();
    showMenu.value = false;
  } catch (error) {
    console.error("Logout failed:", error);
  }
};
</script>
