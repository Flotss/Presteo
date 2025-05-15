<template>
  <div>
    <!-- Navigation Menu -->
    <div
      :class="[
        'fixed md:static z-50 top-0 left-0 h-full bg-white shadow-md  transition-transform duration-300 ease-in-out',
        isOpen ? 'translate-x-0' : '-translate-x-full md:translate-x-0',
        'w-64',
      ]"
    >
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
              @click="handleSectionClick('profile')"
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
              @click="handleSectionClick('services')"
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
              @click="handleSectionClick('bookings')"
            >
              <font-awesome-icon
                :icon="['fas', 'calendar-check']"
                class="mr-2"
              />
              My Bookings
            </button>
          </li>
        </ul>
      </nav>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  activeSection: string;
  isProviderOrAdmin: boolean;
  isOpen: boolean;
}>();

const emit = defineEmits<{
  (e: "update:activeSection", section: string): void;
  (e: "update:isOpen", isOpen: boolean): void;
}>();

const handleSectionClick = (section: string) => {
  emit("update:activeSection", section);
  emit("update:isOpen", false);
};
</script>
