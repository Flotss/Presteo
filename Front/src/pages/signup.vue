<template>
  <div
    class="flex flex-col justify-center items-center bg-gray-100 min-h-screen"
  >
    <div
      class="container border rounded-lg shadow-lg bg-white p-8 max-w-md sm:max-w-lg lg:max-w-xl mx-auto my-4"
    >
      <h1 class="text-3xl font-bold text-center mb-6">Sign Up</h1>
      <p
        v-if="signUpMessage.message"
        class="text-sm text-center mb-4"
        :class="{
          'text-red-500': !signUpMessage.isSuccess,
          'text-green-500': signUpMessage.isSuccess,
        }"
      >
        {{ signUpMessage.message }}
      </p>
      <form @submit.prevent="handleSignUp" novalidate>
        <div class="flex flex-col sm:flex-row sm:space-x-4">
          <FormInput
            id="firstName"
            label="First Name"
            type="text"
            v-model="firstName"
            :submit="submit"
            :placeholder="'Enter your first name'"
            class="flex-1 sm:mb-0"
          />
          <FormInput
            id="lastName"
            label="Last Name"
            type="text"
            v-model="lastName"
            :submit="submit"
            :placeholder="'Enter your last name'"
            class="flex-1"
          />
        </div>
        <FormInput
          id="username"
          label="Username"
          type="text"
          v-model="username"
          :submit="submit"
          :placeholder="'Enter your username'"
        />
        <FormInput
          id="email"
          label="Email"
          type="email"
          v-model="email"
          :submit="submit"
          :error="formatEmailError"
          :placeholder="'jean.dupont@example.com'"
        />
        <FormAddressInput
          id="address"
          label="Address"
          v-model="address"
          :submit="submit"
          :placeholder="'Start typing your address...'"
        />
        <FormInput
          id="phoneNumber"
          label="Phone Number"
          type="tel"
          v-model="phoneNumber"
          :submit="submit"
          :placeholder="'Enter your phone number'"
        />
        <FormInput
          id="birthDate"
          label="Birth Date"
          type="date"
          v-model="birthDate"
          :submit="submit"
        />
        <FormSelect
          id="gender"
          label="Gender"
          v-model="gender"
          :submit="submit"
          :options="[
            { value: '', text: 'Select your gender' },
            { value: 'male', text: 'Male' },
            { value: 'female', text: 'Female' },
            { value: 'other', text: 'Other' },
          ]"
        />
        <PasswordField
          :submit="submit"
          v-model="password"
          @update:passwordRequirements="passwordRequirements = $event"
        />
        <FormInput
          id="confirmPassword"
          label="Confirm Password"
          type="password"
          v-model="confirmPassword"
          :submit="submit"
          :placeholder="'Re-enter your password'"
        />
        <button
          :disabled="loading"
          type="submit"
          class="w-full bg-blue-600 text-white rounded py-2 hover:bg-blue-700 transition duration-150"
        >
          <div v-if="loading" class="flex items-center justify-center">
            <font-awesome-icon icon="spinner" class="animate-spin mr-2" />
            Loading...
          </div>

          Sign Up
        </button>
        <div class="flex items-center justify-center mt-4">
          <span class="text-gray-500">or</span>
          <button
            type="button"
            class="ml-2 bg-white border border-gray-300 rounded px-4 py-2 flex items-center hover:bg-gray-100 transition duration-150"
          >
            <font-awesome-icon
              :icon="['fa-brands', 'fa-google']"
              class="w-5 h-5 mr-2"
            />
            Sign Up with Google
          </button>
        </div>
      </form>
      <p class="mt-4 text-center">
        Already have an account?
        <NuxtLink to="/login" class="text-blue-600 hover:underline"
          >Login</NuxtLink
        >
      </p>
    </div>
  </div>
</template>
<script lang="ts" setup>
const submit = ref(false);
const email = ref("");
const password = ref("");
const confirmPassword = ref("");
const username = ref("");
const firstName = ref("");
const lastName = ref("");
const address = ref("");
const gender = ref("");
const birthDate = ref("");
const phoneNumber = ref("");

const signUpMessage = ref({
  message: "",
  isSuccess: false,
});

const formatEmailError = computed(() => {
  if (!submit.value) {
    return "";
  }

  if (!email.value) {
    return "Email is required";
  }

  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailPattern.test(email.value)) {
    return "Invalid email format";
  }
  return "";
});

const passwordRequirements = ref(false);

const passwordIsConfirmed = computed(() => {
  return password.value === confirmPassword.value;
});

const allInputRequired = computed(() => {
  return (
    email.value &&
    password.value &&
    passwordRequirements.value &&
    passwordIsConfirmed.value
  );
});

const handleSignUp = () => {
  submit.value = true;

  if (!allInputRequired.value) {
    return;
  }
  // Handle sign up logic here
  console.log("Signing up with:", email.value, password.value);
  sendSignUp();
  signUpMessage.value.message = email.value + " | " + password.value;
  signUpMessage.value.isSuccess = true;
  console.log("Sign Up Message:", signUpMessage.value);
};

const { data: lol, loading, error, postData } = useApi("auth/signup");

const sendSignUp = async () => {
  console.log("Sending sign up data for:", email.value);
  const response = await postData({
    email: email.value,
    password: password.value,
    username: username.value,
    firstName: firstName.value,
    lastName: lastName.value,
    address: address.value,
    gender: gender.value,
    birthDate: birthDate.value,
    phoneNumber: phoneNumber.value,
  });
  if (response.isSuccess) {
    console.log("Sign up successful:", response.data);
    signUpMessage.value.message = "Sign up successful!";
    signUpMessage.value.isSuccess = true;
  } else {
    signUpMessage.value.message = "Sign up failed: " + response.message;
    signUpMessage.value.isSuccess = false;
  }
};
</script>
