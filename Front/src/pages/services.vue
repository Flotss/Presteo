<template>
  <div
    class="min-h-screen bg-cover bg-center"
    style="
      background-image: url('https://devistravaux.org/wp-content/uploads/2016/10/plan-maison-architecte.jpg');
    "
  >
    <div
      class="max-w-4xl mx-auto p-6 bg-white bg-opacity-90 rounded-lg shadow-md mt-12 mb-12"
    >
      <!-- Header -->
      <h1 class="text-4xl font-extrabold text-center text-gray-900 mb-6">
        Services list
      </h1>
      <p class="text-lg text-center text-gray-600 mb-8">
        Discover the available services and find the one which suits you the
        best :
      </p>

      <!-- Search Bar -->
      <div class="mb-8">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Search for a service..."
          class="w-full p-4 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      <!-- Services List -->
      <ul v-if="paginatedServices.length > 0" class="divide-y divide-gray-200">
        <div
          v-for="service in paginatedServices"
          :key="service.id"
          class="py-4 relative rounded-lg group"
        >
          <NuxtLink :to="'/booking/' + service.id">
            <div class="flex flex-col sm:flex-row justify-between items-center gap-4">
              <div class="flex-1 w-full">
                <div class="flex flex-col gap-2">
                  <div class="flex flex-wrap gap-3 items-center">
                    <h1 class="text-lg font-semibold text-gray-800 hover:text-blue-600">
                      {{ service.title }}
                      {{ service.domain }}
                    </h1>
                    <span
                      class="inline-flex items-center rounded-md bg-gray-50 px-2 py-1 text-s font-medium text-gray-600 ring-1 ring-gray-500/10 ring-inset"
                    >
                      City : {{ service.city }}
                    </span>
                  </div>
                  <p class="text-sm text-gray-500">Price: {{ service.price }} €</p>
                </div>
              </div>
              <div class="flex-shrink-0 w-full sm:w-auto flex justify-center">
                <img
                  v-if="service.imageUrl"
                  :src="service.imageUrl"
                  alt="Service image"
                  class="rounded-lg object-contain w-32 h-20 md:w-48 md:h-28 shadow-md border border-gray-200 bg-white"
                />
              </div>
            </div>
          </NuxtLink>
        </div>
      </ul>
      <div v-if="loading" class="text-center text-gray-500">
        Loading of the service list...
      </div>
      <div v-if="error" class="text-center text-red-500">{{ error }}</div>
      <p
        v-if="paginatedServices == 0"
        class="text-center text-gray-500 text-lg mb-8"
      >
        The service you are looking for does not exist.
      </p>

      <!-- Footer -->
      <div class="flex justify-between items-center mt-8">
        <button
          @click="prevPage"
          :disabled="currentPage === 1"
          class="px-6 py-3 bg-blue-600 text-white rounded-lg text-lg font-semibold hover:bg-blue-700 disabled:bg-gray-300 disabled:cursor-not-allowed"
        >
          Previous
        </button>
        <span class="text-gray-600 text-lg">
          Page {{ currentPage }} sur {{ totalPages }}
        </span>
        <button
          @click="nextPage"
          :disabled="currentPage === totalPages"
          class="px-6 py-3 bg-blue-600 text-white rounded-lg text-lg font-semibold hover:bg-blue-700 disabled:bg-gray-300 disabled:cursor-not-allowed"
        >
          Next
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
useHead({
  title: "Services | Presteo",
});

const services = ref([]);
const searchQuery = ref("");
const { loading, fetch, error, data } = useApi("services");

const fetchServices = async () => {
  try {
    await fetch();

    if (error.value) {
      throw new Error(error.value || "Failed to fetch services");
    }

    if (!data.value) {
      throw new Error("Failed to fetch services");
    }

    services.value = data.value;
  } catch (err) {
    console.error(
      "There has been an error while fetching the data:",
      err.message
    );
    services.value = [];
    error.value = "Failed to fetch services.";
  }
};

const filterService = (service) => {
  return (
    service.title.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
    service.domain.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
    service.city.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
    service.price
      .toString()
      .includes(searchQuery.value)
  );
};

const scrollTop = () => {
  window.scrollTo({
    top: 0,
    behavior: "smooth",
  });
};

onMounted(() => {
  fetchServices();
});

const {
  paginatedItems: paginatedServices,
  nextPage,
  prevPage,
  currentPage,
  totalPages,
} = usePagination(services, filterService, scrollTop);
</script>

<style scoped></style>
