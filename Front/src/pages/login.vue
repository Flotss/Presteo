<template>
  <div 
    class="flex flex-col justify-center items-center bg-gray-100 sm:min-h-[80vh] min-h-[50vh]"
  >
    <div class="container border rounded-lg shadow-lg bg-white p-8 max-w-md mx-auto">
      <h1 class="text-3xl font-bold text-center mb-6">Login</h1>
      <p v-if="loginMessage.message" 
      class="text-sm text-center mb-4"
      :class="{
        'text-red-500': !loginMessage.isSuccess,
        'text-green-500': loginMessage.isSuccess,
      }"
      >
        {{ loginMessage.message }}</p>
      <form @submit.prevent="handleLogin" novalidate>
        <div class="mb-4">
          <label for="email" class="block text-gray-700">Email</label>
          <input
            type="email"
            id="email"
            v-model="email"
            class="w-full border rounded px-3 py-2"
            :class="{
              'placeholder-red-300': placeHolderEmailError,
            }"
            :placeholder="placeHolderEmailError ? placeHolderEmailError : 'jean.dupont@example.com'"
          />
          <span v-if="formatEmailError" class="text-red-500 text-sm">{{ formatEmailError }}</span>
        </div>
        <div class="mb-4">
          <label for="password" class="block text-gray-700">Password</label>
          <input
            type="password"
            id="password"
            v-model="password"
            class="w-full border rounded px-3 py-2 placeholder-red-300"
            :placeholder="passwordError ? passwordError : ''"
          />
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
const loginMessage = ref({
  message: '',
  isSuccess: false,
}); 

const { postData } = useApi('auth/signin');

const placeHolderEmailError = computed(() => {
  if (!submit.value) {
    return '';
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

const formatEmailError = computed(() => {
  if (!submit.value) {
    return '';
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

  loginMessage.value.message = email.value + " | " + password.value;  
  loginMessage.value.isSuccess = true; 
  console.log('Login Message:', loginMessage.value);

  // Example API call to login
  postData({ login: email.value, password: password.value })
    .then((response) => {
      // Handle successful login response
      console.log('Login successful:', response.data);
      loginMessage.value.message = 'Login successful!';
      loginMessage.value.isSuccess = true;
    })
    .catch((error) => {
      // Handle error response
      console.error('Login failed:', error);
      loginMessage.value.message = 'Login failed. Please try again.';
      loginMessage.value.isSuccess = false;
    });
};



</script>

<style>
</style>