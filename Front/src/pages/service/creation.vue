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
            label="Title *"
            type="text"
            v-model="title"
            :submit="submit"
            :placeholder="'Enter the service title'"
          />
          <FormInput
            id="domain"
            label="Domain *"
            type="text"
            v-model="domain"
            :submit="submit"
            :placeholder="'Enter the service domain'"
          />
        </div>
        <FormInput
          id="description"
          label="Description *"
          type="text"
          v-model="description"
          :submit="submit"
          :placeholder="'Enter the service description'"
        />
        <FormInput
          id="city"
          label="City *"
          type="text"
          v-model="city"
          :submit="submit"
          :placeholder="'Enter the city of the service provider'"
        />
        <div class="flex flex-col sm:flex-row sm:space-x-4 size-400">
          <FormInput
            id="price"
            label="Price *"
            type="number"
            min="0"
            v-model="price"
            :submit="submit"
            :placeholder="'Enter the service price per hour'"
          />
          <FormInput
            id="duration"
            label="Duration *"
            type="number"
            min="0"
            v-model="duration"
            :submit="submit"
            :placeholder="'Enter the average duration of the service in hours'"
          />
        </div>

        <div class="mb-4">
        <label
          class="block mb-2 text-sm text-gray-900 dark:text-white"
          for="file_input"
          >Upload file</label
        >
        <input
          class="block w-full text-sm text-gray-900 border border-gray-300 rounded-lg cursor-pointer bg-gray-50 dark:text-gray-400 focus:outline-none dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400"
          id="file_input"
          type="file"
          accept="image/*"
          @change="onFileChange"
        />
        </div>

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
const authStore = useAuthStore();
const submit = ref(false);
const id = computed(() => authStore.user?.id);
const title = ref({ content: "", isValid: false });
const description = ref({ content: "", isValid: false });
const domain = ref({ content: "", isValid: false });
const price = ref({ content: "", isValid: false });
const duration = ref({ content: "", isValid: false });
const imageUrl = ref({ content: "", isValid: true });
const city = ref({ content: "", isValid: false });

const selectedFile = ref<File | null>(null);

const createServiceMessage = ref({
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

const { post: postData, loading, error } = useApi("services/create");
const handleCreateService = async () => {
  loading.value = true;

  if (!allInputRequired.value) {
    createServiceMessage.value.message = "Please fill all required fields.";
    createServiceMessage.value.isSuccess = false;
    loading.value = false;
    return;
  }

  // Création du service avec FormData pour gérer l'image
  const formData = new FormData();
  formData.append("providerId", String(id.value ?? ""));
  formData.append("title", title.value.content);
  formData.append("description", description.value.content);
  formData.append("domain", domain.value.content);
  formData.append("city", city.value.content);
  formData.append("price", price.value.content);
  formData.append("durationHours", duration.value.content);
  if (selectedFile.value) {
    formData.append("imageFile", selectedFile.value);
  }

  let response;
  try {
    response = await postData(formData); // postData doit accepter FormData
    console.log("Réponse backend création service :", response);
    if (error.value) {
      throw new Error(error.value || "Failed to create service");
    }
  } catch (e: any) {
    createServiceMessage.value.message = "Erreur lors de la création du service. " + e.message;
    createServiceMessage.value.isSuccess = false;
    return;
  }

  createServiceMessage.value.message = "Service created successfully!";
  createServiceMessage.value.isSuccess = true;
  setTimeout(() => {
    const router = useRouter();
    router.push("/services");
  }, 2000);
};
</script>
