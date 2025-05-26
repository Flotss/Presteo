<template>
  <div class="max-w-3xl mx-auto p-8 bg-white rounded-lg shadow-md mt-12 mb-12">
    <h1 class="text-4xl font-extrabold text-center text-gray-900 mb-6">
      Welcome to the Presteo Back Office
    </h1>
    <p class="text-lg text-center text-gray-600 mb-8">
      Manage users, handle reports, and configure the platform from this admin-only area.
    </p>
    <div class="grid grid-cols-1 md:grid-cols-2 gap-8 mt-8">
      <NuxtLink to="/admin/users" class="block p-6 bg-blue-50 rounded-lg shadow hover:bg-blue-100 transition">
        <div class="flex items-center mb-2">
          <font-awesome-icon :icon="['fas', 'user-cog']" class="text-2xl mr-2 text-blue-700" />
          <h2 class="text-2xl font-bold text-blue-700">User Management</h2>
        </div>
        <p class="text-gray-600">View, edit, or delete user accounts.</p>
      </NuxtLink>
      <div class="block p-6 bg-yellow-50 rounded-lg shadow opacity-60 cursor-not-allowed">
        <div class="flex items-top mb-2">
          <font-awesome-icon :icon="['fas', 'flag']" class="text-2xl mr-2 text-yellow-700" />
          <h2 class="text-2xl font-bold text-yellow-700">Reports Management (soon)</h2>
        </div>
        <p class="text-gray-600">Review and handle user reports and flagged content.</p>
      </div>
      <div class="block p-6 bg-gray-50 rounded-lg shadow opacity-60 cursor-not-allowed">
        <div class="flex items-center mb-2">
          <font-awesome-icon :icon="['fas', 'cogs']" class="text-2xl mr-2 text-gray-700" />
          <h2 class="text-2xl font-bold text-gray-700">Settings (soon)</h2>
        </div>
        <p class="text-gray-600">Configure advanced application settings.</p>
      </div>
      <NuxtLink to="/" class="block p-6 bg-green-50 rounded-lg shadow hover:bg-green-100 transition">
        <div class="flex items-center mb-2">
          <font-awesome-icon :icon="['fas', 'home']" class="text-2xl mr-2 text-green-700" />
          <h2 class="text-2xl font-bold text-green-700">Back to Site</h2>
        </div>
        <p class="text-gray-600">Return to the public Presteo homepage.</p>
      </NuxtLink>
    </div>
  </div>
</template>

<script setup>
definePageMeta({
  layout: 'admin',
  middleware: 'auth',
});
useHead({
  title: 'Admin Dashboard | Presteo',
});
</script> 