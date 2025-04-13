<template>
  <div>
    <!-- Loading State -->
    <div v-if="loading && !user" class="text-center py-8">
      <font-awesome-icon
        icon="fa-solid fa-circle-notch"
        spin
        class="text-gray-500 text-4xl mb-4"
      />
      <p class="text-gray-600">Loading profile information...</p>
    </div>

    <!-- Error Message -->
    <div
      v-if="errorMessage"
      class="alert alert-error bg-red-50 text-red-800 border border-red-200 p-4 rounded-lg mb-6 text-sm"
    >
      {{ errorMessage }}
    </div>

    <!-- Info Message -->
    <div
      v-if="infoMessage"
      class="alert alert-info bg-blue-50 text-blue-800 border border-blue-200 p-4 rounded-lg mb-6 text-sm"
    >
      {{ infoMessage }}
    </div>

    <!-- Not Found State -->
    <div
      v-if="notFound"
      class="not-found text-center py-12 bg-white rounded-lg shadow-sm"
    >
      <font-awesome-icon
        icon="fa-solid fa-user-slash"
        class="text-gray-300 text-5xl mb-4"
      />
      <h2 class="text-xl font-medium text-gray-700 mb-2">User Not Found</h2>
      <p class="text-gray-500">
        The user you're looking for doesn't exist. Please check the ID.
      </p>
    </div>
  </div>
</template>

<script setup>

defineProps({
  loading: {
    type: Boolean,
    default: false,
  },
  errorMessage: {
    type: String,
    default: "",
  },
  infoMessage: {
    type: String,
    default: "",
  },
  notFound: {
    type: Boolean,
    default: false,
  },
  user: {
    type: [Object, null],
    default: () => null,
  },
});
</script>
