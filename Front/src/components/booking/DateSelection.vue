<template>
  <div class="mb-8">
    <label for="date" class="block text-lg font-medium text-gray-700 mb-3 flex items-center">
      <font-awesome-icon :icon="['fas', 'calendar-day']" class="h-6 w-6 mr-3 text-blue-600" />
      Select Date
    </label>
    <input
      id="date"
      :value="modelValue"
      type="date"
      :min="minDate"
      class="w-full md:w-80 px-4 py-3 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500 text-lg"
      @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
    />
  </div>
</template>

<script setup lang="ts">
defineProps<{
  modelValue: string;
  minDate: string;
}>();

defineEmits<{
  (e: 'update:modelValue', value: string): void;
}>();
</script> 