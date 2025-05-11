<template>
  <div class="bg-gray-100 min-h-screen">
    <div class="container mx-auto p-6">
      <!-- Service Header -->
      <div class="bg-white rounded-lg shadow-lg p-6 mb-6">
        <h1 class="text-3xl font-bold mb-2">{{ service.title }}</h1>
        <p class="text-gray-600 text-sm mb-4">{{ service.domain }}</p>
        <div class="flex items-center space-x-4">
          <p class="text-lg font-semibold text-gray-800">
            {{ service.price }} € / hour
          </p>
          <p class="text-lg font-semibold text-gray-800">
            Duration: {{ service.duration }} hours
          </p>
        </div>
      </div>

      <!-- Service Image -->
      <div class="rounded-lg overflow-hidden shadow-lg mb-6">
        <img
          :src="service.imageUrl || 'https://via.placeholder.com/800x400'"
          alt="Service Image"
          class="w-full h-64 object-cover"
        />
      </div>

      <!-- Service Details -->
      <div class="bg-white rounded-lg shadow-lg p-6">
        <h2 class="text-2xl font-bold mb-4">Service Details</h2>
        <p class="text-gray-600 mb-4">{{ service.description }}</p>

        <!-- Provider Information -->
        <div class="border-t pt-4 mt-4">
          <h2 class="text-xl font-bold mb-2">Provider Information</h2>
          <p class="text-gray-600">
            <span class="font-semibold">Name:</span> {{ service.providerName }}
          </p>
          <p class="text-gray-600">
            <span class="font-semibold">Email:</span> {{ service.providerEmail }}
          </p>
        </div>
      </div>

      <!-- Booking Section -->
      <div class="bg-white rounded-lg shadow-lg p-6 mt-6">
        <h2 class="text-2xl font-bold mb-4">Book this Service</h2>
        <form @submit.prevent="handleBooking">
          <div class="mb-4">
            <label for="date" class="block text-sm font-medium text-gray-700">
              Select Date
            </label>
            <input
              id="date"
              type="date"
              v-model="bookingDate"
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm"
            />
          </div>
          <button
            type="submit"
            class="w-full bg-blue-600 text-white rounded py-2 transition duration-150 hover:bg-blue-700"
          >
            Book Now
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { useApi } from "@/composables/useApi";

const route = useRoute();
const serviceId = route.params.id;

const service = ref({
  title: "",
  description: "",
  domain: "",
  price: 0,
  duration: 0,
  imageUrl: "",
  providerName: "",
  providerEmail: "",
});

const bookingDate = ref("");

const { fetch } = useApi(`services/${serviceId}`);

const fetchServiceDetails = async () => {
  const response = await fetch();
  if (response) {
    service.value = response;
  } else {
    console.error("Failed to fetch service details");
  }
};

const handleBooking = () => {
  alert(`Service booked for ${bookingDate.value}`);
};

onMounted(() => {
  fetchServiceDetails();
});
</script>

<style>
.container {
  max-width: 800px;
}
</style>