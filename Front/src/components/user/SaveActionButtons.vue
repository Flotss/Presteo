<template>
  <div
    class="fixed bottom-4 right-4 z-50 bg-white shadow-lg rounded-lg p-3 border border-gray-200"
  >
    <div class="flex space-x-2">
      <button
        @click="$emit('reset')"
        class="px-4 py-2 text-gray-600 border border-gray-300 rounded-md hover:bg-gray-50"
      >
        Reset
      </button>
      <button
        v-if="!confirmSave"
        @click="confirmSave = true"
        class="px-4 py-2 text-white bg-blue-600 rounded-md hover:bg-blue-700"
        :disabled="loading"
      >
        <span>Save</span>
      </button>
      <button
        v-else
        @click="$emit('save')"
        class="px-4 py-2 text-white bg-green-600 rounded-md hover:bg-green-700"
        :disabled="loading"
      >
        <span v-if="loading">
          Saving...
          <font-awesome-icon
            icon="fa-solid fa-circle-notch"
            spin
            class="mr-2"
          />
        </span>
        <span v-else-if="saved">
          Saved
          <font-awesome-icon icon="fa-solid fa-check-circle" class="mr-2" />
        </span>
        <span v-else>Confirm Save</span>
      </button>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  loading: {
    type: Boolean,
    default: false,
  },
  saved: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(["save", "reset"]);
const confirmSave = ref(false);

watch(
  () => props.saved,
  (newVal) => {
    if (newVal) {
      setTimeout(() => {
        confirmSave.value = false;
      }, 2000);
    }
  }
);
</script>
