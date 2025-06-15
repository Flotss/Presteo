<template>
  <div class="flex items-center">
    <template v-if="rating && count > 0">
      <div class="flex items-center">
        <font-awesome-icon
          v-for="star in 5"
          :key="star"
          :icon="[star <= Math.round(rating) ? 'fas' : 'far', 'star']"
          :class="[
            'h-4 w-4',
            star <= Math.round(rating) ? 'text-yellow-400' : 'text-gray-300',
          ]"
        />
      </div>
      <span v-if="showCount" class="ml-2 text-sm text-gray-600">
        ({{ count }})
      </span>
    </template>
    <template v-else>
      <span class="text-sm text-gray-500">No rating yet</span>
    </template>
  </div>
</template>

<script setup lang="ts">
defineProps({
  rating: {
    type: Number,
    required: false,
    default: null,
  },
  count: {
    type: Number,
    default: 0,
  },
  showCount: {
    type: Boolean,
    default: true,
  },
});
</script>
