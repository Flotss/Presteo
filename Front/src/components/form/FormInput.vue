<template>
  <div class="mb-4">
    <div class="flex items-center justify-between">
      <label :for="id" class="block text-gray-700">{{ label }}</label>
    </div>
    <input
      :type="type"
      :id="id"
      :value="modelValue"
      class="w-full border rounded px-3 py-2"
      :placeholder="placeholder"
      @input="$emit('update:modelValue', $event.target.value)"
    />
    <span v-if="error && submit" class="text-red-500 text-sm">{{ error }}</span>
    <span v-else-if="!hasValue && submit" class="text-red-500 text-sm">
      {{ label }} is required
    </span>
  </div>
</template>

<script setup>
const props = defineProps({
  submit: { type: Boolean, default: false },
  id: { type: String, required: true },
  label: { type: String, required: true },
  type: { type: String, default: 'text' },
  modelValue: { type: String, required: true },
  error: { type: String, default: '' },
  placeholder: { type: String, default: '' }
});

defineEmits(['update:modelValue']);

const hasValue = computed(() => {
  return props.modelValue?.length > 0;
});

</script>