<template>
  <div class="mb-4">
    <label for="password" class="block text-gray-700">Password</label>
    <input
      type="password"
      id="password"
      :value="modelValue"
      class="w-full border rounded px-3 py-2"
      placeholder="Enter your password"
      @focus="showPasswordRequirements = true"
      @blur="showPasswordRequirements = false"
      @input="emit('update:modelValue', $event.target.value)"
      @keydown.enter="showPasswordRequirements = false"
    />
    <span v-if="passwordError" class="text-red-500 text-sm">{{
      passwordError
    }}</span>
    <Transition
      name="menu"
      class="transition-[height] duration-300"
      enter-active-class="animate-wrapIn"
      leave-active-class="animate-wrapOut"
    >
      <div v-if="showPasswordRequirements" class="text-gray-600 text-sm mt-2">
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
    </Transition>
  </div>
</template>

<script setup>
const props = defineProps({
  modelValue: { type: String, required: true },
  submit: { type: Boolean, default: false },
});

const emit = defineEmits(["update:modelValue", "update:passwordRequirements"]);


const showPasswordRequirements = ref(false);

const passwordLength = computed(() => ({
  isValid: props.modelValue.length >= 8,
  errorText: props.modelValue.length >= 8 ? '' : 'Password must be at least 8 characters long',
}));

const hasUppercase = computed(() => ({
  isValid: /[A-Z]/.test(props.modelValue),
  errorText: /[A-Z]/.test(props.modelValue) ? '' : 'Password must contain at least one uppercase letter',
}));

const hasLowercase = computed(() => ({
  isValid: /[a-z]/.test(props.modelValue),
  errorText: /[a-z]/.test(props.modelValue) ? '' : 'Password must contain at least one lowercase letter',
}));

const hasNumber = computed(() => ({
  isValid: /[0-9]/.test(props.modelValue),
  errorText: /[0-9]/.test(props.modelValue) ? '' : 'Password must contain at least one number',
}));

const hasSpecial = computed(() => ({
  isValid: /[!@#$%^&*(),.?":{}|<>]/.test(props.modelValue),
  errorText: /[!@#$%^&*(),.?":{}|<>]/.test(props.modelValue) ? '' : 'Password must contain at least one special character',
}));


const passwordRequirements = computed(() => {
  const requirements = {
    length: passwordLength.value,
    uppercase: hasUppercase.value,
    lowercase: hasLowercase.value,
    number: hasNumber.value,
    special: hasSpecial.value,
  };

  emit('update:passwordRequirements', Object.values(requirements).every(req => req.isValid));
  return requirements;
});

const passwordError = computed(() => {
  if (!props.submit) {
    return '';
  }

  if (!props.modelValue) {
    return 'Password is required';
  }

  for (const requirement of Object.values(passwordRequirements.value)) {
    if (!requirement.isValid) {
      return requirement.errorText;
    }
  }

  
  return '';
});
</script>
