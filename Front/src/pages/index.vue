<template>
  <section id="hero" class="pt-12 pb-20 px-4">
    <div class="container mx-auto">
      <div class="grid lg:grid-cols-2 gap-12 items-center">
        <div class="space-y-8">
          <h1
            class="text-4xl md:text-5xl lg:text-6xl font-bold text-gray-900 leading-tight"
          >
            Home Services, <span class="text-blue-600">Simplified</span> and
            Trustworthy
          </h1>
          <p class="text-xl text-gray-600 leading-relaxed">
            Connect with verified service providers for all your home
            maintenance needs. Book appointments, track services, and pay
            securely - all in one platform.
          </p>
          <div class="flex flex-col sm:flex-row gap-4">
            <NuxtLink
              to="/services"
              class="bg-blue-600 text-white px-8 py-4 rounded-full text-lg font-semibold hover:bg-blue-700 transition-all"
            >
              Find a Provider Now
            </NuxtLink>
            <button
              class="bg-gray-100 text-gray-700 px-8 py-4 rounded-full text-lg font-semibold hover:bg-gray-200 transition-all"
            >
              Learn More
            </button>
          </div>

          <!-- Features -->
          <div id="features" class="grid grid-cols-2 gap-6 mt-12">
            <div class="flex items-start space-x-3">
              <font-awesome-icon
                :icon="['fas', 'calendar-check']"
                class="text-blue-600 text-xl mt-2 w-6"
              />
              <div>
                <h3 class="font-semibold">Real-time Booking</h3>
                <p class="text-gray-600">Schedule services instantly</p>
              </div>
            </div>
            <div class="flex items-start space-x-3">
              <i class="text-blue-600 text-xl mt-1 w-6" data-fa-i2svg=""
                ><svg
                  class="svg-inline--fa fa-shield-check"
                  aria-hidden="true"
                  focusable="false"
                  data-prefix="fas"
                  data-icon="shield-check"
                  role="img"
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 512 512"
                  data-fa-i2svg=""
                >
                  <g class="missing">
                    <path
                      fill="currentColor"
                      d="M156.5,447.7l-12.6,29.5c-18.7-9.5-35.9-21.2-51.5-34.9l22.7-22.7C127.6,430.5,141.5,440,156.5,447.7z M40.6,272H8.5 c1.4,21.2,5.4,41.7,11.7,61.1L50,321.2C45.1,305.5,41.8,289,40.6,272z M40.6,240c1.4-18.8,5.2-37,11.1-54.1l-29.5-12.6 C14.7,194.3,10,216.7,8.5,240H40.6z M64.3,156.5c7.8-14.9,17.2-28.8,28.1-41.5L69.7,92.3c-13.7,15.6-25.5,32.8-34.9,51.5 L64.3,156.5z M397,419.6c-13.9,12-29.4,22.3-46.1,30.4l11.9,29.8c20.7-9.9,39.8-22.6,56.9-37.6L397,419.6z M115,92.4 c13.9-12,29.4-22.3,46.1-30.4l-11.9-29.8c-20.7,9.9-39.8,22.6-56.8,37.6L115,92.4z M447.7,355.5c-7.8,14.9-17.2,28.8-28.1,41.5 l22.7,22.7c13.7-15.6,25.5-32.9,34.9-51.5L447.7,355.5z M471.4,272c-1.4,18.8-5.2,37-11.1,54.1l29.5,12.6 c7.5-21.1,12.2-43.5,13.6-66.8H471.4z M321.2,462c-15.7,5-32.2,8.2-49.2,9.4v32.1c21.2-1.4,41.7-5.4,61.1-11.7L321.2,462z M240,471.4c-18.8-1.4-37-5.2-54.1-11.1l-12.6,29.5c21.1,7.5,43.5,12.2,66.8,13.6V471.4z M462,190.8c5,15.7,8.2,32.2,9.4,49.2h32.1 c-1.4-21.2-5.4-41.7-11.7-61.1L462,190.8z M92.4,397c-12-13.9-22.3-29.4-30.4-46.1l-29.8,11.9c9.9,20.7,22.6,39.8,37.6,56.9 L92.4,397z M272,40.6c18.8,1.4,36.9,5.2,54.1,11.1l12.6-29.5C317.7,14.7,295.3,10,272,8.5V40.6z M190.8,50 c15.7-5,32.2-8.2,49.2-9.4V8.5c-21.2,1.4-41.7,5.4-61.1,11.7L190.8,50z M442.3,92.3L419.6,115c12,13.9,22.3,29.4,30.5,46.1 l29.8-11.9C470,128.5,457.3,109.4,442.3,92.3z M397,92.4l22.7-22.7c-15.6-13.7-32.8-25.5-51.5-34.9l-12.6,29.5 C370.4,72.1,384.4,81.5,397,92.4z"
                    />
                    <circle fill="currentColor" cx="256" cy="364" r="28">
                      <animate
                        attributeType="XML"
                        repeatCount="indefinite"
                        dur="2s"
                        attributeName="r"
                        values="28;14;28;28;14;28;"
                      />
                      <animate
                        attributeType="XML"
                        repeatCount="indefinite"
                        dur="2s"
                        attributeName="opacity"
                        values="1;0;1;1;0;1;"
                      />
                    </circle>
                    <path
                      fill="currentColor"
                      opacity="1"
                      d="M263.7,312h-16c-6.6,0-12-5.4-12-12c0-71,77.4-63.9,77.4-107.8c0-20-17.8-40.2-57.4-40.2c-29.1,0-44.3,9.6-59.2,28.7 c-3.9,5-11.1,6-16.2,2.4l-13.1-9.2c-5.6-3.9-6.9-11.8-2.6-17.2c21.2-27.2,46.4-44.7,91.2-44.7c52.3,0,97.4,29.8,97.4,80.2 c0,67.6-77.4,63.5-77.4,107.8C275.7,306.6,270.3,312,263.7,312z"
                    >
                      <animate
                        attributeType="XML"
                        repeatCount="indefinite"
                        dur="2s"
                        attributeName="opacity"
                        values="1;0;0;0;0;1;"
                      />
                    </path>
                    <path
                      fill="currentColor"
                      opacity="0"
                      d="M232.5,134.5l7,168c0.3,6.4,5.6,11.5,12,11.5h9c6.4,0,11.7-5.1,12-11.5l7-168c0.3-6.8-5.2-12.5-12-12.5h-23 C237.7,122,232.2,127.7,232.5,134.5z"
                    >
                      <animate
                        attributeType="XML"
                        repeatCount="indefinite"
                        dur="2s"
                        attributeName="opacity"
                        values="0;0;1;1;0;0;"
                      />
                    </path>
                  </g></svg
              ></i>
              <div>
                <h3 class="font-semibold">Secure Payments</h3>
                <p class="text-gray-600">Multiple payment options</p>
              </div>
            </div>
            <div class="flex items-start space-x-3">
              <font-awesome-icon
                :icon="['fas', 'star']"
                class="text-blue-600 text-xl mt-2 w-6"
              />
              <div>
                <h3 class="font-semibold">Verified Providers</h3>
                <p class="text-gray-600">Trusted professionals</p>
              </div>
            </div>
            <div class="flex items-start space-x-3">
              <font-awesome-icon
                :icon="['fas', 'location-dot']"
                class="text-blue-600 text-xl mt-2 w-6"
              />
              <div>
                <h3 class="font-semibold">Service Tracking</h3>
                <p class="text-gray-600">Real-time updates</p>
              </div>
            </div>
          </div>
        </div>

        <div class="relative">
          <img
            class="rounded-2xl shadow-2xl"
            src="https://cdn.shopify.com/s/files/1/0530/6636/7145/files/stratoss_craftsman_cutting_leather_in_a_white_and_very_clean_wo_c4a901cd-3452-45a7-b394-ec883790bfa5_1024x1024.png?v=1704449205"
            alt="professional plumber in blue uniform smiling while fixing sink, modern kitchen background, high quality professional photo"
          />
        </div>
      </div>

      <!-- Testimonials -->
      <div id="testimonials" class="mt-20">
        <h2 class="text-3xl font-bold text-center mb-12">Project Feedback</h2>
        <div class="grid md:grid-cols-3 gap-8">
          <Testimonial
            v-for="(customer, index) in customers"
            :key="index"
            :Name="customer.Name"
            :stars="customer.stars"
            :message="customer.message"
          />
        </div>
      </div>

      <!-- About Project Section -->
      <div class="mt-20 bg-gray-50 rounded-3xl p-12">
        <div class="text-center mb-12">
          <h2 class="text-3xl font-bold text-gray-900 mb-4">
            About the Project
          </h2>
          <p class="text-xl text-gray-600 max-w-3xl mx-auto">
            A student project developed at ISEP, demonstrating our passion for
            web development and innovation
          </p>
        </div>
        <div class="grid md:grid-cols-3 gap-8">
          <div class="text-center">
            <font-awesome-icon
              :icon="['fas', 'code']"
              class="text-blue-600 text-4xl mb-4"
            />
            <h3 class="text-xl font-semibold mb-2">Modern Tech Stack</h3>
            <p class="text-gray-600">
              Built with Nuxt.js, TypeScript, and Tailwind CSS
            </p>
          </div>
          <div class="text-center">
            <font-awesome-icon
              :icon="['fas', 'graduation-cap']"
              class="text-blue-600 text-4xl mb-4"
            />
            <h3 class="text-xl font-semibold mb-2">Academic Project</h3>
            <p class="text-gray-600">
              Part of our engineering curriculum at ISEP
            </p>
          </div>
          <div class="text-center">
            <font-awesome-icon
              :icon="['fas', 'users']"
              class="text-blue-600 text-4xl mb-4"
            />
            <h3 class="text-xl font-semibold mb-2">Team Work</h3>
            <p class="text-gray-600">
              Developed by Florian MANGIN and Mathis FENOT
            </p>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
useHead({
  title: "Home | Presteo",
});
interface TerimonialCustomer {
  Name: string;
  stars: number;
  message: string;
}
const customers: TerimonialCustomer[] = [
  {
    Name: "Project Supervisor",
    stars: 5,
    message:
      "An impressive demonstration of modern web development skills. The team has shown great attention to detail and user experience.",
  },
  {
    Name: "Technical Reviewer",
    stars: 4,
    message:
      "Well-structured codebase with clean architecture. The use of TypeScript and modern frameworks shows good engineering practices.",
  },
  {
    Name: "UX Expert",
    stars: 5,
    message:
      "The interface is intuitive and user-friendly. Great job on implementing responsive design and accessibility features.",
  },
];
</script>
