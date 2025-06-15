<template>
  <div class="max-w-lg mx-auto p-8 bg-white rounded-lg shadow-md mt-12 mb-12">
    <h1 class="text-2xl font-bold mb-6 text-center">Report this provider</h1>
    <form class="flex flex-col space-y-4" @submit.prevent="submitReport">
      <div>
        <label class="block text-gray-700 font-semibold mb-1"
          >Report description</label
        >
        <textarea
          v-model="description"
          required
          rows="4"
          class="w-full p-3 border border-gray-300 rounded-lg resize-none"
          placeholder="Describe the issue..."
        />
      </div>
      <button
        type="submit"
        class="bg-red-600 text-white px-6 py-2 rounded-lg font-semibold hover:bg-red-700 transition flex items-center justify-center"
        :disabled="loading"
      >
        <span v-if="loading">
          <font-awesome-icon icon="circle-notch" spin class="mr-2" />
          Reporting...
        </span>
        <span v-else>Submit report</span>
      </button>
      <div v-if="error" class="text-red-500 text-center">{{ error }}</div>
      <div v-if="success" class="text-green-600 text-center">
        Report submitted successfully!
      </div>
    </form>
  </div>
</template>

<script lang="ts" setup>
import { useRoute, useRouter } from "vue-router";
const authStore = useAuthStore();
const route = useRoute();
const router = useRouter();

const providerId = route.params.id;
const reporterId = ref(authStore.user?.id);

watch(
  () => authStore.user,
  (newUser) => {
    reporterId.value = newUser?.id;
  }
);
const description = ref("");
const loading = ref(false);
const error = ref("");
const success = ref(false);

const submitReport = async () => {
  error.value = "";
  success.value = false;
  loading.value = true;
  try {
    const { post, error: postError } = useApi("reports/create");
    await post({
      userReportedId: providerId,
      userReporterId: reporterId.value,
      description: description.value,
    });
    if (postError.value) throw new Error(postError.value.message);
    success.value = true;
    setTimeout(() => {
      router.back();
    }, 1500);
  } catch (e: unknown) {
    if (e instanceof Error) {
      error.value =
        e.message || "An error occurred while submitting the report.";
    } else {
      error.value = "An error occurred while submitting the report.";
    }
  } finally {
    loading.value = false;
  }
};
</script>
