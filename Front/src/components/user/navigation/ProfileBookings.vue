<template>
  <div class="space-y-6">
    <h2 class="text-2xl font-semibold text-gray-800">My Bookings</h2>

    <div v-if="loading" class="space-y-4">
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
      v-else-if="bookings.length === 0"
      class="text-center py-12 bg-white rounded-lg shadow-md"
    >
      <font-awesome-icon
        :icon="['fas', 'calendar-times']"
        class="h-12 w-12 text-gray-400 mx-auto mb-4"
      />
      <p class="text-gray-600">No bookings found</p>
    </div>

    <div v-else class="space-y-4">
      <div
        v-for="booking in bookings"
        :key="booking.id"
        class="bg-white rounded-lg shadow-md"
      >
        <div class="p-6">
          <div
            class="flex justify-between items-start"
            @click="toggleBookingDetails(booking.id)"
          >
            <div>
              <h3 class="text-lg font-medium text-gray-900">
                {{ booking.service.title }}
              </h3>
              <p class="text-sm text-gray-600">
                {{ formatDate(booking.bookingDate) }}
              </p>
            </div>
            <div class="flex items-center space-x-4">
              <select
                v-model="booking.status"
                class="rounded-md border-gray-300 text-sm focus:border-blue-500 focus:ring-blue-500"
                :class="getStatusClass(booking.status)"
                @click.stop
                @change="$emit('update-status', booking)"
              >
                <option
                  v-for="status in bookingStatuses"
                  :key="status"
                  :value="status"
                >
                  {{ status }}
                </option>
              </select>
              <div class="text-gray-400 hover:text-gray-600">
                <font-awesome-icon
                  :icon="[
                    'fas',
                    expandedBookings.includes(booking.id)
                      ? 'chevron-up'
                      : 'chevron-down',
                  ]"
                />
              </div>
            </div>
          </div>

          <!-- Expanded Booking Details -->
          <div
            v-if="expandedBookings.includes(booking.id)"
            class="mt-4 pt-4 border-t"
          >
            <div class="grid grid-cols-2 gap-4">
              <div>
                <h4 class="text-sm font-medium text-gray-500">
                  Customer Information
                </h4>
                <p class="mt-1 text-sm text-gray-900">
                  {{ booking.customer.firstName }}
                  {{ booking.customer.lastName }}
                </p>
                <p class="text-sm text-gray-600">
                  {{ booking.customer.email }}
                </p>
                <p class="text-sm text-gray-600">
                  {{ booking.customer.phoneNumber }}
                </p>
              </div>
              <div>
                <h4 class="text-sm font-medium text-gray-500">
                  Service Details
                </h4>
                <p class="mt-1 text-sm text-gray-900">
                  {{ booking.service.description }}
                </p>
                <p class="text-sm text-gray-600">
                  Duration: {{ booking.service.durationHours }} hours
                </p>
                <p class="text-sm text-gray-600">
                  Price: {{ booking.finalPrice }}€
                </p>
              </div>
            </div>
            <div class="mt-4">
              <h4 class="text-sm font-medium text-gray-500">
                Additional Information
              </h4>
              <p class="mt-1 text-sm text-gray-900">
                {{
                  booking.additionalInfo || "No additional information provided"
                }}
              </p>
            </div>
            <div class="mt-4">
              <h4 class="text-sm font-medium text-gray-500">Address</h4>
              <p class="mt-1 text-sm text-gray-900">{{ booking.address }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import type { Booking } from "~/model/booking";
import { BookingStatusType } from "~/model/booking";

defineProps<{
  bookings: Booking[];
  loading: boolean;
}>();

defineEmits<{
  (e: "update-status", booking: Booking): void;
}>();

const expandedBookings = ref<number[]>([]);
const bookingStatuses = Object.values(BookingStatusType);

const toggleBookingDetails = (bookingId: number) => {
  const index = expandedBookings.value.indexOf(bookingId);
  if (index === -1) {
    expandedBookings.value.push(bookingId);
  } else {
    expandedBookings.value.splice(index, 1);
  }
};

const formatDate = (date: Date) => {
  return new Date(date).toLocaleString("en-US", {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
};

const getStatusClass = (status: BookingStatusType) => {
  switch (status) {
    case BookingStatusType.PENDING:
      return "bg-yellow-100 text-yellow-800";
    case BookingStatusType.CONFIRMED:
      return "bg-blue-100 text-blue-800";
    case BookingStatusType.IN_PROGRESS:
      return "bg-purple-100 text-purple-800";
    case BookingStatusType.CANCELLED:
      return "bg-red-100 text-red-800";
    case BookingStatusType.COMPLETED:
      return "bg-green-100 text-green-800";
    default:
      return "bg-gray-100 text-gray-800";
  }
};
</script>
