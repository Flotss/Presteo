<template>
  <div class="max-w-4xl mx-auto p-6 bg-white rounded-lg shadow-md mt-12 mb-12">
    <!-- Header -->
    <h1 class="text-4xl font-extrabold text-center text-gray-900 mb-6">
      Liste des utilisateurs
    </h1>
    <p class="text-lg text-center text-gray-600 mb-8">
      Recherchez et naviguez parmi les utilisateurs connectés :
    </p>

    <!-- Body -->
    <div class="mb-8">
      <input
        type="text"
        v-model="searchQuery"
        placeholder="Rechercher un utilisateur..."
        class="w-full p-4 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
      />
    </div>
    <ul v-if="filteredUsers.length > 0" class="divide-y divide-gray-200 mb-8">
      <li
        v-for="user in paginatedUsers"
        :key="user.id"
        class="flex justify-between items-center py-4"
      >
        <span class="text-lg font-semibold text-gray-800">{{ user.name }}</span>
        <span class="text-gray-500">{{ user.email }}</span>
      </li>
    </ul>
    <p v-else class="text-center text-gray-500 text-lg mb-8">
      L'utilisateur que vous cherchez n'existe pas.
    </p>

    <!-- Footer -->
    <div class="flex justify-between items-center mt-8">
      <button
        @click="prevPage"
        :disabled="currentPage === 1"
        class="px-6 py-3 bg-blue-600 text-white rounded-lg text-lg font-semibold hover:bg-blue-700 disabled:bg-gray-300 disabled:cursor-not-allowed"
      >
        Précédent
      </button>
      <span class="text-gray-600 text-lg">
        Page {{ currentPage }} sur {{ totalPages }}
      </span>
      <button
        @click="nextPage"
        :disabled="currentPage === totalPages"
        class="px-6 py-3 bg-blue-600 text-white rounded-lg text-lg font-semibold hover:bg-blue-700 disabled:bg-gray-300 disabled:cursor-not-allowed"
      >
        Suivant
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';

const users = ref([]);
const currentPage = ref(1);
const itemsPerPage = 10;
const searchQuery = ref('');

onMounted(async () => {
  users.value = [
    { id: 1, name: 'Alice', email: 'alice@example.com' },
    { id: 2, name: 'Bob', email: 'bob@example.com' },
    { id: 3, name: 'Charlie', email: 'charlie@example.com' },
    { id: 4, name: 'Max', email: 'max@example.com' },
    { id: 5, name: 'Edouard', email: 'edouard@example.com' },
    { id: 6, name: 'Rosa', email: 'rosa@example.com' },
    { id: 7, name: 'Mohamed', email: 'mohamed@example.com' },
    { id: 8, name: 'Jean', email: 'jean@example.com' },
    { id: 9, name: 'Patricia', email: 'patricia@example.com' },
    { id: 10, name: 'Sasha', email: 'sasha@example.com' },
    { id: 11, name: 'Martin', email: 'martin@example.com' },
    { id: 12, name: 'John', email: 'john@example.com' },
    { id: 13, name: 'Elias', email: 'Elias@example.com' },
    { id: 14, name: 'Daisy', email: 'Daisy@example.com' },
    { id: 15, name: 'Lily', email: 'lily@example.com' }
  ];
});

const totalPages = computed(() => Math.ceil(filteredUsers.value.length / itemsPerPage));

const filteredUsers = computed(() => {
  const query = searchQuery.value.toLowerCase();
  return users.value.filter(
    (user) =>
      user.name.toLowerCase().includes(query) ||
      user.email.toLowerCase().includes(query)
  );
});

const paginatedUsers = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return filteredUsers.value.slice(start, end);
});

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
  }
};

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
  }
};
</script>
