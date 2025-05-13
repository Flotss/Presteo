<template>
  <div class="w-full md:w-96 bg-gray-50 rounded-lg p-6 border border-gray-100">
    <h3 class="text-xl font-medium text-gray-900 mb-4 flex items-center">
      <font-awesome-icon :icon="['fas', 'user-tie']" class="h-6 w-6 mr-3 text-blue-600" />
      Provider Information
    </h3>
    <div class="space-y-4">
      <div class="flex items-center">
        <div class="flex-1">
          <p class="text-lg font-medium text-gray-900">
            {{ provider.firstName }} {{ provider.lastName }}
          </p>
          <p class="text-sm text-gray-600">{{ provider.email }}</p>
        </div>
      </div>
      <div class="flex items-center space-x-1">
        <font-awesome-icon
          v-for="i in 5"
          :key="i"
          :icon="['fas', i <= 4 ? 'star' : 'star-half-alt']"
          class="h-5 w-5 text-yellow-400"
        />
        <span class="ml-2 text-sm text-gray-600">4.5 (128 reviews)</span>
      </div>
      <div v-if="provider.providerInformation" class="mt-2">
        <p class="text-sm text-gray-600">
          <font-awesome-icon :icon="['fas', 'briefcase']" class="h-4 w-4 mr-2 text-blue-600" />
          {{ provider.providerInformation.experience }}
        </p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Provider } from "~/model/provider";

defineProps<{
  provider: Provider;
}>();
</script> 