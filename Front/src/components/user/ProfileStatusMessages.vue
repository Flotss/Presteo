<template>
  <div>
    <!-- Loading State -->
    <div v-if="loading && !user" class="text-center py-8">
      <font-awesome-icon
        icon="fa-solid fa-circle-notch"
        spin
        class="text-gray-500 text-4xl mb-4"
      />
      <p class="text-gray-600">Loading profile information...</p>
    </div>

    <!-- Error Message -->
    <div
      v-if="errorMessage"
      class="alert alert-error bg-red-50 text-red-800 border border-red-200 p-4 rounded-lg mb-2 text-sm relative"
    >
      {{ errorMessage }}
      <div class="w-full h-1 bg-red-100 rounded mt-2 overflow-hidden">
        <div
          class="h-full bg-red-400 transition-all duration-100 linear"
          :style="{ width: errorProgress + '%' }"
        />
      </div>
    </div>

    <!-- Info Message -->
    <div
      v-if="infoMessage"
      class="alert alert-info bg-blue-50 text-blue-800 border border-blue-200 p-4 rounded-lg mb-2 text-sm relative"
    >
      {{ infoMessage }}
      <div class="w-full h-1 bg-blue-100 rounded mt-2 overflow-hidden">
        <div
          class="h-full bg-blue-400 transition-all duration-100 linear"
          :style="{ width: infoProgress + '%' }"
        />
      </div>
    </div>

    <!-- Not Found State -->
    <div
      v-if="notFound"
      class="not-found text-center py-12 bg-white rounded-lg shadow-sm"
    >
      <font-awesome-icon
        icon="fa-solid fa-user-slash"
        class="text-gray-300 text-5xl mb-4"
      />
      <h2 class="text-xl font-medium text-gray-700 mb-2">User Not Found</h2>
      <p class="text-gray-500">
        The user you're looking for doesn't exist. Please check the ID.
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onUnmounted, defineEmits } from "vue";

const props = defineProps({
  loading: {
    type: Boolean,
    default: false,
  },
  errorMessage: {
    type: String,
    default: "",
  },
  infoMessage: {
    type: String,
    default: "",
  },
  notFound: {
    type: Boolean,
    default: false,
  },
  user: {
    type: [Object, null],
    default: () => null,
  },
  autoHideDuration: {
    type: Number,
    default: 4000,
  },
});

const emit = defineEmits(["clear-error-message", "clear-info-message"]);

const errorProgress = ref(0);
const infoProgress = ref(0);
let errorTimer = null;
let infoTimer = null;

function startErrorTimer() {
  clearErrorTimer();
  errorProgress.value = 0;
  if (!props.errorMessage) return;
  const interval = 40;
  let elapsed = 0;
  errorTimer = setInterval(() => {
    elapsed += interval;
    errorProgress.value = Math.min(
      (elapsed / props.autoHideDuration) * 100,
      100
    );
    if (elapsed >= props.autoHideDuration) {
      clearErrorTimer();
      emit("clear-error-message");
    }
  }, interval);
}
function clearErrorTimer() {
  if (errorTimer) {
    clearInterval(errorTimer);
    errorTimer = null;
  }
  errorProgress.value = 0;
}

function startInfoTimer() {
  clearInfoTimer();
  infoProgress.value = 0;
  if (!props.infoMessage) return;
  const interval = 40;
  let elapsed = 0;
  infoTimer = setInterval(() => {
    elapsed += interval;
    infoProgress.value = Math.min(
      (elapsed / props.autoHideDuration) * 100,
      100
    );
    if (elapsed >= props.autoHideDuration) {
      clearInfoTimer();
      emit("clear-info-message");
    }
  }, interval);
}
function clearInfoTimer() {
  if (infoTimer) {
    clearInterval(infoTimer);
    infoTimer = null;
  }
  infoProgress.value = 0;
}

watch(
  () => props.errorMessage,
  (val) => {
    if (val) {
      startErrorTimer();
    } else {
      clearErrorTimer();
    }
  }
);

watch(
  () => props.infoMessage,
  (val) => {
    if (val) {
      startInfoTimer();
    } else {
      clearInfoTimer();
    }
  }
);

onUnmounted(() => {
  clearErrorTimer();
  clearInfoTimer();
});
</script>
