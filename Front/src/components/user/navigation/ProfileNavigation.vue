<template>
  <div class="w-64 bg-white shadow-md">
    <div class="p-4 border-b">
      <h2 class="text-xl font-semibold text-gray-800">Profile Menu</h2>
    </div>
    <nav class="p-4">
      <ul class="space-y-2">
        <li>
          <button
            :class="[
              'w-full text-left px-4 py-2 rounded-lg transition-colors duration-200',
              activeSection === 'profile'
                ? 'bg-blue-50 text-blue-600'
                : 'text-gray-600 hover:bg-gray-50',
            ]"
            @click="$emit('update:activeSection', 'profile')"
          >
            <font-awesome-icon :icon="['fas', 'user']" class="mr-2" />
            Profile Information
          </button>
        </li>
        <li v-if="isProviderOrAdmin">
          <button
            :class="[
              'w-full text-left px-4 py-2 rounded-lg transition-colors duration-200',
              activeSection === 'services'
                ? 'bg-blue-50 text-blue-600'
                : 'text-gray-600 hover:bg-gray-50',
            ]"
            @click="$emit('update:activeSection', 'services')"
          >
            <font-awesome-icon :icon="['fas', 'tools']" class="mr-2" />
            My Services
          </button>
        </li>
        <li v-if="isProviderOrAdmin">
          <button
            :class="[
              'w-full text-left px-4 py-2 rounded-lg transition-colors duration-200',
              activeSection === 'bookings'
                ? 'bg-blue-50 text-blue-600'
                : 'text-gray-600 hover:bg-gray-50',
            ]"
            @click="$emit('update:activeSection', 'bookings')"
          >
            <font-awesome-icon :icon="['fas', 'calendar-check']" class="mr-2" />
            My Bookings
          </button>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  activeSection: string;
  isProviderOrAdmin: boolean;
}>();

defineEmits<{
  (e: "update:activeSection", section: string): void;
}>();
</script>
