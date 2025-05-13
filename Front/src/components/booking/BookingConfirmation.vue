<template>
  <div class="fixed inset-0 bg-white z-50 flex items-center justify-center">
    <div class="max-w-2xl w-full mx-4">
      <div class="text-center mb-8">
        <div class="relative w-32 h-32 mx-auto mb-8">
          <div
            class="absolute inset-0 border-4 border-green-200 rounded-full animate-pulse"
          />
          <div
            class="absolute inset-4 border-4 border-green-400 rounded-full animate-spin"
            style="animation-duration: 2s"
          />
          <div class="absolute inset-0 flex items-center justify-center">
            <font-awesome-icon
              :icon="['fas', 'check-circle']"
              class="h-16 w-16 text-green-500 text-9xl"
            />
          </div>
        </div>
        <h2 class="text-3xl font-bold text-gray-900 mb-4">
          Booking Confirmed!
        </h2>
        <p class="text-lg text-gray-600">Thank you for choosing our service.</p>
      </div>

      <div class="bg-white rounded-lg shadow-lg p-6 mb-8">
        <div class="space-y-6">
          <div class="flex items-center">
            <font-awesome-icon
              :icon="['fas', 'calendar-check']"
              class="h-6 w-6 text-blue-600 mr-3"
            />
            <div>
              <h3 class="text-lg font-medium text-gray-900">Booking Details</h3>
              <p class="text-gray-600">{{ formatDate(booking.bookingDate as unknown as string) }}</p>
            </div>
          </div>

          <div class="flex items-center">
            <font-awesome-icon
              :icon="['fas', 'map-marker-alt']"
              class="h-6 w-6 text-blue-600 mr-3"
            />
            <div>
              <h3 class="text-lg font-medium text-gray-900">Location</h3>
              <p class="text-gray-600">{{ booking.address }}</p>
            </div>
          </div>

          <div class="flex items-center">
            <font-awesome-icon
              :icon="['fas', 'user-tie']"
              class="h-6 w-6 text-blue-600 mr-3"
            />
            <div>
              <h3 class="text-lg font-medium text-gray-900">
                Service Provider
              </h3>
              <p class="text-gray-600">
                {{ booking.service.provider.firstName }}
                {{ booking.service.provider.lastName }}
              </p>
            </div>
          </div>

          <div class="flex items-center">
            <font-awesome-icon
              :icon="['fas', 'euro-sign']"
              class="h-6 w-6 text-blue-600 mr-3"
            />
            <div>
              <h3 class="text-lg font-medium text-gray-900">Price</h3>
              <p class="text-gray-600">{{ booking.finalPrice }}€</p>
            </div>
          </div>
        </div>
      </div>

      <div class="flex justify-center gap-4">
        <button
          class="px-6 py-3 bg-gray-100 text-gray-700 rounded-md hover:bg-gray-200 transition-colors duration-200"
          @click="$emit('close')"
        >
          Close
        </button>
        <button
          class="px-6 py-3 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors duration-200"
          @click="$emit('view-bookings')"
        >
          <font-awesome-icon :icon="['fas', 'list']" class="mr-2" />
          View my bookings
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Booking } from "~/model/booking";

defineProps<{
  booking: Booking;
}>();

defineEmits<{
  (e: "close" | "view-bookings"): void;
}>();

const formatDate = (date: string) => {
  const dateObject = new Date(date);
  return dateObject.toLocaleString("en-US", {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
    hour: "2-digit",
  });
};
</script>
