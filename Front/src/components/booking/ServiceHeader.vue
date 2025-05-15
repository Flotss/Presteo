<template>
  <div class="mb-8">
    <!-- Hero Image Section -->
    <div class="relative h-64 md:h-96 rounded-t-lg overflow-hidden">
      <div
        class="absolute inset-0 bg-cover bg-center"
        :style="service.imageUrl ? { backgroundImage: `url(${service.imageUrl})` } : {}"
      >
        <div class="absolute inset-0 bg-gradient-to-b from-black/50 to-black/30" />
      </div>
    </div>

    <!-- Information Card -->
    <div class="relative -mt-8 md:-mt-16 mx-4 md:mx-8">
      <div class="bg-white rounded-lg shadow-lg p-6 md:p-8">
        <div class="flex flex-col md:flex-row justify-between items-start md:items-center gap-8">
          <!-- Service Details -->
          <div class="flex-1">
            <h1 class="text-3xl md:text-4xl font-bold text-gray-900 mb-4">
              {{ service.title }}
            </h1>
            <p class="text-lg text-gray-600 mb-6">
              {{ service.description }}
            </p>
            <div class="flex items-center gap-8 text-base">
              <div class="flex items-center text-gray-700">
                <font-awesome-icon :icon="['fas', 'clock']" class="h-6 w-6 mr-1 text-blue-600" />
                {{ service.durationHours }} hours
              </div>
              <div class="flex items-center text-gray-700">
                <font-awesome-icon :icon="['fas', 'euro-sign']" class="h-6 w-6  text-blue-600" />
                {{ service.price * service.durationHours }}€ ({{ service.price }}€/h)
              </div>
            </div>
          </div>

          <!-- Provider Information -->
          <ProviderInfo :provider="service.provider" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Service } from "~/model/service";

defineProps<{
  service: Service;
}>();
</script> 