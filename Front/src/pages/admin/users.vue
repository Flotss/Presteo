<template>
  <div class="max-w-4xl mx-auto p-6 bg-white rounded-lg shadow-md mt-12 mb-12">
    <h1 class="text-4xl font-extrabold text-center text-gray-900 mb-6">
      Users list
    </h1>
    <p class="text-lg text-center text-gray-600 mb-8">
      Search and navigue amongst the connected users :
    </p>

    <!-- Barre de recherche -->
    <div class="mb-8">
      <input
        type="text"
        v-model="searchQuery"
        placeholder="Search for a user..."
        class="w-full p-4 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
      />
      <select
        v-model="roleFilter"
        class="mt-4 w-full p-4 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
      >
        <option value="">All Roles</option>
        <option value="Admin">ADMIN</option>
        <option value="Customer">CUSTOMER</option>
      </select>
    </div>

    <!-- Modifications dans la liste des utilisateurs -->
    <ul v-if="paginatedUsers.length > 0" class="divide-y divide-gray-200 mb-8">
      <li
        v-for="user in paginatedUsers"
        :key="user.id"
        class="flex justify-between items-center py-4"
      >
        <div class="flex items-center space-x-4">
          <div
            class="w-10 h-10 bg-blue-500 text-white rounded-full flex items-center justify-center"
          >
            <img
              :src="'https://api.dicebear.com/9.x/dylan/svg?seed=' + user.id"
              alt="Avatar"
              class="h-10 w-10 rounded-full border border-gray-200"
            />
          </div>
          <div class="flex flex-col space-y-0">
            <div class="flex space-x-5">
              <div class="text-lg font-semibold p-0 m-0 text-gray-800">
                {{ user.firstName }} {{ user.lastName }}
              </div>
              <span
                v-if="user.role && user.role.name"
                class="inline-flex items-center rounded-md bg-blue-50 px-2 py-0 ml-2 font-medium text-blue-700 ring-1 ring-blue-700/10 ring-inset"
              >
                {{ user.role.name }}
              </span>
            </div>
            <small class="text-gray-500 p-0 m-0">{{ user.email }}</small>
          </div>
        </div>
        <div class="flex flex-col space-y-1">
          <NuxtLink :to="'/user/' + user.id + '/profile'">
            <button @click="goToProfile(user)">
              <span
                class="inline-flex items-center justify-center rounded-md w-16 hover:bg-green-50 px-2 py-1 text-xs font-medium hover:text-green-700 ring-1 ring-green-600/20 ring-inset transition-duration-500 transition-all"
              >
                Modify
              </span>
            </button>
          </NuxtLink>
          <button @click="deleteUser(user)">
            <span
              class="inline-flex items-center justify-center rounded-md w-16 hover:bg-red-50 px-2 py-1 text-xs font-medium hover:text-red-700 ring-1 ring-red-600/10 ring-inset transition-duration-500 transition-all"
            >
              Delete
            </span>
          </button>
        </div>
      </li>
    </ul>
    <div v-if="loading" class="text-center text-gray-500">
      Loading of the user list...
    </div>
    <div v-if="error" class="text-center text-red-500">{{ error }}</div>
    <p
      v-if="paginatedUsers == 0"
      class="text-center text-gray-500 text-lg mb-8"
    >
      The user you are looking for does not exist.
    </p>

    <!-- Pagination -->
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
</template>

<script setup>
definePageMeta({
  middleware: "auth",
});

const users = ref([]);
const searchQuery = ref("");
const roleFilter = ref("");
const { loading, data, error,  fetch } = useApi("users");

const authStore = useAuthStore();

const fetchUsers = async () => {
  try {
    debugger
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

const filterUser = (user) => {
  const fullName = `${user.firstName} ${user.lastName}`.toLowerCase();
  const matchesRole =
    roleFilter.value === "" ||
    user.role?.name?.toLowerCase() === roleFilter.value.toLowerCase();
  return (
    matchesRole &&
    (fullName.includes(searchQuery.value.toLowerCase()) ||
      user.email.toLowerCase().includes(searchQuery.value.toLowerCase()))
  );
};

const {
  paginatedItems: paginatedUsers,
  nextPage,
  prevPage,
  currentPage,
  totalPages,
} = usePagination(users, filterUser, scrollTop);

const deleteUser = async (user) => {
  const fullName = `${user.firstName} ${user.lastName}`;
  const confirmDelete = confirm(
    `Are you sure you want to delete the user: ${fullName}? This action cannot be undone.`
  );

  if (!confirmDelete) {
    return;
  }

  try {
    const { delete: deleteApi, error: deleteError } = useApi(
      `users/${user.id}`
    );
    const response = await deleteApi();

    if (response.status === 204) {
      users.value = users.value.filter((u) => u.id !== user.id);
      alert(`User ${fullName} has been successfully deleted.`);
    } else {
      throw new Error(deleteError.value || "Failed to delete user.");
    }
  } catch (err) {
    console.error(
      "There has been an error while deleting the user:",
      err.message
    );
    alert(`Failed to delete user: ${fullName}`);
  }
};

onMounted(() => {
  if (authStore.isAdmin) {
    fetchUsers();
  } else {
    const router = useRouter();
    router.push("/unauthorized");
  }
});

const scrollTop = () => {
  window.scrollTo({
    top: 0,
    behavior: "smooth",
  });
};
</script>