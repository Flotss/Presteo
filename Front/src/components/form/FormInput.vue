<template>
  <div class="mb-4">
    <div class="flex items-center justify-between">
      <label :for="id" class="block text-gray-700">{{ label }}</label>
    </div>
    <input
      :type="type"
      :id="id"
      :value="modelValue.content"
      class="w-full border rounded px-3 py-2"
      :placeholder="placeholder"
      @input="handleInput"
    />
    <span v-if="error && submit" class="text-red-500 text-sm">{{ error }}</span>
    <span v-else-if="!hasValue && submit" class="text-red-500 text-sm">
      {{ label }} is required
    </span>
    <span v-else-if="!formatIsCorrect && submit" class="text-red-500 text-sm">
      Invalid format: {{ formatString }}
    </span>
    <span v-else-if="!minLengthIsValid && submit" class="text-red-500 text-sm">
      Minimum length: {{ minLength }}
    </span>
    <span v-else-if="!maxLengthIsValid && submit" class="text-red-500 text-sm">
      Maximum length: {{ maxLength }}
    </span>
  </div>
</template>

<script setup>
const props = defineProps({
  submit: { type: Boolean, default: false },
  id: { type: String, required: true },
  label: { type: String, required: true },
  type: { type: String, default: 'text' },
  modelValue: { 
    type: Object, 
    required: true, 
    default: () => ({ content: '', isValid: false }) 
  },
  error: { type: String, default: '' },
  placeholder: { type: String, default: '' },
  formatRegex: { type: String, default: '.*' },
  formatString: { type: String, default: '' },
  minLength: { type: Number, default: 0 },
  maxLength: { type: Number, default: 100 },
});


const emit = defineEmits(['update:modelValue']);

const hasValue = computed(() => {
  return props.modelValue.content.length > 0;
});

const minLengthIsValid = computed(() => {
  return props.minLength > 0 ? props.modelValue.content.length >= props.minLength : true;
});

const maxLengthIsValid = computed(() => {
  return props.maxLength > 0 ? props.modelValue.content.length <= props.maxLength : true;
});

const formatIsCorrect = computed(() => {
  if (props.formatRegex.length) {
    const regex = new RegExp(props.formatRegex);
    return regex.test(props.modelValue.content);
  }
  return true;
});

const isValid = computed(() => {
  return hasValue.value &&
    minLengthIsValid.value &&
    maxLengthIsValid.value &&
    formatIsCorrect.value;
});


const handleInput = (event) => {
  props.modelValue.content = event.target.value;
  props.modelValue.isValid = isValid.value;
  emit('update:modelValue', props.modelValue);
};

</script>