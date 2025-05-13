<template>
  <div>
    <BookingConfirmation
      v-if="booking"
      :booking="booking"
      @close="navigateTo('/bookings')"
      @view-bookings="navigateTo('/bookings')"
    />
    <LoadingScreen v-else />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import type { Booking } from '~/model/booking';

const route = useRoute();
const booking = ref<Booking | null>(null);

onMounted(async () => {
  const bookingData = route.query.booking;
  if (bookingData) {
    booking.value = JSON.parse(bookingData as string) as Booking;
  } else {
    const bookingId = route.params.id;
    const { data, fetch } = useApi<Booking>(`bookings/${bookingId}`);
    await fetch();
    if (data.value) {
      booking.value = data.value as Booking;
    }
  }
});
</script> 