<template>
  <div>
    <LoadingScreen v-if="isLoading" />

    <div v-else class="container mx-auto px-4 py-8 max-w-7xl">
      <ServiceHeader v-if="service" :service="service" />

      <DateSelection
        v-model="selectedDate"
        :min-date="minDate"
        @update:model-value="fetchAvailability"
      />

      <TimeSlotsGrid
        v-if="!isMobile"
        :slots="availability"
        :loading="loadingSlots"
        :selected-slot="selectedSlot"
        @select="selectTimeSlot"
      />

      <TimeSlotsSelect
        v-else
        :slots="availability"
        :loading="loadingSlots"
        v-model:selected-morning-slot="selectedMorningSlot"
        v-model:selected-afternoon-slot="selectedAfternoonSlot"
      />

      <BookingModal
        v-model="showModal"
        :loading="loading"
        v-model:address="bookingForm.address"
        v-model:additional-info="bookingForm.additionalInfo"
        @use-user-address="useUserAddress"
        @submit="submitBooking"
      />
    </div>

    <!-- Section Reviews -->
    <div v-if="service && service.reviews && service.reviews.length > 0" class="container mx-auto px-4 py-8 max-w-3xl">
      <h2 class="text-2xl font-bold mb-4">Customer Reviews</h2>
      <div v-for="(review, idx) in service.reviews" :key="idx" class="mb-6 p-4 border rounded-lg bg-white shadow">
        <div class="flex items-center mb-2">
          <span class="font-semibold mr-2">{{ review.customer?.firstName }} {{ review.customer?.lastName }}</span>
          <RatingDisplay :rating="review.rating" :count="1" :show-count="false" class="ml-2" />
        </div>
        <p class="text-gray-700">{{ review.reviewText }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Booking } from "~/model/booking";
import type { Service } from "~/model/service";

useHead({
  title: "Booking | Presteo",
});

interface TimeSlot {
  available: boolean;
  time: string;
}

interface BookingForm {
  address: {
    content: string;
    isValid: boolean;
  };
  additionalInfo: string;
}

const route = useRoute();
const authStore = useAuthStore();
const serviceId = computed(() => route.params.id);

const selectedDate = ref("");
const availability = ref<TimeSlot[]>([]);
const selectedSlot = ref<TimeSlot | null>(null);
const showModal = ref(false);
const loading = ref(false);
const isMobile = ref(false);
const service = ref<Service | null>(null);
const providerId = computed(() => service.value?.provider.id);

const isLoading = ref(true);
const loadingSlots = ref(false);

const bookingForm = ref<BookingForm>({
  address: {
    content: "",
    isValid: false,
  },
  additionalInfo: "",
});

const selectedMorningSlot = ref("");
const selectedAfternoonSlot = ref("");

const minDate = computed(() => {
  const today = new Date();
  return today.toISOString().split("T")[0];
});

const isHoliday = async (_date: string) => {
  return false;
};

const getNextAvailableDate = async (startDate: string) => {
  const currentDate = new Date(startDate);
  while (await isHoliday(currentDate.toISOString().split("T")[0])) {
    currentDate.setDate(currentDate.getDate() + 1);
  }
  return currentDate.toISOString().split("T")[0];
};

const fetchServiceDetails = async () => {
  if (!serviceId.value) return;

  const { fetch, data, error } = useApi<Service>(`services/${serviceId.value}`);
  await fetch();

  if (error.value) {
    console.error("Error fetching service details:", error.value);
    return;
  }

  if (data.value) {
    service.value = data.value;
  }
};

const fetchAvailability = async () => {
  if (!selectedDate.value) return;

  loadingSlots.value = true;
  try {
    const { fetch, data, error } = useApi<TimeSlot[]>(
      `bookings/provider/${providerId.value}/availability`
    );
    await fetch({
      date: selectedDate.value,
    });

    if (error.value) {
      console.error("Error fetching availability:", error.value);
      return;
    }

    availability.value = data.value || [];
  } finally {
    loadingSlots.value = false;
  }
};

const selectTimeSlot = (slot: TimeSlot) => {
  if (!slot.available) return;
  selectedSlot.value = slot;
  showModal.value = true;
};

const submitBooking = async () => {
  if (!selectedSlot.value || !authStore.user || !bookingForm.value.address.isValid) return;

  loading.value = true;
  try {
    const payload = {
      serviceId: Number(serviceId.value),
      customerId: authStore.user.id,
      bookingDate: selectedSlot.value.time,
      additionalInfo: bookingForm.value.additionalInfo,
      address: bookingForm.value.address.content,
    };

    const { data, error, post } = useApi<Booking>("bookings/create");
    await post(payload);

    if (error.value) {
      throw error.value;
    }

    if (data.value) {
      const bookingId = data.value.id;
      
      if (data.value) {
        navigateTo({
          path: `/booking/confirmation/${bookingId}`,
          query: { booking: JSON.stringify(data.value) }
        });
      }
    }

    showModal.value = false;
    bookingForm.value = {
      address: {
        content: "",
        isValid: false,
      },
      additionalInfo: "",
    };
    selectedSlot.value = null;
    await fetchAvailability();
  } catch (error) {
    console.error("Error creating booking:", error);
  } finally {
    loading.value = false;
  }
};

const useUserAddress = () => {
  bookingForm.value.address.content = authStore.user?.address || "";
  bookingForm.value.address.isValid = true;
};

onMounted(async () => {
  try {
    isMobile.value = window.innerWidth < 768;
    window.addEventListener("resize", () => {
      isMobile.value = window.innerWidth < 768;
    });

    await fetchServiceDetails();

    const today = new Date();
    const initialDate = await getNextAvailableDate(today.toISOString().split("T")[0]);
    selectedDate.value = initialDate;
    await fetchAvailability();
  } finally {
    setTimeout(() => {
      isLoading.value = false;
    }, 1000);
  }
});
</script>