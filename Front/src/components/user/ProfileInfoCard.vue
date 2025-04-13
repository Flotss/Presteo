<template>
  <div class="info-card bg-white rounded-lg shadow-sm mb-6 overflow-hidden">
    <div class="px-6 py-4 bg-gray-50 border-b">
      <h3 class="text-lg font-medium text-gray-800">{{ title }}</h3>
      <p class="text-sm text-gray-500">{{ subtitle }}</p>
    </div>

    <div class="divide-y">
      <slot></slot>
    </div>
  </div>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    required: true,
  },
  subtitle: {
    type: String,
    required: true,
  },
});
</script>
