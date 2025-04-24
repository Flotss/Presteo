<template>
  <div class="p-4 border border-gray-200 rounded-lg shadow-sm bg-gray-50">
    <h2 class="text-lg font-semibold text-gray-800">{{ name }}</h2>
    <p class="text-sm text-blue-600 font-medium">{{ field }}</p>
    <p class="text-gray-500 mt-2">{{ description }}</p>
  </div>
</template>

<script setup>
defineProps({
  name: String,
  field: String,
  description: String,
});
</script>

<style scoped>
/* No additional styles needed as TailwindCSS handles the design */
</style>