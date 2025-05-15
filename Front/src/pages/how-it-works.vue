<template>
  <section class="py-16 px-4">
    <div class="container mx-auto">
      <!-- Header -->
      <div class="text-center mb-16">
        <h1 class="text-4xl md:text-5xl font-bold text-gray-900 mb-4">
          How Presteo Works
        </h1>
        <p class="text-xl text-gray-600 max-w-3xl mx-auto">
          Your journey to hassle-free home services in four simple steps
        </p>
      </div>

      <!-- Steps -->
      <div class="grid md:grid-cols-2 lg:grid-cols-4 gap-8 mb-16">
        <div
          class="bg-white p-8 rounded-2xl shadow-lg hover:shadow-xl transition-shadow"
        >
          <div
            class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mb-6"
          >
            <font-awesome-icon
              :icon="['fas', 'search']"
              class="text-blue-600 text-2xl"
            />
          </div>
          <h3 class="text-xl font-semibold mb-3">1. Find Your Service</h3>
          <p class="text-gray-600">
            Browse our extensive list of verified service providers. Filter by
            service type, location, and availability.
          </p>
        </div>

        <div
          class="bg-white p-8 rounded-2xl shadow-lg hover:shadow-xl transition-shadow"
        >
          <div
            class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mb-6"
          >
            <font-awesome-icon
              :icon="['fas', 'calendar-check']"
              class="text-blue-600 text-2xl"
            />
          </div>
          <h3 class="text-xl font-semibold mb-3">2. Book Instantly</h3>
          <p class="text-gray-600">
            Choose your preferred date and time. Our real-time booking system
            confirms your appointment instantly.
          </p>
        </div>

        <div
          class="bg-white p-8 rounded-2xl shadow-lg hover:shadow-xl transition-shadow"
        >
          <div
            class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mb-6"
          >
            <font-awesome-icon
              :icon="['fas', 'user-check']"
              class="text-blue-600 text-2xl translate-x-1"
            />
          </div>
          <h3 class="text-xl font-semibold mb-3">3. Service Delivery</h3>
          <p class="text-gray-600">
            Track your service provider's arrival in real-time. Receive
            notifications and updates throughout the process.
          </p>
        </div>

        <div
          class="bg-white p-8 rounded-2xl shadow-lg hover:shadow-xl transition-shadow"
        >
          <div
            class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mb-6"
          >
            <font-awesome-icon
              :icon="['fas', 'star']"
              class="text-blue-600 text-2xl"
            />
          </div>
          <h3 class="text-xl font-semibold mb-3">4. Rate & Review</h3>
          <p class="text-gray-600">
            Share your experience and help others make informed decisions. Your
            feedback matters to us.
          </p>
        </div>
      </div>

      <!-- Additional Features -->
      <div class="bg-gray-50 rounded-3xl p-12 mb-16">
        <h2 class="text-3xl font-bold text-center mb-12">
          Why Choose Presteo?
        </h2>
        <div class="grid md:grid-cols-3 gap-8">
          <div class="text-center">
            <font-awesome-icon
              :icon="['fas', 'shield']"
              class="text-blue-600 text-4xl mb-4"
            />
            <h3 class="text-xl font-semibold mb-2">Verified Professionals</h3>
            <p class="text-gray-600">
              All service providers are thoroughly vetted and background-checked
            </p>
          </div>
          <div class="text-center">
            <font-awesome-icon
              :icon="['fas', 'credit-card']"
              class="text-blue-600 text-4xl mb-4"
            />
            <h3 class="text-xl font-semibold mb-2">Secure Payments</h3>
            <p class="text-gray-600">
              Multiple payment options with secure transaction processing
            </p>
          </div>
          <div class="text-center">
            <font-awesome-icon
              :icon="['fas', 'headset']"
              class="text-blue-600 text-4xl mb-4"
            />
            <h3 class="text-xl font-semibold mb-2">24/7 Support</h3>
            <p class="text-gray-600">
              Round-the-clock customer support for all your needs
            </p>
          </div>
        </div>
      </div>

      <!-- CTA Section -->
      <div class="text-center">
        <h2 class="text-3xl font-bold mb-6">Ready to Get Started?</h2>
        <p class="text-xl text-gray-600 mb-8">
          Join thousands of satisfied customers who trust Presteo for their home
          services
        </p>
        <NuxtLink
          to="/services"
          class="bg-blue-600 text-white px-8 py-4 rounded-full text-lg font-semibold hover:bg-blue-700 transition-all inline-block"
        >
          Find a Service Provider
        </NuxtLink>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
useHead({
  title: "How Presteo Works | Presteo",
});
</script>
