<template>
  <div
    class="flex flex-col justify-center items-center bg-gray-100 sm:min-h-[80vh] min-h-[50vh]"
  >
    <div
      class="container border rounded-lg shadow-lg bg-white p-8 max-w-md mx-auto"
    >
      <h1 class="text-3xl font-bold text-center mb-3">Change Password</h1>
      <div v-if="isUsingToken" class="mb-4">
        <small class="text-gray-500">
          You are using a token to change your password.
        </small>
      </div>

      <div
        v-if="unauthorizedAccess && authStore.isReady"
        class="mb-4 mt-3 p-4 bg-red-100 text-red-700 rounded-lg text-center"
      >
        <p>You are not authorized to change another user's password.</p>
        <NuxtLink to="/" class="text-blue-600 hover:underline mt-2 inline-block"
          >Back to Home</NuxtLink
        >
      </div>

      <div
        v-else-if="!authStore.isReady"
        class="flex justify-center items-center"
      >
        <font-awesome-icon icon="circle-notch" spin class="" />
      </div>

      <p
        v-if="statusMessage.message && !unauthorizedAccess"
        class="text-sm text-center mb-4"
        :class="{
          'text-red-500': !statusMessage.isSuccess,
          'text-green-500': statusMessage.isSuccess,
        }"
      >
        {{ statusMessage.message }}
      </p>

      <form
        v-if="!unauthorizedAccess && authStore.isReady"
        @submit.prevent="handleChangePassword"
        novalidate
      >
        <div v-if="!isUsingToken" class="mb-4">
          <label for="oldPassword" class="block text-gray-700"
            >Old Password</label
          >
          <input
            type="password"
            id="oldPassword"
            v-model="oldPassword"
            class="w-full border rounded px-3 py-2"
            :class="{ 'border-red-500': submitted && !oldPassword }"
            placeholder="Enter your current password"
          />
          <span v-if="submitted && !oldPassword" class="text-red-500 text-sm">
            Old password is required
          </span>
        </div>

        <div class="mb-4">
          <PasswordField
            type="password"
            id="newPassword"
            v-model="newPassword"
            placeholder="Enter your new password"
          />
          <span v-if="submitted && !newPassword" class="text-red-500 text-sm">
            New password is required
          </span>
        </div>

        <div class="mb-4">
          <label for="confirmPassword" class="block text-gray-700"
            >Confirm Password</label
          >
          <input
            type="password"
            id="confirmPassword"
            v-model="confirmPassword"
            class="w-full border rounded px-3 py-2"
            :class="{ 'border-red-500': submitted && passwordError }"
            placeholder="Confirm your new password"
          />
          <span v-if="submitted && passwordError" class="text-red-500 text-sm">
            {{ passwordError }}
          </span>
        </div>

        <button
          type="submit"
          class="w-full text-white rounded py-2 transition duration-150"
          :class="{
            'bg-green-400': statusMessage.isSuccess && !api.loading,
            'bg-blue-600 hover:bg-blue-700':
              allInputsValid && !statusMessage.isSuccess,
            'bg-gray-400 cursor-not-allowed':
              !allInputsValid && !statusMessage.isSuccess,
          }"
          :disabled="api.loading && !allInputsValid"
        >
          <span v-if="api.loading.value" class="flex justify-center items-center">
            <font-awesome-icon :icon="['fas', 'circle-notch']" spin />
          </span>
          <span
            v-else-if="statusMessage.isSuccess"
            class="flex justify-center items-center"
          >
            <font-awesome-icon :icon="['fas', 'check']" class="text-black" />
          </span>
          <span v-else> Change Password </span>
        </button>
      </form>

      <p v-if="!unauthorizedAccess" class="mt-4 text-center">
        <NuxtLink
          v-if="!isUsingToken"
          :to="`/user/${userId}/profile`"
          class="text-blue-600 hover:underline"
        >
          Back to Profile
        </NuxtLink>
        <NuxtLink v-else to="/login" class="text-blue-600 hover:underline">
          Back to Login
        </NuxtLink>
      </p>
    </div>
  </div>
</template>

<script lang="ts" setup>
useHead({
  title: "Change Password | Presteo",
});

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();
const id = ref(route.params.id);
const userId = ref(id.value);

// Détecter si on utilise un token au lieu d'un ID
const isUsingToken = computed(() => {
  return isNaN(parseInt(userId.value as string));
});

const oldPassword = ref("");
const newPassword = ref({
  content: "",
  isValid: false,
});
const confirmPassword = ref("");
const submitted = ref(false);
const unauthorizedAccess = computed(() => {
  if (isUsingToken.value) {
    return false;
  }
  return authStore.user?.id !== parseInt(userId.value as string);
});
const statusMessage = ref({
  message: "",
  isSuccess: false,
});

const changePasswordAuth = useApi("auth/change-password");
const changePasswordToken = useApi("auth/change-password-token");

const api = computed(() => {
  const {
    post: postData,
    loading,
    data,
    error,
  } = isUsingToken.value ? changePasswordToken : changePasswordAuth;

  return {
    postData,
    data,
    loading,
    error,
  };
});

onMounted(async () => {
  if (!authStore.isReady) {
    await authStore.initialize();
    await nextTick();
  }

  if (!authStore.isLoggedIn && !isUsingToken.value) {
    router.push("/login");
  }
});

const passwordError = computed(() => {
  if (!submitted.value) {
    return "";
  }

  if (!confirmPassword.value) {
    return "Confirm password is required";
  }

  if (newPassword.value.content !== confirmPassword.value) {
    return "Passwords do not match";
  }

  return "";
});

const allInputsValid = computed(() => {
  if (isUsingToken.value) {
    return (
      newPassword.value.content &&
      confirmPassword.value &&
      newPassword.value.content === confirmPassword.value
    );
  }

  return (
    oldPassword.value &&
    newPassword.value.content &&
    confirmPassword.value &&
    newPassword.value.content === confirmPassword.value
  );
});

const handleChangePassword = async () => {
  submitted.value = true;
  statusMessage.value.message = "";
  statusMessage.value.isSuccess = false;

  if (!allInputsValid.value) {
    return;
  }

  const payload = isUsingToken.value
    ? {
        token: userId.value,
        newPassword: newPassword.value.content,
        confirmPassword: confirmPassword.value,
      }
    : {
        userId: parseInt(userId.value as string),
        oldPassword: oldPassword.value,
        newPassword: newPassword.value.content,
        confirmPassword: confirmPassword.value,
      };

  try {
    const response = await api.value.postData(payload);
    const data = api.value.data.value;
    const error = api.value.error.value;
    if (data == "Password updated successfully") {
      statusMessage.value.message = "Password changed successfully!";
      statusMessage.value.isSuccess = true;

      setTimeout(() => {
        statusMessage.value.message = "Redirecting in 1 second...";
      }, 1000);

      setTimeout(() => {
        if (isUsingToken.value) {
          router.push("/login");
        } else {
          router.push(`/user/${userId.value}/profile`);
        }
      }, 2000);
    } else {
      console.error("Error while changing password:", api.value.error);
      statusMessage.value.message = error.error || error.message;
      statusMessage.value.isSuccess = false;
    }
  } catch (err: any) {
    statusMessage.value.message =
      "An error occurred while changing the password.";
    statusMessage.value.isSuccess = false;
  }
};
</script>

<style></style>
