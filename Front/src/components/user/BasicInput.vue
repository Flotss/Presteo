<template>
  <div class="mb-4">
    <input
      :id="id"
      :type="type"
      :value="modelValue"
      @input="$emit('update:modelValue', $event.target.value)"
      :placeholder="placeholder"
      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
      :class="{ 'border-red-500': error }"
    />
    <p v-if="formatError" class="mt-1 text-xs text-red-600">
      {{ formatError }}
    </p>
    <p v-if="requiredError" class="mt-1 text-xs text-red-600">
      {{ requiredError }}
    </p>
  </div>
</template>

<script setup>
const props = defineProps({
  id: {
    type: String,
    default: () => `input-${Math.random().toString(36).substring(2, 10)}`
  },
  modelValue: {
    type: [String, Number],
    required: true
  },
  placeholder: {
    type: String,
    default: ""
  },
  type: {
    type: String,
    default: "text",
    validator: (value) => ["text", "email", "password", "tel", "date", "number"].includes(value)
  },
  format: {
    type: String,
    default: "",
  },
  formatError: {
    type: String,
    default: null,
  },
});

const emit = defineEmits(["update:modelValue"]);

const format = ref(props.format);
const formatError = ref(null);
const modelValue = computed({
  get: () => props.modelValue,
  set: (value) => emit("update:modelValue", value),
});
const requiredError = computed(() => {
  return !modelValue.value && props.label ? "This field is required." : null;
});

const error = computed(() => {
  return formatError.value || requiredError.value;
});

watch(() => modelValue.value, (newValue) => {
  if (format.value && !new RegExp(format.value).test(newValue)) {
    formatError.value = `Invalid format. Expected: ${props.formatError}`;
  } else {
    formatError.value = null;
  }
});

</script>