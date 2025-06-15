<template>
  <div
    class="flex flex-col justify-center items-center bg-gray-100 min-h-screen"
  >
    <div
      v-if="loading"
      class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50"
    >
      <div class="bg-white p-8 rounded-lg shadow-lg flex flex-col items-center">
        <font-awesome-icon
          :icon="['fas', 'circle-notch']"
          spin
          class="text-4xl text-blue-600 mb-4"
        />
        <p class="text-gray-700">Loading service data...</p>
      </div>
    </div>

    <div
      class="container border rounded-lg shadow-lg bg-white p-8 max-w-md sm:max-w-lg lg:max-w-xl mx-auto my-4"
    >
      <h1 class="text-3xl font-bold text-center mb-6">Edit Service</h1>

      <form novalidate @submit.prevent="handleUpdateService">
        <div class="flex flex-col sm:flex-row sm:space-x-4">
          <FormInput
            id="title"
            v-model="title"
            label="Title *"
            type="text"
            :submit="submit"
            :placeholder="'Enter the service title'"
          />
          <FormInput
            id="domain"
            v-model="domain"
            label="Domain *"
            type="text"
            :submit="submit"
            :placeholder="'Enter the service domain'"
          />
        </div>
        <FormInput
          id="description"
          v-model="description"
          label="Description *"
          type="text"
          :submit="submit"
          :placeholder="'Enter the service description'"
        />
        <FormInput
          id="city"
          v-model="city"
          label="City *"
          type="text"
          :submit="submit"
          :placeholder="'Enter the city of the service provider'"
        />
        <div class="flex flex-col sm:flex-row sm:space-x-4 size-400">
          <FormInput
            id="price"
            v-model="price"
            label="Price *"
            type="number"
            min="0"
            :submit="submit"
            :placeholder="'Enter the service price per hour'"
          />
          <FormInput
            id="duration"
            v-model="duration"
            label="Duration *"
            type="number"
            min="0"
            :submit="submit"
            :placeholder="'Enter the average duration of the service in hours'"
          />
        </div>

        <div class="mb-4">
          <label
            class="block mb-2 text-sm text-gray-900 dark:text-white"
            for="file_input"
            >Update image</label
          >
          <input
            id="file_input"
            class="block w-full text-sm text-gray-900 border border-gray-300 rounded-lg cursor-pointer bg-gray-50 dark:text-gray-400 focus:outline-none dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400"
            type="file"
            accept="image/*"
            @change="onFileChange"
          />
          <div v-if="service?.imageUrl" class="mt-2">
            <img
              :src="service.imageUrl"
              alt="Current service image"
              class="w-32 h-32 object-cover rounded-lg"
            />
          </div>
        </div>

        <button
          type="submit"
          class="w-full bg-blue-600 text-white rounded py-2 transition duration-150"
          :class="{
            'bg-green-400': updateServiceMessage.isSuccess && !loading,
            'bg-blue-600 hover:bg-blue-700':
              allInputRequired && !updateServiceMessage.isSuccess,
            'bg-gray-400 cursor-not-allowed':
              !allInputRequired && !updateServiceMessage.isSuccess,
          }"
          :disabled="loading"
        >
          <span v-if="loading" class="flex justify-center items-center">
            <font-awesome-icon :icon="['fas', 'circle-notch']" spin />
          </span>
          <span
            v-else-if="updateServiceMessage.isSuccess"
            class="flex justify-center items-center"
          >
            <font-awesome-icon :icon="['fas', 'check']" class="text-black" />
          </span>
          <span v-else> Update Service </span>
        </button>
        <p
          v-if="updateServiceMessage.message"
          class="text-sm text-center mb-4"
          :class="{
            'text-red-500': !updateServiceMessage.isSuccess,
            'text-green-500': updateServiceMessage.isSuccess,
          }"
        >
          {{ updateServiceMessage.message }}
        </p>
      </form>
    </div>
  </div>
</template>

<script lang="ts" setup>
import type { Service } from "~/model/service";

const route = useRoute();
const router = useRouter();
const serviceId = route.params.id;

const submit = ref(false);
const title = ref({ content: "", isValid: false });
const description = ref({ content: "", isValid: false });
const domain = ref({ content: "", isValid: false });
const price = ref({ content: "", isValid: false });
const duration = ref({ content: "", isValid: false });
const city = ref({ content: "", isValid: false });
const selectedFile = ref<File | null>(null);

const service = ref<Service | null>(null);
const loading = ref(true);

const updateServiceMessage = ref({
  message: "",
  isSuccess: false,
});

const allInputRequired = computed(() => {
  return (
    title.value.content.trim() !== "" &&
    description.value.content.trim() !== "" &&
    domain.value.content.trim() !== "" &&
    city.value.content.trim() !== "" &&
    price.value.content.trim() !== "" &&
    duration.value.content.trim() !== ""
  );
});

const onFileChange = (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target.files && target.files[0]) {
    selectedFile.value = target.files[0];
  }
};

// Fetch service data
const fetchService = async () => {
  loading.value = true;
  try {
    const { fetch, data } = useApi<Service>(`services/${serviceId}`);
    await fetch();
    if (data.value) {
      service.value = data.value;
      // Pre-fill form with existing data
      title.value = { content: service.value.title, isValid: true };
      description.value = { content: service.value.description, isValid: true };
      domain.value = { content: service.value.domain, isValid: true };
      price.value = { content: String(service.value.price), isValid: true };
      duration.value = {
        content: String(service.value.durationHours),
        isValid: true,
      };
      city.value = { content: service.value.city, isValid: true };
    }
  } catch (error) {
    console.error("Error fetching service:", error);
    updateServiceMessage.value = {
      message: "Failed to fetch service data",
      isSuccess: false,
    };
  } finally {
    loading.value = false;
  }
};

// Update service
const handleUpdateService = async () => {
  loading.value = true;

  if (!allInputRequired.value) {
    updateServiceMessage.value.message = "Please fill all required fields.";
    updateServiceMessage.value.isSuccess = false;
    loading.value = false;
    return;
  }

  try {
    // First update the service details
    const { put: updateService, error: updateError } =
      useApi(`services/update`);
    const serviceData = {
      id: serviceId,
      title: title.value.content,
      description: description.value.content,
      domain: domain.value.content,
      city: city.value.content,
      price: parseFloat(price.value.content),
      durationHours: parseInt(duration.value.content),
    };

    await updateService(serviceData);
    if (updateError.value) {
      throw new Error(updateError.value || "Failed to update service");
    }

    // If there's a new image, update it
    if (selectedFile.value) {
      const formData = new FormData();
      formData.append("file", selectedFile.value);
      formData.append("serviceId", String(serviceId));
      const { post: updateImage, error: imageError } = useApi(
        `services/update-service-picture`
      );
      await updateImage(formData);
      if (imageError.value) {
        throw new Error(imageError.value || "Failed to update service image");
      }
    }

    updateServiceMessage.value.message = "Service updated successfully!";
    updateServiceMessage.value.isSuccess = true;
    setTimeout(() => {
      router.back();
    }, 2000);
  } catch (e: unknown) {
    updateServiceMessage.value.message =
      "Error updating service: " + (e instanceof Error ? e.message : String(e));
    updateServiceMessage.value.isSuccess = false;
  } finally {
    loading.value = false;
  }
};

// Fetch service data on component mount
onMounted(() => {
  fetchService();
});
</script>
