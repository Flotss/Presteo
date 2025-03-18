<template>
  <header class="bg-gray-800 text-white p-4">
    <nav class="container mx-auto flex justify-between items-center">
      <div class="flex items-center">
        <NuxtIcon name="mdi-home" class="mr-2" />
        <span class="font-bold">Prestéo</span>
      </div>
      <ul class="flex space-x-4">
        <li v-for="link in links" :key="link.to">
          <NuxtLink :to="link.to">{{ link.text }}</NuxtLink>
        </li>
      </ul>
    </nav>
  </header>
</template>

<script setup>
const links = [
  { text: "Home", to: "/" },
  { text: "Compteur", to: "/TestPage" },
];
</script>