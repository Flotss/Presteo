<template>
  <div class="p-4">
    <h1 class="text-2xl font-bold mb-4">Test Page</h1>
    <p class="mb-4">Counter: {{ counter }}</p>
    <button @click="incrementCounter" class="bg-blue-500 text-white px-4 py-2 rounded">Increment</button>
  </div>

</template>

<script setup>
import { ref } from "vue";

const counter = ref(0);

const incrementCounter = () => {
  counter.value++;
}
</script>

<style scoped>
button {
  margin-top: 10px;
}
</style>
