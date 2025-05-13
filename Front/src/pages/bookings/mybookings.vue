<template>
  <div class="container mx-auto px-4 py-8 max-w-7xl">
    <h1 class="text-3xl font-bold text-gray-900 mb-8 flex items-center">
      <font-awesome-icon
        :icon="['fas', 'calendar-check']"
        class="h-8 w-8 mr-3 text-blue-600"
      />
      My Bookings
    </h1>

    <!-- Active Bookings Section -->
    <div class="mb-12">
      <h2 class="text-2xl font-semibold text-gray-800 mb-6 flex items-center">
        <font-awesome-icon
          :icon="['fas', 'clock']"
          class="h-6 w-6 mr-2 text-blue-600"
        />
        Active Bookings
      </h2>
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
        v-else-if="activeBookings.length === 0"
        class="text-center py-12 bg-gray-50 rounded-lg"
      >
        <font-awesome-icon
          :icon="['fas', 'calendar-times']"
          class="h-12 w-12 text-gray-400 mx-auto mb-4"
        />
        <p class="text-gray-600">No active bookings found</p>
      </div>
      <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div
          v-for="booking in activeBookings"
          :key="booking.id"
          class="bg-white rounded-lg shadow-md p-6"
        >
          <div class="flex justify-between items-start mb-4">
            <h3 class="text-lg font-medium text-gray-900">
              {{ booking.service.title }}
            </h3>
            <span
              :class="getStatusClass(booking.status)"
              class="px-3 py-1 rounded-full text-sm font-medium"
            >
              {{ booking.status }}
            </span>
          </div>
          <div class="space-y-3">
            <div class="flex items-center text-gray-600">
              <font-awesome-icon
                :icon="['fas', 'calendar']"
                class="h-5 w-5 mr-2 text-blue-600"
              />
              {{ formatDate(booking.bookingDate) }}
            </div>
            <div class="flex items-center text-gray-600">
              <font-awesome-icon
                :icon="['fas', 'map-marker-alt']"
                class="h-5 w-5 mr-2 text-blue-600"
              />
              {{ booking.address }}
            </div>
            <div class="flex items-center text-gray-600">
              <font-awesome-icon
                :icon="['fas', 'euro-sign']"
                class="h-5 w-5 mr-2 text-blue-600"
              />
              {{ booking.service.price }}€
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Historical Bookings Section -->
    <div>
      <h2 class="text-2xl font-semibold text-gray-800 mb-6 flex items-center">
        <font-awesome-icon
          :icon="['fas', 'history']"
          class="h-6 w-6 mr-2 text-blue-600"
        />
        Booking History
      </h2>
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
        v-else-if="historicalBookings.length === 0"
        class="text-center py-12 bg-gray-50 rounded-lg"
      >
        <font-awesome-icon
          :icon="['fas', 'history']"
          class="h-12 w-12 text-gray-400 mx-auto mb-4"
        />
        <p class="text-gray-600">No booking history found</p>
      </div>
      <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div
          v-for="booking in historicalBookings"
          :key="booking.id"
          class="bg-white rounded-lg shadow-md p-6"
        >
          <div class="flex justify-between items-start mb-4">
            <h3 class="text-lg font-medium text-gray-900">
              {{ booking.service.title }}
            </h3>
            <span
              :class="getStatusClass(booking.status)"
              class="px-3 py-1 rounded-full text-sm font-medium"
            >
              {{ booking.status }}
            </span>
          </div>
          <div class="space-y-3">
            <div class="flex items-center text-gray-600">
              <font-awesome-icon
                :icon="['fas', 'calendar']"
                class="h-5 w-5 mr-2 text-blue-600"
              />
              {{ formatDate(booking.bookingDate) }}
            </div>
            <div class="flex items-center text-gray-600">
              <font-awesome-icon
                :icon="['fas', 'map-marker-alt']"
                class="h-5 w-5 mr-2 text-blue-600"
              />
              {{ booking.address }}
            </div>
            <div class="flex items-center text-gray-600">
              <font-awesome-icon
                :icon="['fas', 'euro-sign']"
                class="h-5 w-5 mr-2 text-blue-600"
              />
              {{ booking.service.price }}€
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import type { Booking } from "~/model/booking";
import { BookingStatusType } from "~/model/booking";

const bookings = ref<Booking[]>([]);
const loading = ref(true);

const activeBookings = computed(() => {
  return bookings.value.filter(
    (booking) =>
      booking.status === BookingStatusType.PENDING ||
      booking.status === BookingStatusType.CONFIRMED ||
      booking.status === BookingStatusType.IN_PROGRESS
  );
});

const historicalBookings = computed(() => {
  return bookings.value.filter(
    (booking) =>
      booking.status === BookingStatusType.CANCELLED ||
      booking.status === BookingStatusType.COMPLETED
  );
});

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

const formatDate = (date: Date) => {
  return new Date(date).toLocaleString("fr-FR", {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
};

onMounted(async () => {
  try {
    const { data, fetch } = useApi<Booking[]>("bookings/mybookings");
    await fetch();
    if (data.value) {
      bookings.value = data.value;
    }
  } catch (error) {
    console.error("Error fetching bookings:", error);
  } finally {
    loading.value = false;
  }
});
</script>
