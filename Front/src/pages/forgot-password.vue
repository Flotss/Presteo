<template>
  <div
    class="flex flex-col justify-center items-center bg-gray-100 sm:min-h-[80vh] min-h-[50vh]"
  >
    <div
      class="container border rounded-lg shadow-lg bg-white p-8 max-w-md mx-auto"
    >
      <h1 class="text-3xl font-bold text-center mb-6">Forgot Password</h1>

      <p
        v-if="statusMessage.message"
        class="text-sm text-center mb-4"
        :class="{
          'text-red-500': !statusMessage.isSuccess,
          'text-green-500': statusMessage.isSuccess,
        }"
      >
        {{ statusMessage.message }}
      </p>

      <form v-if="!requestSent" @submit.prevent="handleSubmit" novalidate>
        <div class="mb-4">
          <label for="email" class="block text-gray-700">Email</label>
          <input
            type="email"
            id="email"
            v-model="email"
            class="w-full border rounded px-3 py-2"
            :class="{
              'border-red-500': submitted && emailError,
            }"
            placeholder="john.doe@example.com"
          />
          <span v-if="submitted && emailError" class="text-red-500 text-sm">
            {{ emailError }}
          </span>
        </div>

        <button
          type="submit"
          class="w-full text-white rounded py-2 transition duration-150"
          :class="{
            'bg-green-400': statusMessage.isSuccess && !loading,
            'bg-blue-600 hover:bg-blue-700':
              isEmailValid && !statusMessage.isSuccess,
            'bg-gray-400 cursor-not-allowed':
              !isEmailValid && !statusMessage.isSuccess,
          }"
          :disabled="loading"
        >
          <span v-if="loading" class="flex justify-center items-center">
            <font-awesome-icon :icon="['fas', 'circle-notch']" spin />
          </span>
          <span
            v-else-if="statusMessage.isSuccess"
            class="flex justify-center items-center"
          >
            <font-awesome-icon :icon="['fas', 'check']" class="text-black" />
          </span>
          <span v-else> Reset Password </span>
        </button>
      </form>

      <div v-else class="text-center">
        <div
          class="bg-green-100 text-green-700 p-4 rounded-lg mb-4 flex items-center"
        >
          <font-awesome-icon :icon="['fas', 'envelope']" class="mr-2" />
          A reset link has been sent to your email address if it exists in our
          system.
        </div>
        <p class="text-gray-600 mb-4">
          Check your inbox and follow the instructions to reset your password.
        </p>
      </div>

      <p class="mt-4 text-center">
        <NuxtLink to="/login" class="text-blue-600 hover:underline">
          Back to login page
        </NuxtLink>
      </p>
    </div>
  </div>
</template>

<script lang="ts" setup>
useHead({
  title: "Forgot Password | Presteo",
});

const email = ref("");
const submitted = ref(false);
const requestSent = ref(false);
const statusMessage = ref({
  message: "",
  isSuccess: false,
});

const { fetch: fetchData, loading } = useApi("auth/forgot-password");

const emailError = computed(() => {
  if (!submitted.value) return "";

  if (!email.value) {
    return "Email address is required";
  }

  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailPattern.test(email.value)) {
    return "Invalid email format";
  }

  return "";
});

const isEmailValid = computed(() => {
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return email.value && emailPattern.test(email.value);
});

const handleSubmit = async () => {
  submitted.value = true;
  statusMessage.value.message = "";
  statusMessage.value.isSuccess = false;

  if (!isEmailValid.value) {
    return;
  }

  try {
    const response = await fetchData({ email: email.value });

    if (response) {
      requestSent.value = true;
    }
  } catch (err: any) {
    console.error("Error during reset request:", err);
    statusMessage.value.message = "An error occurred. Please try again.";
    statusMessage.value.isSuccess = false;
  }
};

useHead({
  title: "Forgot Password | Presteo",
});
</script>

<style></style>
