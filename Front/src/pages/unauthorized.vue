<template>
  <div
    class="min-h-[80vh] flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8"
  >
    <div class="max-w-md w-full space-y-8 text-center">
      <div>
        <font-awesome-icon
          icon="fa-solid fa-ban"
          class="text-red-500 text-6xl mb-4"
        />
        <h2 class="text-3xl font-extrabold text-gray-900">
          Unauthorized Access
        </h2>
        <p class="mt-2 text-gray-600">
          You do not have the necessary permissions to access this page.
        </p>
      </div>

      <div class="mt-8 space-y-4">
        <div class="bg-yellow-50 border-l-4 border-yellow-400 p-4 rounded-md">
          <div class="flex">
            <div class="flex-shrink-0">
              <font-awesome-icon
                icon="fa-solid fa-triangle-exclamation"
                class="text-yellow-400"
              />
            </div>
            <div class="ml-3">
              <p class="text-sm text-yellow-700">
                If you believe this error is incorrect, please contact your
                administrator.
              </p>
            </div>
          </div>
        </div>
      </div>

      <div class="flex justify-center space-x-4 mt-6">
        <NuxtLink
          to="/"
          class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
        >
          <font-awesome-icon icon="fa-solid fa-home" class="mr-2" />
          Return to Home
        </NuxtLink>

        <button
          @click="goBack"
          class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
        >
          <font-awesome-icon icon="fa-solid fa-arrow-left" class="mr-2" />
          Go Back
        </button>
      </div>

      <div class="mt-8 pt-6 border-t border-gray-200">
        <p class="text-sm text-gray-500">
          If you need assistance, please contact the support team at
          <a
            href="mailto:support@presteo.com"
            class="text-blue-600 hover:text-blue-500"
          >
            support@presteo.com
          </a>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// Retrieve the authentication store
const authStore = useAuthStore();
const router = useRouter();

// Function to go back to the previous page
const goBack = () => {
  router.back();
};

// Set the page title
useHead({
  title: "Unauthorized Access | Presteo",
});
</script>
