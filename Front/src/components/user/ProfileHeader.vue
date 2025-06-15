<template>
  <div
    class="profile-header bg-white rounded-lg shadow-sm mb-6 overflow-hidden"
  >
    <div class="p-6 flex items-center">
      <div class="relative">
        <div
          class="relative w-24 h-24 rounded-full overflow-hidden group cursor-pointer"
          @click="canModify && triggerFileInput()"
        >
          <img
            v-if="previewUrl || user?.profileImageUrl"
            :src="previewUrl || user?.profileImageUrl"
            alt="Profile"
            class="w-full h-full object-cover"
          />
          <NuxtImg
            v-else
            :src="'https://api.dicebear.com/9.x/dylan/svg?seed=' + user.id"
            alt="User Profile Image"
            class="w-full h-full object-cover"
          />
          <input
            ref="fileInput"
            type="file"
            accept="image/*"
            class="hidden"
            @change="handleFileChange"
          />
          <div
            v-if="canModify"
            class="absolute inset-0 bg-black bg-opacity-40 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity duration-200"
          >
            <font-awesome-icon
              :icon="['fas', 'pen']"
              class="text-white text-2xl"
            />
          </div>
        </div>
      </div>
      <div class="profile-greeting w-full">
        <h2 v-if="isUserLoggedIn" class="text-2xl font-bold text-gray-800">
          Hello, {{ user.firstName }}!
        </h2>
        <h2 v-else class="text-2xl font-bold text-gray-800">
          User: {{ user.firstName }} {{ user.lastName }}
        </h2>
        <div class="text-gray-600 text-sm mt-1">
          <div v-if="!editDescription">
            {{ user.description || "No description provided" }}
          </div>
          <div v-else>
            <input
              v-model="tempDescription"
              type="text"
              class="border border-gray-300 rounded-md px-2 py-1 w-full"
              placeholder="Enter your description"
            />
          </div>
          <button
            class="text-blue-600 hover:underline text-sm"
            @click="toggleDescriptionEdit"
          >
            <font-awesome-icon
              :icon="
                editDescription
                  ? 'fa-solid fa-times'
                  : 'fa-solid fa-pen-to-square'
              "
              class="ml-1"
            />
            {{ editDescription ? "Cancel" : "Edit" }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from "vue";

const props = defineProps({
  user: {
    type: Object,
    required: true,
  },
  isUserLoggedIn: {
    type: Boolean,
    default: false,
  },
  canModify: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(["update:description", "reset", "update:image"]);

const editDescription = ref(false);
const tempDescription = ref(props.user.description);
const previewUrl = ref<string | null>(null);
const fileInput = ref<HTMLInputElement | null>(null);

watch(
  () => props.user.description,
  (newDescription) => {
    tempDescription.value = newDescription;
  },
  { immediate: true }
);

const toggleDescriptionEdit = () => {
  if (editDescription.value) {
    tempDescription.value = props.user.description;
    editDescription.value = false;
  } else {
    editDescription.value = true;
  }
};

const resetDescription = () => {
  tempDescription.value = props.user.description;
  editDescription.value = false;
};

const closeDescriptionEdit = () => {
  editDescription.value = false;
};

const handleFileChange = (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target.files && target.files[0]) {
    const file = target.files[0];
    previewUrl.value = URL.createObjectURL(file);
    emit("update:image", file);
  }
};

function triggerFileInput() {
  if (fileInput.value) {
    fileInput.value.click();
  }
}

defineExpose({ resetDescription, closeDescriptionEdit });

watch(tempDescription, (value) => {
  if (editDescription.value) {
    emit("update:description", value);
  }
});
</script>
