<template>
  <section class="py-16 px-4">
    <div class="container mx-auto">
      <!-- Hero Section -->
      <div class="text-center mb-16">
        <h1 class="text-4xl md:text-5xl font-bold text-gray-900 mb-4">
          About Presteo
        </h1>
        <p class="text-xl text-gray-600 max-w-3xl mx-auto">
          Revolutionizing home services through technology and trust
        </p>
      </div>

      <!-- Mission Section -->
      <div class="grid md:grid-cols-2 gap-12 items-center mb-20">
        <div class="space-y-6">
          <h2 class="text-3xl font-bold text-gray-900">Our Mission</h2>
          <p class="text-lg text-gray-600">
            At Presteo, we're on a mission to transform the home services
            industry by connecting homeowners with trusted professionals through
            an innovative, user-friendly platform. We believe that finding and
            booking home services should be as simple as ordering takeout.
          </p>
          <p class="text-lg text-gray-600">
            Our commitment to quality, transparency, and customer satisfaction
            drives everything we do. We're not just a booking platform – we're
            your partner in maintaining and improving your home.
          </p>
        </div>
        <div class="relative">
          <img
            src="https://images.unsplash.com/photo-1556911220-bff31c812dba?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80"
            alt="Team collaboration"
            class="rounded-2xl shadow-xl"
          />
        </div>
      </div>

      <!-- Values Section -->
      <div class="bg-gray-50 rounded-3xl p-12 mb-20">
        <h2 class="text-3xl font-bold text-center mb-12">Our Core Values</h2>
        <div class="grid md:grid-cols-3 gap-8">
          <div class="text-center">
            <div
              class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-6"
            >
              <font-awesome-icon
                :icon="['fas', 'heart']"
                class="text-blue-600 text-2xl"
              />
            </div>
            <h3 class="text-xl font-semibold mb-3">Customer First</h3>
            <p class="text-gray-600">
              We prioritize customer satisfaction in every decision we make,
              ensuring exceptional service at every touchpoint.
            </p>
          </div>
          <div class="text-center">
            <div
              class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-6"
            >
              <font-awesome-icon
                :icon="['fas', 'handshake']"
                class="text-blue-600 text-2xl"
              />
            </div>
            <h3 class="text-xl font-semibold mb-3">Trust & Transparency</h3>
            <p class="text-gray-600">
              We build trust through complete transparency in pricing, service
              quality, and provider verification.
            </p>
          </div>
          <div class="text-center">
            <div
              class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-6"
            >
              <font-awesome-icon
                :icon="['fas', 'lightbulb']"
                class="text-blue-600 text-2xl"
              />
            </div>
            <h3 class="text-xl font-semibold mb-3">Innovation</h3>
            <p class="text-gray-600">
              We continuously innovate to make home services more accessible,
              efficient, and enjoyable for everyone.
            </p>
          </div>
        </div>
      </div>

      <!-- Team Section -->
      <div class="mb-20">
        <h2 class="text-3xl font-bold text-center mb-12">Our Team</h2>
        <div class="grid md:grid-cols-2 gap-8 max-w-4xl mx-auto">
          <div class="text-center">
            <img
              src="https://api.dicebear.com/9.x/adventurer/svg?seed=Oliver&flip=true&earrings[]&earringsProbability=100&eyebrows=variant01,variant04,variant05,variant06,variant07,variant08,variant09,variant10,variant11,variant12,variant13,variant14,variant15,variant02&hair=long03,long04,long05,long06,long07,long08,long09,long10,long11,long12,long13,long14,long15,long16,long17,long18,long19,long20,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19&hairColor=0e0e0e&backgroundColor=b6e3f4"
              alt="Florian MANGIN"
              class="w-48 h-48 rounded-full mx-auto mb-4 object-cover"
            />
            <h3 class="text-xl font-semibold">Florian MANGIN</h3>
            <p class="text-gray-600">Engineering Student</p>
          </div>
          <div class="text-center">
            <img
              src="https://api.dicebear.com/9.x/adventurer/svg?seed=Vivian&earrings[]&earringsProbability=100&eyebrows=variant01,variant04,variant05,variant06,variant07,variant08,variant09,variant10,variant11,variant12,variant13,variant14,variant15,variant02&hair=long14&hairColor=3eac2c&skinColor=ecad80,f2d3b1&backgroundColor=b6e3f4"
              alt="Mathis FENOT"
              class="w-48 h-48 rounded-full mx-auto mb-4 object-cover"
            />
            <h3 class="text-xl font-semibold">Mathis FENOT</h3>
            <p class="text-gray-600">Engineering Student</p>
          </div>
        </div>
        <div class="text-center mt-12">
          <p class="text-lg text-gray-600 max-w-2xl mx-auto">
            Project developed as part of our engineering school curriculum,
            showcasing our passion for innovation and web development.
          </p>
        </div>
      </div>

      <!-- Stats Section -->
      <div class="bg-blue-600 text-white rounded-3xl p-12 mb-20">
        <div class="grid md:grid-cols-3 gap-8 text-center">
          <div>
            <div class="text-4xl font-bold mb-2">2</div>
            <p>Passionate Students</p>
          </div>
          <div>
            <div class="text-4xl font-bold mb-2">1</div>
            <p>Innovative Project</p>
          </div>
          <div>
            <div class="text-4xl font-bold mb-2">100%</div>
            <p>Dedication</p>
          </div>
        </div>
      </div>

      <!-- CTA Section -->
      <div class="text-center">
        <h2 class="text-3xl font-bold mb-6">Join Our Journey</h2>
        <p class="text-xl text-gray-600 mb-8">
          Be part of our mission to transform home services
        </p>
        <div class="flex flex-col sm:flex-row gap-4 justify-center">
          <NuxtLink
            to="/services"
            class="bg-blue-600 text-white px-8 py-4 rounded-full text-lg font-semibold hover:bg-blue-700 transition-all"
          >
            Find a Service
          </NuxtLink>
          <NuxtLink
            to="/signup"
            class="bg-white text-blue-600 border-2 border-blue-600 px-8 py-4 rounded-full text-lg font-semibold hover:bg-blue-50 transition-all"
          >
            Join as Provider
          </NuxtLink>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
useHead({
  title: "About Us | Presteo",
});
</script>
