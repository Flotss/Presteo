<template>
  <div class="mb-8">
    <div class="mb-4">
      <label class="block text-sm font-medium text-gray-700 mb-2 flex items-center">
        <font-awesome-icon :icon="['fas', 'sun']" class="h-5 w-5 mr-2 text-blue-600" />
        Morning
      </label>
      <select
        v-if="!loading"
        :value="selectedMorningSlot"
        class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
        @change="$emit('update:selectedMorningSlot', ($event.target as HTMLSelectElement).value)"
      >
        <option value="">Select a time</option>
        <option
          v-for="slot in morningSlots"
          :key="slot.time"
          :value="slot.time"
          :disabled="!slot.available"
        >
          {{ formatTime(slot.time) }}
        </option>
      </select>
      <div v-else class="w-full h-10 bg-gray-100 rounded-md animate-pulse" />
    </div>
    <div>
      <label class="block text-sm font-medium text-gray-700 mb-2 flex items-center">
        <font-awesome-icon :icon="['fas', 'moon']" class="h-5 w-5 mr-2 text-blue-600" />
        Afternoon
      </label>
      <select
        v-if="!loading"
        :value="selectedAfternoonSlot"
        class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
        @change="$emit('update:selectedAfternoonSlot', ($event.target as HTMLSelectElement).value)"
      >
        <option value="">Select a time</option>
        <option
          v-for="slot in afternoonSlots"
          :key="slot.time"
          :value="slot.time"
          :disabled="!slot.available"
        >
          {{ formatTime(slot.time) }}
        </option>
      </select>
      <div v-else class="w-full h-10 bg-gray-100 rounded-md animate-pulse" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

interface TimeSlot {
  available: boolean;
  time: string;
}

const props = defineProps<{
  slots: TimeSlot[];
  loading: boolean;
  selectedMorningSlot: string;
  selectedAfternoonSlot: string;
}>();

defineEmits<{
  (e: 'update:selectedMorningSlot', value: string): void;
  (e: 'update:selectedAfternoonSlot', value: string): void;
}>();

const morningSlots = computed(() =>
  props.slots.filter((slot) => {
    const hour = new Date(slot.time).getHours();
    return hour < 12;
  })
);

const afternoonSlots = computed(() =>
  props.slots.filter((slot) => {
    const hour = new Date(slot.time).getHours();
    return hour >= 12;
  })
);

const formatTime = (timeString: string) => {
  const date = new Date(timeString);
  return date.toLocaleTimeString("fr-FR", {
    hour: "2-digit",
    minute: "2-digit",
  });
};
</script> 