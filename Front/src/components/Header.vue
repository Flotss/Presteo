<template>
  <header
    id="header"
    class="w-full bg-white/95 backdrop-blur-sm z-50"
  >
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
              v-for="route in routes"
              :key="route.name"
              class="text-gray-600 hover:text-blue-600 transition duration-150"
            >
              <NuxtLink :to="route.path">{{ route.name }}</NuxtLink>
            </span>
          </nav>
        </div>
        <div class="hidden md:flex items-center space-x-4 text-nowrap">
          <NuxtLink
            v-if="!isLoginPage"
            to="/login" 
            class="hidden md:block text-gray-600 hover:text-blue-600 transition duration-150"
          >
            Login
          </NuxtLink>
          <NuxtLink
            v-if="!isSignUpPage"
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
      <Transition
        name="menu"
        enter-active-class="animate-wrapIn"
        leave-active-class="animate-wrapOut"
      >
        <div
          v-if="isMenuOpen"
          class="md:hidden mt-4 space-y-2 overflow-hidden hamburger-menu pb-2"
        >
          <span
            v-for="(route, index) in routes"
            :key="route.name"
            class="block text-gray-600 text-center hover:text-blue-600 transition duration-150 openAnimation"
            :class="{ 'text-blue-600': route.isActualPage }"
            :style="{ animationDelay: `${index * 0.1}s` }"
          >
            <NuxtLink :to="route.path">{{ route.name }}</NuxtLink>
            <div class="w-full h-0.5 bg-gray-200 my-2"></div>
          </span>
          <div class="flex justify-evenly space-x-4">
            <button
              v-if="!isLoginPage"
              class="bg-gray-200 text-gray-600 px-6 py-2 rounded-full hover:text-blue-600 w-full text-center transition duration-150 openAnimation opacity-0"
              :style="{ animationDelay: `${routes.length * 0.1}s`, animationFillMode: 'forwards' }"
            >
              <NuxtLink to="/login">Login</NuxtLink>
            </button>
            <button
              v-if="!isSignUpPage"
              class="bg-blue-600 text-white px-6 py-2 rounded-full hover:bg-blue-700 w-full text-center transition duration-150 openAnimation opacity-0"
              :style="{ animationDelay: `${routes.length * 0.1 + 0.1}s`, animationFillMode: 'forwards' }"
            >
              <NuxtLink to="/signup">Sign Up</NuxtLink>
            </button>
          </div>
        </div>
      </Transition>
    </div>
  </header>
</template>

<script setup lang="ts">
const isMenuOpen = ref(false);
const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value;
};

const route = useRoute();
const routePath = route.path;
console.log(routePath, route)
const routes = [
  { name: "Service", path: "/service", isActualPage: computed(() => routePath === "/service") },
  { name: "How it Works", path: "/how-it-works", isActualPage: computed(() => routePath === "/how-it-works") },
  { name: "Providers", path: "/providers", isActualPage: computed(() => routePath === "/providers") },
  { name: "About Us", path: "/about-us", isActualPage: computed(() => routePath === "/about-us") },
];

const isSignUpPage = computed(() => {
  console.log("Current route:", routePath);
  console.log("Is signup page:", routePath == "/signup");
  return routePath == "/signup";
});

const isLoginPage = computed(() => {
  return routePath === "/login";
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
