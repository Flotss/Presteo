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
          v-model="searchQuery"
          type="text"
          placeholder="Search for a service..."
          class="w-full p-4 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      <!-- Services List -->
      <ul v-if="paginatedServices.length > 0" class="divide-y divide-gray-200">
        <div
          v-for="service in paginatedServices"
          :key="service.id"
          class="group transition-shadow duration-200 hover:shadow-lg bg-white rounded-2xl shadow-sm flex flex-row items-stretch gap-3 p-3 md:p-4 mb-2 border border-gray-100 hover:border-blue-200"
        >
          <NuxtLink
            :to="'/booking/' + service.id"
            class="flex flex-row w-full gap-6 items-stretch no-underline"
          >
            <div
              class="flex-shrink-0 items-center justify-center bg-gray-100 rounded-2xl overflow-hidden min-w-[140px] min-h-[100px] md:min-w-[220px] md:min-h-[150px] max-w-[220px] max-h-[150px] border border-gray-200 hidden md:flex"
            >
              <img
                v-if="service.imageUrl"
                :src="service.imageUrl"
                alt="Service image"
                class="object-cover w-full h-full transition-transform duration-300 group-hover:scale-105"
              />
            </div>
            <div class="flex flex-col justify-between flex-1 py-2">
              <div class="flex flex-col gap-2">
                <div class="flex flex-wrap gap-3 items-center mb-1">
                  <h2
                    class="text-xl font-bold text-gray-900 hover:text-blue-600 transition-colors leading-tight"
                  >
                    {{ service.title }}
                  </h2>
                  <span
                    class="inline-flex items-center rounded-full bg-blue-50 px-3 py-1 text-xs font-semibold text-blue-700 ring-1 ring-blue-200"
                  >
                    {{ service.domain }}
                  </span>
                  <span
                    class="inline-flex items-center rounded-full bg-gray-50 px-3 py-1 text-xs font-medium text-gray-600 ring-1 ring-gray-200"
                  >
                    {{ service.city }}
                  </span>
                </div>
                <div class="flex items-center gap-2 mb-1 mt-1">
                  <div class="w-8 h-8 rounded-full overflow-hidden bg-gray-100">
                    <img
                      v-if="
                        service.provider && service.provider.profileImageUrl
                      "
                      :src="service.provider.profileImageUrl"
                      alt="Provider profile"
                      class="w-full h-full object-cover"
                    />
                    <NuxtImg
                      v-else
                      :src="
                        'https://api.dicebear.com/9.x/dylan/svg?seed=' +
                        service.provider.id
                      "
                      alt="Provider avatar"
                      class="w-full h-full object-cover"
                    />
                  </div>
                  <span class="text-sm text-gray-700 font-medium">
                    {{ service.provider.firstName }}
                    {{ service.provider.lastName }}
                  </span>
                </div>
              </div>
              <div class="flex flex-row items-center gap-6 mt-2">
                <div class="flex items-center gap-2">
                  <span class="text-lg font-semibold text-blue-700"
                    >{{ service.price }} €</span
                  >
                  <span class="text-xs text-gray-400">/ hour</span>
                </div>
                <div class="flex items-center gap-2">
                  <RatingDisplay
                    :rating="service.averageRating"
                    :count="service.reviewCount"
                  />
                  <span
                    v-if="!service.reviewCount || service.reviewCount === 0"
                    class="text-xs text-gray-400"
                    >No rating yet</span
                  >
                </div>
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
          :disabled="currentPage === 1"
          class="px-6 py-3 bg-blue-600 text-white rounded-lg text-lg font-semibold hover:bg-blue-700 disabled:bg-gray-300 disabled:cursor-not-allowed"
          @click="prevPage"
        >
          Previous
        </button>
        <span class="text-gray-600 text-lg">
          Page {{ currentPage }} sur {{ totalPages }}
        </span>
        <button
          :disabled="currentPage === totalPages"
          class="px-6 py-3 bg-blue-600 text-white rounded-lg text-lg font-semibold hover:bg-blue-700 disabled:bg-gray-300 disabled:cursor-not-allowed"
          @click="nextPage"
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
    service.price.toString().includes(searchQuery.value)
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

<style scoped>
/* Amélioration de l'intégration de l'image */
.group:hover img {
  z-index: 1;
}
</style>
