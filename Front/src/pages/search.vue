<template>
  <div class="min-h-screen bg-gray-100 py-6 sm:py-8 md:py-10">
    <div class="max-w-4xl mx-auto p-2 sm:p-4 md:p-6 bg-white rounded-lg shadow-md">
      <h1 class="text-2xl sm:text-3xl md:text-4xl font-extrabold text-center text-gray-900 mb-4 sm:mb-6">
        Global Search
      </h1>
      <p class="text-base sm:text-lg text-center text-gray-600 mb-2">
        Search for a service or a professional by any information:
      </p>
      <p class="text-center text-pretty text-xs sm:text-base text-gray-500 mb-4 sm:mb-8">
        Search : "professional" to find the list of service providers, or "service" to find the list of services.
      </p>
      <div class="mb-4 sm:mb-8">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Search for a service or a user..."
          class="w-full p-3 sm:p-4 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      <ul
        v-if="paginatedMerged.length > 0"
        class="divide-y divide-gray-200 mb-6 sm:mb-8"
      >
        <li
          v-for="item in paginatedMerged"
          :key="item._type + '-' + item.id"
          class="flex flex-row justify-between items-center py-3 sm:py-4 gap-3 sm:gap-0"
        >
          <div class="flex items-center space-x-3 sm:space-x-4 w-full">
            <div
              class="w-12 h-12 sm:w-10 sm:h-10 bg-blue-500 text-white rounded-full flex items-center justify-center flex-shrink-0"
            >
              <img
                v-if="item._type === 'user'"
                :src="'https://api.dicebear.com/9.x/dylan/svg?seed=' + item.id"
                alt="Avatar"
                class="h-12 w-12 sm:h-10 sm:w-10 rounded-full border border-gray-200 object-cover"
              />
              <img
                v-else-if="item._type === 'service' && item.imageUrl"
                :src="item.imageUrl"
                alt="Service photo"
                class="h-12 w-12 sm:h-10 sm:w-10 rounded-full border border-gray-200 object-cover"
              />
              <span
                v-else
                class="h-12 w-12 sm:h-10 sm:w-10 flex items-center justify-center font-bold text-xl"
                >S</span
              >
            </div>
            <div class="flex flex-col space-y-0 w-full">
              <div class="flex flex-col sm:flex-row sm:space-x-5 w-full">
                <NuxtLink
                  v-if="item._type === 'user'"
                  :to="'/user/' + item.id + '/profile'"
                >
                  <div class="text-base sm:text-lg font-semibold p-0 m-0 text-gray-800 hover:text-blue-500 transition-colors duration-200">
                    {{ item.firstName }} {{ item.lastName }}
                  </div>
                </NuxtLink>
                <NuxtLink v-else :to="'/booking/' + item.id">
                  <div class="text-base sm:text-lg font-semibold p-0 m-0 text-gray-800 hover:text-blue-500 transition-colors duration-200">
                    {{ item.title }}
                  </div>
                </NuxtLink>
                <span
                  v-if="item._type === 'service' && item.domain"
                  class="inline-flex items-center max-w-fit rounded-md bg-gray-50 px-2 py-1 text-xs sm:text-s font-medium text-gray-600 ring-1 ring-gray-500/10 ring-inset mt-1 sm:mt-0"
                >
                  {{ item.domain }}
                </span>
              </div>
              <small
                v-if="item._type === 'user'"
                class="text-gray-500 p-0 m-0"
                >{{ item.email }}</small
              >
              <div v-else class="text-gray-800">
                <div class="text-xs sm:text-sm text-gray-500">
                  {{ item.city }}
                </div>
                <div class="text-xs sm:text-sm text-gray-500">
                  Price: {{ item.price }} €
                </div>
              </div>
            </div>
          </div>
          <div class="flex flex-col space-y-1 ml-auto">
            <span
              :class="
                item._type === 'user'
                  ? 'inline-flex items-center justify-center rounded-md w-28 bg-green-50 px-2 py-1 text-xs font-medium text-green-700 ring-1 ring-green-600/20 ring-inset'
                  : 'inline-flex items-center justify-center rounded-md w-14 bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-blue-600/20 ring-inset'
              "
            >
              {{ item._type === "user" ? "Service Provider" : "Service" }}
            </span>
          </div>
        </li>
      </ul>
      <div v-if="loading" class="text-center text-gray-500">
        Loading of the list...
        <font-awesome-icon :icon="['fas', 'circle-notch']" spin />
      </div>
      <div v-if="error" class="text-center text-red-500">{{ error }}</div>
      <p
        v-if="!loading && paginatedMerged.length === 0"
        class="text-center text-gray-500 text-base sm:text-lg mb-6 sm:mb-8"
      >
        The data you are looking for does not exist.
      </p>

      <!-- Pagination -->
      <div class="flex flex-col sm:flex-row justify-between items-center mt-6 sm:mt-8 gap-3 sm:gap-0">
        <button
          @click="prevPage"
          :disabled="currentPage === 1"
          class="px-4 sm:px-6 py-2 sm:py-3 bg-blue-600 text-white rounded-lg text-base sm:text-lg font-semibold hover:bg-blue-700 disabled:bg-gray-300 disabled:cursor-not-allowed"
        >
          Previous
        </button>
        <span class="text-gray-600 text-base sm:text-lg">
          Page {{ currentPage }} sur {{ totalPages }}
        </span>
        <button
          @click="nextPage"
          :disabled="currentPage === totalPages"
          class="px-4 sm:px-6 py-2 sm:py-3 bg-blue-600 text-white rounded-lg text-base sm:text-lg font-semibold hover:bg-blue-700 disabled:bg-gray-300 disabled:cursor-not-allowed"
        >
          Next
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
const users = ref([]);
const services = ref([]);
const searchQuery = ref("");
const roleFilter = ref("");
const { loading, data, error, fetch } = useApi("users");
const {
  data: servicesData,
  error: servicesError,
  fetch: fetchServices,
  loading: loadingServices,
} = useApi("services");

