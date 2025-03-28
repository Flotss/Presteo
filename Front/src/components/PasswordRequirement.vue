<template>
  <div class="flex items-center">
    <font-awesome-icon
      :icon="isMet ? 'fa-check-circle' : 'fa-times-circle'"
      :class="{
        'text-green-500': isMet,
        'text-red-500': !isMet,
      }"
      class="mr-2 transition-all duration-300 fa-lg"
      key="isMet"
    />
    {{ text }}
  </div>
</template>

<script lang="ts" setup>
defineProps({
  isMet: {
    type: Boolean,
    required: true,
  },
  text: {
    type: String,
    required: true,
  },
});
</script>
