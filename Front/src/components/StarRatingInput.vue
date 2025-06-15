<template>
  <div class="flex items-center">
    <button
      v-for="star in 5"
      :key="star"
      type="button"
      :aria-label="`Rate ${star} star${star > 1 ? 's' : ''}`"
      class="focus:outline-none"
      @mouseenter="hoveredMain = star"
      @mouseleave="hoveredMain = 0"
      @click="openModal(star)"
    >
      <font-awesome-icon
        :icon="[
          hoveredMain > 0
            ? star <= hoveredMain
              ? 'fas'
              : 'far'
            : modelValue >= star
            ? 'fas'
            : 'far',
          'star',
        ]"
        :class="[
          'h-6 w-6 transition-colors',
          (hoveredMain > 0 ? star <= hoveredMain : modelValue >= star)
            ? 'text-yellow-400'
            : 'text-gray-300',
        ]"
      />
    </button>

    <!-- Modal -->
    <div
      v-if="showModal"
      class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-40"
    >
      <div class="bg-white rounded-lg shadow-lg p-6 w-full max-w-sm relative">
        <button
          v-if="!loading && !success"
          class="absolute top-2 right-2 text-gray-400 hover:text-gray-600"
          @click="closeModal"
        >
          <font-awesome-icon :icon="['fas', 'times']" class="h-5 w-5" />
        </button>
        <h3 class="text-lg font-semibold mb-4">Leave a Review</h3>
        <div
          v-if="loading"
          class="flex flex-col items-center justify-center py-8"
        >
          <font-awesome-icon
            :icon="['fas', 'spinner']"
            spin
            class="h-8 w-8 text-blue-500 mb-2"
          />
          <span class="text-gray-500">Submitting...</span>
        </div>
        <div
          v-else-if="success"
          class="flex flex-col items-center justify-center py-8"
        >
          <font-awesome-icon
            :icon="['fas', 'check-circle']"
            class="h-10 w-10 text-green-500 mb-2"
          />
          <span class="text-green-600 font-semibold">Review submitted!</span>
        </div>
        <div v-else>
          <div class="flex items-center mb-4">
            <button
              v-for="star in 5"
              :key="star"
              type="button"
              class="focus:outline-none"
              @mouseenter="hovered = star"
              @mouseleave="hovered = 0"
              @click="selectedRating = star"
            >
              <font-awesome-icon
                :icon="[
                  hovered > 0
                    ? star <= hovered
                      ? 'fas'
                      : 'far'
                    : star <= selectedRating
                    ? 'fas'
                    : 'far',
                  'star',
                ]"
                :class="[
                  'h-6 w-6 transition-colors',
                  (hovered > 0 ? star <= hovered : star <= selectedRating)
                    ? 'text-yellow-400'
                    : 'text-gray-300',
                ]"
              />
            </button>
          </div>
          <textarea
            v-model="reviewText"
            rows="3"
            class="w-full border border-gray-300 rounded-lg p-2 mb-4 focus:ring-2 focus:ring-blue-500 focus:outline-none"
            placeholder="Write your review (optional)"
          />
          <button
            class="w-full bg-blue-600 text-white py-2 rounded-lg font-semibold hover:bg-blue-700 transition"
            :disabled="selectedRating === 0"
            @click="submit"
          >
            Submit
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";

defineProps({
  modelValue: {
    type: Number,
    default: 0,
  },
});
const emit = defineEmits(["update:rating", "submit"]);

const showModal = ref(false);
const hovered = ref(0);
const hoveredMain = ref(0);
const selectedRating = ref(0);
const reviewText = ref("");
const loading = ref(false);
const success = ref(false);

function openModal(star: number) {
  selectedRating.value = star;
  hovered.value = 0;
  reviewText.value = "";
  showModal.value = true;
  loading.value = false;
  success.value = false;
}
function closeModal() {
  showModal.value = false;
}
async function submit() {
  loading.value = true;
  emit("submit", {
    rating: selectedRating.value,
    reviewText: reviewText.value,
    done,
  });
}
function done() {
  loading.value = false;
  success.value = true;
  setTimeout(() => {
    closeModal();
  }, 1000);
}
</script>
