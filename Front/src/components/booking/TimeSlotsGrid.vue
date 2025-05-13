<template>
  <div class="grid grid-cols-4 md:grid-cols-6 lg:grid-cols-8 xl:grid-cols-12 gap-4 mb-8">
    <template v-if="loading">
      <div v-for="i in 12" :key="i" class="p-4 rounded-lg bg-gray-100 animate-pulse">
        <div class="h-5 w-16 bg-gray-200 rounded mx-auto" />
      </div>
    </template>
    <button
      v-for="slot in slots"
      v-else
      :key="slot.time"
      :disabled="!slot.available"
      class="p-4 rounded-lg text-center transition-colors duration-200"
      :class="{
        'bg-green-100 hover:bg-green-200 text-green-800': slot.available,
        'bg-gray-100 text-gray-400 cursor-not-allowed': !slot.available,
        'ring-2 ring-blue-500': selectedSlot?.time === slot.time,
      }"
      @click="$emit('select', slot)"
    >
      <div class="text-base font-medium mb-1 flex items-center">
        <font-awesome-icon :icon="['fas', 'clock']" class="h-4 w-4 mr-1" />
        {{ formatTime(slot.time) }}
      </div>
      <div v-if="slot.available" class="text-xs text-green-700 flex items-center">
        <font-awesome-icon :icon="['fas', 'check']" class="h-3 w-3 mr-1" />
        Available
      </div>
      <div v-else class="text-xs text-gray-500 flex items-center">
        <font-awesome-icon :icon="['fas', 'times']" class="h-3 w-3 mr-1" />
        Not Available
      </div>
    </button>
  </div>
</template>

<script setup lang="ts">
interface TimeSlot {
  available: boolean;
  time: string;
}

defineProps<{
  slots: TimeSlot[];
  loading: boolean;
  selectedSlot: TimeSlot | null;
}>();

defineEmits<{
  (e: 'select', slot: TimeSlot): void;
}>();

const formatTime = (timeString: string) => {
  const date = new Date(timeString);
  return date.toLocaleTimeString("fr-FR", {
    hour: "2-digit",
    minute: "2-digit",
  });
};
</script> 