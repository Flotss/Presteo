<template>
  <div class="mb-4">
    <div class="flex items-center justify-between">
      <label :for="id" class="block text-gray-700">{{ label }}</label>
    </div>
    <div class="relative">
      <input
        :type="type"
        :id="id"
        :value="modelValue"
        class="w-full border rounded px-3 py-2"
        :placeholder="placeholder"
        @input="handleInput"
        @focus="showSuggestions = true"
        @blur="hideSuggestionsDelayed"
      />
      <div
        v-if="showSuggestions && suggestions.length > 0"
        class="absolute z-10 w-full bg-white border border-gray-200 rounded-md shadow-lg mt-1 max-h-60 overflow-y-auto"
      >
        <ul>
          <li
            v-for="(suggestion, index) in suggestions"
            :key="index"
            @mousedown="selectSuggestion(suggestion)"
            class="px-4 py-2 hover:bg-gray-100 cursor-pointer truncate"
            :title="suggestion.display_name"
          >
            {{ suggestion.display_name }}
          </li>
        </ul>
      </div>
    </div>
    <span v-if="error && submit" class="text-red-500 text-sm">{{ error }}</span>
    <span v-else-if="!hasValue && submit" class="text-red-500 text-sm">
      {{ label }} is required
    </span>
  </div>
</template>

<script setup>
import { computed, ref } from "vue";

const props = defineProps({
  submit: { type: Boolean, default: false },
  id: { type: String, required: true },
  label: { type: String, required: true },
  type: { type: String, default: "text" },
  modelValue: { type: String, required: true },
  error: { type: String, default: "" },
  placeholder: { type: String, default: "" },
});

const emit = defineEmits(["update:modelValue"]);

const hasValue = computed(() => {
  return props.modelValue?.length > 0;
});

const suggestions = ref([]);
const showSuggestions = ref(false);
const searchTimeout = ref(null);
const blurTimeout = ref(null);

const handleInput = (event) => {
  const query = event.target.value;
  emit("update:modelValue", query);

  if (searchTimeout.value) {
    clearTimeout(searchTimeout.value);
  }

  if (query.length > 2) {
    searchTimeout.value = setTimeout(() => {
      fetchAddressSuggestions(query);
    }, 300);
  } else {
    suggestions.value = [];
  }
};

const fetchAddressSuggestions = async (query) => {
  try {
    const response = await fetch(
      `https://nominatim.openstreetmap.org/search.php?q=${encodeURIComponent(
        query
      )}&format=jsonv2&limit=5`
    );

    if (response.ok) {
      const data = await response.json()
      suggestions.value = data.filter((item, index, self) =>
        index === self.findIndex((t) => (
          t.display_name === item.display_name
        ))
      );
      showSuggestions.value = true;
    } else {
      console.error("Failed to fetch address suggestions");
      suggestions.value = [];
    }
  } catch (error) {
    console.error("Error fetching address suggestions:", error);
    suggestions.value = [];
  }
};

const selectSuggestion = (suggestion) => {
  emit("update:modelValue", suggestion.display_name);
  suggestions.value = [];
  showSuggestions.value = false;
};

const hideSuggestionsDelayed = () => {
  blurTimeout.value = setTimeout(() => {
    showSuggestions.value = false;
  }, 200);
};

onBeforeUnmount(() => {
  if (searchTimeout.value) clearTimeout(searchTimeout.value);
  if (blurTimeout.value) clearTimeout(blurTimeout.value);
});
</script>
