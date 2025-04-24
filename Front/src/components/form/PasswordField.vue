<template>
  <div class="mb-4">
    <label for="password" class="block text-gray-700">Password</label>
    <input
      type="password"
      id="password"
      :value="modelValue.content"
      class="w-full border rounded px-3 py-2"
      placeholder="Enter your password"
      @focus="showPasswordRequirements = true"
      @blur="showPasswordRequirements = false"
      @input="handleInput"
      @keydown.enter="showPasswordRequirements = false"
    />
    <span v-if="passwordError" class="text-red-500 text-sm">{{
      passwordError
    }}</span>
    <TransitionOpen
      :showContent="showPasswordRequirements"
    >
      <div class="text-gray-600 text-sm mt-2">
        <p>Password must meet the following requirements:</p>
        <div>
          <PasswordRequirement
            :isMet="passwordRequirements.length.isValid"
            text="At least 8 characters"
          />
          <PasswordRequirement
            :isMet="passwordRequirements.uppercase.isValid"
            text="At least one uppercase letter"
          />
          <PasswordRequirement
            :isMet="passwordRequirements.lowercase.isValid"
            text="At least one lowercase letter"
          />
          <PasswordRequirement
            :isMet="passwordRequirements.number.isValid"
            text="At least one number"
          />
          <PasswordRequirement
            :isMet="passwordRequirements.special.isValid"
            text="At least one special character"
          />
        </div>
      </div>
    </TransitionOpen>
  </div>
</template>

<script setup>
const props = defineProps({
  modelValue: { 
    type: Object, 
    required: true, 
    default: () => ({ content: '', isValid: false }) 
  },
  submit: { type: Boolean, default: false },
});

const emit = defineEmits(["update:modelValue"]);
const showPasswordRequirements = ref(false);

const value = ref(props.modelValue.content);
watch(
  () => props.modelValue,
  (newValue) => {
    value.value = newValue.content;
  },
  { immediate: true }
);

const createRequirement = (regex, errorText) => computed(() => ({
  isValid: regex.test(value.value),
  errorText: regex.test(value.value) ? '' : errorText,
}));

const passwordLength = computed(() => ({
  isValid: value.value.length >= 8,
  errorText: value.value.length >= 8 ? '' : 'Password must be at least 8 characters long',
}));

const hasUppercase = createRequirement(/[A-Z]/, 'Password must contain at least one uppercase letter');
const hasLowercase = createRequirement(/[a-z]/, 'Password must contain at least one lowercase letter');
const hasNumber = createRequirement(/[0-9]/, 'Password must contain at least one number');
const hasSpecial = createRequirement(/[!@#$%^&*(),.?":{}|<>]/, 'Password must contain at least one special character');

const passwordRequirements = computed(() => {
  const requirements = {
    length: passwordLength.value,
    uppercase: hasUppercase.value,
    lowercase: hasLowercase.value,
    number: hasNumber.value,
    special: hasSpecial.value,
  };

  return requirements;
});

const isValid = computed(() => {
  return Object.values(passwordRequirements.value).every((req) => req.isValid);
});

const passwordError = computed(() => {
  if (!props.submit) {
    return '';
  }

  if (!value) {
    return 'Password is required';
  }

  for (const requirement of Object.values(passwordRequirements.value)) {
    if (!requirement.isValid) {
      return requirement.errorText;
    }
  }
  return '';
});

const handleInput = async (event) => {
  value.value = event.target.value;
  await nextTick();
  emit('update:modelValue', { content: value.value, isValid: isValid.value });
};
</script>
