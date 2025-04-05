<template>
  <div
    class="flex flex-col justify-center items-center bg-gray-100 min-h-screen"
  >
    <div
      class="container border rounded-lg shadow-lg bg-white p-8 max-w-md sm:max-w-lg lg:max-w-xl mx-auto my-4"
    >
      <h1 class="text-3xl font-bold text-center mb-6">Sign Up</h1>

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
          type="submit"
          class="w-full bg-blue-600 text-white rounded py-2 hover:bg-blue-700 transition duration-150"
          :class="{
            'bg-green-400': signUpMessage.isSuccess && !loading,
            'bg-blue-600': allInputRequired && !signUpMessage.isSuccess,
            'bg-gray-400': !allInputRequired && !signUpMessage.isSuccess,
          }"
          :disabled="loading"
        >
          <span v-if="loading" class="flex justify-center items-center">
            <font-awesome-icon
              :icon="['fas', 'circle-notch']"
              class="animate-spin"
            />
          </span>
          <span
            v-else-if="signUpMessage.isSuccess"
            class="flex justify-center items-center"
          >
            <font-awesome-icon :icon="['fas', 'check']" class="text-black" />
          </span>
          <span v-else> Sign Up </span>
        </button>
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
        <div v-else class="flex items-center justify-center mt-4">
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
const email = ref("test@example.com");
const password = ref("Test@1234");
const confirmPassword = ref("Test@1234");
const username = ref("testuser");
const firstName = ref("John");
const lastName = ref("Doe");
const address = ref(
  "123 Main St, Springfield123 Main St, Springfield123 Main St, Springfield123 Main St, Springfield123 Main St, Springfield123 Main St, Springfield123 Main St, Springfield123 Main St, Springfield123 Main St, Springfield123 Main S"
);
const gender = ref("male");
const birthDate = ref("1990-01-01");
const phoneNumber = ref("1234567890");

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

  sendSignUp();
};

const { loading, postData } = useApi("auth/signup");

const sendSignUp = async () => {
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
  if (response == 'User registered successfully!') {
    signUpMessage.value.message = "Sign up successful!";
    signUpMessage.value.isSuccess = true;

    setTimeout(() => {
      signUpMessage.value.message = "Redirecting in 1 second...";
    }, 1000);

    setTimeout(() => {
      const router = useRouter();
      router.push("/login");
    }, 2000);
  } else {
    console.error("Sign up failed:", response);
    signUpMessage.value.message = "Sign up failed, please try again.";
    signUpMessage.value.isSuccess = false;
  }
};
</script>
