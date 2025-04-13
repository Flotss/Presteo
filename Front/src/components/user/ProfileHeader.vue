<template>
  <div
    class="profile-header bg-white rounded-lg shadow-sm mb-6 overflow-hidden"
  >
    <div class="p-6 flex items-center">
      <div
        class="profile-photo w-16 h-16 rounded-full bg-gray-200 flex items-center justify-center mr-6"
      >
        <NuxtImg
          :src="'https://api.dicebear.com/9.x/dylan/svg?seed=' + user.id"
          alt="User Profile Image"
          class="w-full h-full rounded-full object-cover"
        />
      </div>
      <div class="profile-greeting w-full">
        <h2 v-if="isUserLoggedIn" class="text-2xl font-bold text-gray-800">
          Hello, {{ user.firstName }}!
        </h2>
        <h2 v-else class="text-2xl font-bold text-gray-800">
          User: {{ user.firstName }} {{ user.lastName }}
        </h2>
        <div class="text-gray-600 text-sm mt-1">
          <div v-if="!editDescription">{{ user.description || 'No description provided' }}</div>
          <div v-else>
            <input
              v-model="tempDescription"
              type="text"
              class="border border-gray-300 rounded-md px-2 py-1 w-full"
              placeholder="Enter your description"
            />
          </div>
          <button
            @click="toggleDescriptionEdit"
            class="text-blue-600 hover:underline text-sm"
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

<script setup>
const props = defineProps({
  user: {
    type: Object ,
    required: true,
  },
  isUserLoggedIn: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(["update:description", "reset"]);

const editDescription = ref(false);
const tempDescription = ref(props.user.description);

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

defineExpose({ resetDescription, closeDescriptionEdit });

watch(tempDescription, (value) => {
  if (editDescription.value) {
    emit("update:description", value);
  }
});
</script>
