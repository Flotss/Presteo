<template>
  <div class="mb-4">
    <div class="flex items-center justify-between">
      <label :for="id" class="block text-gray-700">{{ label }}</label>
    </div>
    <select
      :id="id"
      :value="modelValue.content"
      class="w-full border rounded px-3 py-2"
      @change="handleSelectChange"
    >
      <option
        v-for="option in options"
        :key="option.value"
        :value="option.value"
      >
        {{ option.text }}
      </option>
    </select>
    <span v-if="error && submit" class="text-red-500 text-sm">{{ error }}</span>
    <span v-else-if="!hasValue && submit" class="text-red-500 text-sm">
      {{ label }} is required
    </span>
  </div>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
  submit: { type: Boolean, default: false },
  id: { type: String, required: true },
  label: { type: String, required: true },
  modelValue: { 
    type: Object, 
    required: true, 
    default: () => ({ content: '', isValid: false }) 
  },
  error: { type: String, default: "" },
  options: {
    type: Array,
    required: true,
    validator: (value) => {
      return value.every(
        (option) =>
          typeof option === "object" && "value" in option && "text" in option
      );
    },
  },
});

const emit = defineEmits(["update:modelValue"]);

const hasValue = computed(() => {
  return props.modelValue.content.length > 0;
});

const handleSelectChange = (event) => {
  emit("update:modelValue", { content: event.target.value, isValid: hasValue.value });
};
</script>
