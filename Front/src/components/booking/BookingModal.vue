<template>
  <div v-if="modelValue" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4">
    <div class="bg-white rounded-lg p-6 max-w-md w-full">
      <h3 class="text-lg font-medium mb-4 flex items-center">
        <font-awesome-icon :icon="['fas', 'calendar-check']" class="h-5 w-5 mr-2 text-blue-600" />
        Book Appointment
      </h3>
      <form @submit.prevent="$emit('submit')">
        <div class="mb-4">
          <FormAddressInput
            id="address"
            :model-value="address"
            @update:model-value="$emit('update:address', $event)"
            label="Address"
            :submit="loading"
            placeholder="Enter your address"
          />
          <button
            type="button"
            @click="$emit('use-user-address')"
            class="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-blue-100 text-blue-800 hover:bg-blue-200 transition-colors duration-200"
          >
            <font-awesome-icon :icon="['fas', 'house-user']" class="h-4 w-4 mr-1" />
            Use My Address
          </button>
        </div>
        <div class="mb-4">
          <label class="block text-sm font-medium text-gray-700 mb-2 flex items-center">
            <font-awesome-icon :icon="['fas', 'info-circle']" class="h-5 w-5 mr-2 text-blue-600" />
            Additional Information
          </label>
          <textarea
            :value="additionalInfo"
            rows="3"
            class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
            @input="$emit('update:additionalInfo', ($event.target as HTMLTextAreaElement).value)"
          />
        </div>
        <div class="flex justify-end gap-4">
          <button
            type="button"
            class="px-4 py-2 text-gray-700 bg-gray-100 rounded-md hover:bg-gray-200"
            @click="$emit('update:modelValue', false)"
          >
            Cancel
          </button>
          <button
            type="submit"
            :disabled="loading"
            :class="[
              'px-4 py-2 text-white rounded-md transition-colors duration-200',
              created
                ? 'bg-green-600 hover:bg-green-700'
                : 'bg-blue-600 hover:bg-blue-700 disabled:bg-blue-300'
            ]"
          >
            <span v-if="loading" class="flex items-center">
              <font-awesome-icon :icon="['fas', 'circle-notch']" spin class="mr-2" />
              Booking...
            </span>
            <span v-else-if="created" class="flex items-center">
              <font-awesome-icon :icon="['fas', 'check']" class="mr-2 text-black" />
              Booked Successfully
            </span>
            <span v-else class="flex items-center">
              <font-awesome-icon :icon="['fas', 'calendar-plus']" class="mr-2" />
              Book Now
            </span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
interface Address {
  content: string;
  isValid: boolean;
}

defineProps<{
  modelValue: boolean;
  loading: boolean;
  address: Address;
  additionalInfo: string;
  created?: boolean;
}>();

defineEmits<{
  (e: 'update:modelValue', value: boolean): void;
  (e: 'update:address', value: Address): void;
  (e: 'update:additionalInfo', value: string): void;
  (e: 'use-user-address'): void;
  (e: 'submit'): void;
}>();
</script> 