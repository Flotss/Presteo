<template>
  <div>
    <Header />
    <div class="flex-grow">
      <article class="flex items-center justify-center my-40">
        <section class="max-w-md text-center">
          <h1 class="text-5xl font-bold mb-4">Oops!</h1>
          <p class="text-gray-500 mb-4">{{ errorMessage }}</p>
          <NuxtLink
            to="/"
            class="inline-block py-2 px-4 border border-transparent text-base font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700"
          >
            Go back home
          </NuxtLink>
        </section>
      </article>
    </div>
    <Footer />
  </div>
</template>

<script setup>
const props = defineProps({
  error: {
    type: Object,
    required: true,
  },
});

const errorMessage = computed(() => {
  if (props.error?.statusText) {
    return `${props.error.statusText}: ${
      props.error.stack || props.error.message || ""
    }`;
  }
  return props.error?.message || "Error occurred";
});
</script>