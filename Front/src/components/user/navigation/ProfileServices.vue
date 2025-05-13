<template>
  <div class="space-y-6">
    <div class="flex justify-between items-center">
      <h2 class="text-2xl font-semibold text-gray-800">My Services</h2>
      <button
        class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors duration-200"
        @click="$emit('create-service')"
      >
        <font-awesome-icon :icon="['fas', 'plus']" class="mr-2" />
        Add New Service
      </button>
    </div>

    <div
      v-if="loading"
      class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"
    >
      <div
        v-for="i in 3"
        :key="i"
        class="bg-white rounded-lg shadow-md p-6 animate-pulse"
      >
        <div class="h-4 bg-gray-200 rounded w-3/4 mb-4" />
        <div class="h-4 bg-gray-200 rounded w-1/2 mb-4" />
        <div class="h-4 bg-gray-200 rounded w-2/3" />
      </div>
    </div>

    <div
      v-else-if="services.length === 0"
      class="text-center py-12 bg-white rounded-lg shadow-md"
    >
      <font-awesome-icon
        :icon="['fas', 'tools']"
        class="h-12 w-12 text-gray-400 mx-auto mb-4"
      />
      <p class="text-gray-600">You don't have any services yet</p>
    </div>

    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div
        v-for="service in services"
        :key="service.id"
        class="bg-white rounded-lg shadow-md p-6"
      >
        <div class="flex justify-between items-start mb-4">
          <h3 class="text-lg font-medium text-gray-900">{{ service.title }}</h3>
          <span
            class="px-3 py-1 rounded-full text-sm font-medium"
            :class="
              service.active
                ? 'bg-green-100 text-green-800'
                : 'bg-red-100 text-red-800'
            "
          >
            {{ service.active ? "Active" : "Inactive" }}
          </span>
        </div>
        <p class="text-gray-600 mb-4">{{ service.description }}</p>
        <div class="space-y-2">
          <div class="flex items-center text-gray-600">
            <font-awesome-icon
              :icon="['fas', 'clock']"
              class="h-5 w-5 mr-2 text-blue-600"
            />
            {{ service.durationHours }} hours
          </div>
          <div class="flex items-center text-gray-600">
            <font-awesome-icon
              :icon="['fas', 'euro-sign']"
              class="h-5 w-5 mr-2 text-blue-600"
            />
            {{ service.price }}€/hour
          </div>
          <div class="flex items-center text-gray-600">
            <font-awesome-icon
              :icon="['fas', 'map-marker-alt']"
              class="h-5 w-5 mr-2 text-blue-600"
            />
            {{ service.city }}
          </div>
        </div>
        <div class="mt-4 flex justify-end space-x-2">
          <button
            class="px-3 py-1 text-sm text-blue-600 hover:bg-blue-50 rounded-md transition-colors duration-200"
            @click="$emit('edit-service', service)"
          >
            Edit
          </button>
          <button
            class="px-3 py-1 text-sm text-red-600 hover:bg-red-50 rounded-md transition-colors duration-200"
            @click="$emit('delete-service', service)"
          >
            Delete
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Service } from "~/model/service";

defineProps<{
  services: Service[];
  loading: boolean;
}>();

defineEmits<{
  (e: "create-service"): void;
  (e: "edit-service" | "delete-service", service: Service): void;
}>();
</script>
