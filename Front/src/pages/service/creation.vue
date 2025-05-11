<template>
  <div
    class="flex flex-col justify-center items-center bg-gray-100 min-h-screen"
  >
    <div
      class="container border rounded-lg shadow-lg bg-white p-8 max-w-md sm:max-w-lg lg:max-w-xl mx-auto my-4"
    >
      <h1 class="text-3xl font-bold text-center mb-6">Create Service</h1>

      <form @submit.prevent="handleCreateService" novalidate>
        <div class="flex flex-col sm:flex-row sm:space-x-4">
          <FormInput
            id="title"
            label="Title"
            type="text"
            v-model="title"
            :submit="submit"
            :placeholder="'Enter the service title'"
          />
          <FormInput
            id="domain"
            label="Domain"
            type="text"
            v-model="domain"
            :submit="submit"
            :placeholder="'Enter the service domain'"
          />
        </div>
        <FormInput
          id="description"
          label="Description"
          type="text"
          v-model="description"
          :submit="submit"
          :placeholder="'Enter the service description'"
        />
        <div class="flex flex-col sm:flex-row sm:space-x-4 size-400">
          <FormInput
            id="price"
            label="Price"
            type="price"
            v-model="price"
            :submit="submit"
            :placeholder="'Enter the service price per hour'"
          />
          <FormInput
            id="duration"
            label="Duration"
            type="hour"
            v-model="duration"
            :submit="submit"
            :placeholder="'Enter the average duration of the service in hours'"
          />
        </div>
        <FormInput
          id="imageUrl"
          label="Image URL"
          type="text"
          v-model="imageUrl"
          :submit="submit"
          :placeholder="'Enter the image URL (optional)'"
        />
        <button
          type="submit"
          class="w-full bg-blue-600 text-white rounded py-2 transition duration-150"
          :class="{
            'bg-green-400': createServiceMessage.isSuccess && !loading,
            'bg-blue-600 hover:bg-blue-700':
              allInputRequired && !createServiceMessage.isSuccess,
            'bg-gray-400 cursor-not-allowed':
              !allInputRequired && !createServiceMessage.isSuccess,
          }"
          :disabled="loading"
        >
          <span v-if="loading" class="flex justify-center items-center">
            <font-awesome-icon :icon="['fas', 'circle-notch']" spin />
          </span>
          <span
            v-else-if="createServiceMessage.isSuccess"
            class="flex justify-center items-center"
          >
            <font-awesome-icon :icon="['fas', 'check']" class="text-black" />
          </span>
          <span v-else> Create Service </span>
        </button>
        <p
          v-if="createServiceMessage.message"
          class="text-sm text-center mb-4"
          :class="{
            'text-red-500': !createServiceMessage.isSuccess,
            'text-green-500': createServiceMessage.isSuccess,
          }"
        >
          {{ createServiceMessage.message }}
        </p>
      </form>
    </div>
  </div>
</template>

<script lang="ts" setup>
// Utiliser placeholder / codepen.io pour l'image URL
import { ref, computed } from "vue";
import { useApi } from "@/composables/useApi";

const submit = ref(false);
const title = ref({ content: "", isValid: false });
const description = ref({ content: "", isValid: false });
const domain = ref({ content: "", isValid: false });
const price = ref({ content: "", isValid: false });
const duration = ref({ content: "", isValid: false });
const imageUrl = ref({ content: "", isValid: true }); // Optional field

const createServiceMessage = ref({
  message: "",
  isSuccess: false,
});

const allInputRequired = computed(() => {
  return (
    title.value.content.trim() !== "" &&
    description.value.content.trim() !== "" &&
    domain.value.content.trim() !== "" &&
    price.value.content.trim() !== "" &&
    duration.value.content.trim() !== ""
  );
});

const handleCreateService = () => {
  submit.value = true;

  if (!allInputRequired.value) {
    return;
  }

  sendCreateService();
};

const { loading, data, error, post: postData } = useApi("services/create");

const sendCreateService = async () => {
  const response = await postData({
    title: title.value.content,
    description: description.value.content,
    domain: domain.value.content,
    price: parseFloat(price.value.content),
    imageUrl: imageUrl.value.content || null,
  });

  if (response && response.message === "Service created successfully!") {
    createServiceMessage.value.message = "Service created successfully!";
    createServiceMessage.value.isSuccess = true;

    setTimeout(() => {
      createServiceMessage.value.message = "Redirecting in 1 second...";
    }, 1000);

    setTimeout(() => {
      const router = useRouter();
      router.push("/services");
    }, 2000);
  } else {
    console.error("Service creation failed:", response, error.value);
    createServiceMessage.value.message = `Service creation failed, please try again. ${error.value?.error}`;
    createServiceMessage.value.isSuccess = false;
  }
};
</script>
