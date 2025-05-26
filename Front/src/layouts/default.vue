<template>
  <div class="flex flex-col min-h-screen font-inter sans-serif">
    <Header />
    <div class="flex-grow">
      <NuxtPage />
    </div>
    <Footer />
  </div>
</template>

<script setup>
import Footer from '@/components/Footer.vue';
import Header from '@/components/Header.vue';
</script> 