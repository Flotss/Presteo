<template>
  <div 
    class="flex flex-col justify-center items-center bg-gray-100 sm:min-h-[80vh] min-h-[50vh]"
  >
    <div class="container border rounded-lg shadow-lg bg-white p-8 max-w-md mx-auto">
      <h1 class="text-3xl font-bold text-center mb-6">Login</h1>
      <form @submit.prevent="handleLogin">
        <div class="mb-4">
          <label for="email" class="block text-gray-700">Email</label>
          <input
            type="email"
            id="email"
            v-model="email"
            class="w-full border rounded px-3 py-2"
          />
          <span v-if="emailError" class="text-red-500 text-sm">{{ emailError }}</span>
        </div>
        <div class="mb-4">
          <label for="password" class="block text-gray-700">Password</label>
          <input
            type="password"
            id="password"
            v-model="password"
            class="w-full border rounded px-3 py-2"
            @focus="showPasswordRequirements = true"
            @blur="showPasswordRequirements = false"
          />
          <span v-if="passwordError" class="text-red-500 text-sm">{{ passwordError }}</span>
          <Transition
            name="menu"
            class="transition-[height] duration-300"
            enter-active-class="animate-password-specification-open"
            leave-active-class="animate-password-specification-close">
            <div v-if="showPasswordRequirements" class="text-gray-600 text-sm mt-2">
              <p>Password must meet the following requirements:</p>
              <div>
                <PasswordRequirement 
                  :isMet="passwordRequirements.length" 
                  text="At least 8 characters" 
                />
                <PasswordRequirement 
                  :isMet="passwordRequirements.length" 
                  text="At least 8 characters" 
                />
                <PasswordRequirement 
                  :isMet="passwordRequirements.uppercase" 
                  text="At least one uppercase letter" 
                />
                <PasswordRequirement 
                  :isMet="passwordRequirements.lowercase" 
                  text="At least one lowercase letter" 
                />
                <PasswordRequirement 
                  :isMet="passwordRequirements.number" 
                  text="At least one number" 
                />
                <PasswordRequirement 
                  :isMet="passwordRequirements.special" 
                  text="At least one special character" 
                />
              </div>
            </div>
          </Transition>
        </div>
        <button
          type="submit"
          class="w-full bg-blue-600 text-white rounded py-2 hover:bg-blue-700 transition duration-150"
        >
          Login
        </button>
        <!-- google -->
        <div class="flex items-center justify-center mt-4">
          <span class="text-gray-500">or</span>
          <button
            type="button"
            class="ml-2 bg-white border border-gray-300 rounded px-4 py-2 flex items-center hover:bg-gray-100 transition duration-150"
          >
            <font-awesome-icon :icon="['fa-brands', 'fa-google']" class="w-5 h-5 mr-2"/>
            Login with Google
          </button>
        </div>
      </form>
      <p class="mt-4 text-center">
        Don't have an account? 
        <NuxtLink to="/signup" class="text-blue-600 hover:underline">Sign Up</NuxtLink>
      </p>
      <p class="mt-4 text-center">
        <NuxtLink to="/forgot-password" class="text-blue-600 hover:underline">Forgot Password?</NuxtLink>
      </p>
    </div>
  </div>
</template>

<script lang="ts" setup>

const submit = ref(false);
const email = ref('');
const password = ref('');

const emailError = computed(() => {
  if (!submit.value) {
    return;
  }

  if (!email.value) {
    return 'Email is required';
  }
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailPattern.test(email.value)) {
    return 'Invalid email format';
  }
  return '';
});

const passwordError = computed(() => {
  if (!submit.value) {
    return;
  }

  if (!password.value) {
    return 'Password is required';
  }
  return '';
});

const showPasswordRequirements = ref(false);

const passwordLength = computed(() => password.value.length >= 8);
const hasUppercase = computed(() => /[A-Z]/.test(password.value));
const hasLowercase = computed(() => /[a-z]/.test(password.value));
const hasNumber = computed(() => /[0-9]/.test(password.value));
const hasSpecial = computed(() => /[!@#$%^&*(),.?":{}|<>]/.test(password.value));

const passwordRequirements = computed(() => {
  return {
    length: passwordLength.value,
    uppercase: hasUppercase.value,
    lowercase: hasLowercase.value,
    number: hasNumber.value,
    special: hasSpecial.value,
  };
});


const allInputRequired = computed(() => {
  return email.value && password.value;
});

const handleLogin = () => {
  submit.value = true;

  if (!allInputRequired.value) {
    return;
  }
  // Handle login logic here
  console.log('Logging in with:', email.value, password.value);
  // You can use a service or API call to authenticate the user
  // For example:
  // await authService.login(email.value, password.value);
  // after successful login, redirect to the dashboard or home page
  // router.push('/dashboard');
  // For now, just log the values
  console.log('Email:', email.value);
};



</script>

<style>

/* ! TODO: ça doit etre dans signup */
.animate-password-specification-open {
  animation: wrapIn 0.4s ease-in-out;
}

.animate-password-specification-close {
  animation: wrapOut 0.4s ease-in-out;
}

@keyframes wrapIn {
  from {
    max-height: 0;
    opacity: 0;
  }
  to {
    max-height: 500px; /* Adjusted to fit content */
    opacity: 1;
  }
}

@keyframes wrapOut {
  from {
    max-height: 500px; /* Adjusted to fit content */
    opacity: 1;
  }
  to {
    max-height: 0;
    opacity: 0;
  }
}
</style>