const authStore = useAuthStore();

const fetchUsers = async () => {
  try {
    await fetch();

    if (error.value) {
      throw new Error(error.value || "Failed to fetch users");
    }

    if (!data.value) {
      throw new Error("Failed to fetch users");
    }

    users.value = data.value;
    console.log("Users found:", users.value);
  } catch (err) {
    console.error(
      "There has been an error while fetching the data:",
      err.message
    );
    users.value = [];
    error.value = "Failed to fetch users.";
  }
};

const fetchAll = async () => {
  try {
    await Promise.all([fetchUsers(), fetchServices()]);
    if (servicesError.value)
      throw new Error(servicesError.value || "Failed to fetch services");
    if (!servicesData.value) throw new Error("Failed to fetch services");
    services.value = servicesData.value;
  } catch (err) {
    error.value = err.message || "Failed to fetch users/services.";
    users.value = [];
    services.value = [];
  }
};

const filterItem = (item) => {
  const query = searchQuery.value.toLowerCase();
  if (query === "professional") return item._type === "user";
  if (query === "service") return item._type === "service";

  if (item.firstName !== undefined) {
    // User
    const fullName = `${item.firstName} ${item.lastName}`.toLowerCase();
    return (
      fullName.includes(query) ||
      (item.email && item.email.toLowerCase().includes(query)) ||
      (item.role &&
        item.role.name &&
        item.role.name.toLowerCase().includes(query))
    );
  } else {
    // Service
    return (
      (item.title && item.title.toLowerCase().includes(query)) ||
      (item.domain && item.domain.toLowerCase().includes(query)) ||
      (item.city && item.city.toLowerCase().includes(query)) ||
      (item.price && item.price.toString().includes(query))
    );
  }
};

const scrollTop = () => {
  window.scrollTo({
    top: 0,
    behavior: "smooth",
  });
};

const mergedList = computed(() => {
  // Tag each item with its type
  const userItems = users.value
    .filter((u) => u.role && u.role.name === "PROVIDER")
    .map((u) => ({ ...u, _type: "user" }));
  const serviceItems = services.value.map((s) => ({ ...s, _type: "service" }));
  return [...userItems, ...serviceItems];
});

const {
  paginatedItems: paginatedMerged,
  nextPage,
  prevPage,
  currentPage,
  totalPages,
} = usePagination(mergedList, filterItem, scrollTop);

onMounted(() => {
  fetchAll();
});
</script>

<style scoped></style>
