<template>
  <div class="w-full md:w-96 bg-gray-50 rounded-lg p-6 border border-gray-100">
    <div class="flex items-center mb-4">
      <div class="relative w-12 h-12 rounded-full overflow-hidden mr-4">
        <img
          v-if="provider.profileImageUrl"
          :src="provider.profileImageUrl"
          alt="Provider profile image"
          class="w-full h-full object-cover"
        />
        <NuxtImg
          v-else
          :src="'https://api.dicebear.com/9.x/dylan/svg?seed=' + provider.id"
          alt="Provider avatar"
          class="w-full h-full object-cover"
        />
      </div>
      <div>
        <p class="text-lg font-medium text-gray-900">
          {{ provider.firstName }} {{ provider.lastName }}
        </p>
        <p class="text-sm text-gray-600">{{ provider.email }}</p>
      </div>
    </div>
    <div class="space-y-4">
      <div class="flex items-center space-x-1">
        <template v-if="provider.reviewCount && provider.reviewCount > 0">
          <font-awesome-icon
            v-for="i in 5"
            :key="i"
            :icon="[
              i <= Math.floor(provider.averageRating ?? 0)
                ? 'fas'
                : i - (provider.averageRating ?? 0) <= 0.5 &&
                  i - (provider.averageRating ?? 0) > 0
                ? 'fas'
                : 'far',
              i <= Math.floor(provider.averageRating ?? 0)
                ? 'star'
                : i - (provider.averageRating ?? 0) <= 0.5 &&
                  i - (provider.averageRating ?? 0) > 0
                ? 'star-half-alt'
                : 'star',
            ]"
            class="h-5 w-5 text-yellow-400"
          />
          <span class="ml-2 text-sm text-gray-600">
            {{ provider.averageRating ?? "N/A" }}
            <span>({{ provider.reviewCount }} reviews)</span>
          </span>
        </template>
        <template v-else>
          <span class="text-sm text-gray-500">No reviews yet</span>
        </template>
      </div>
      <div v-if="provider.providerInformation" class="mt-2">
        <p class="text-sm text-gray-600">
          <font-awesome-icon
            :icon="['fas', 'briefcase']"
            class="h-4 w-4 mr-2 text-blue-600"
          />
          {{ provider.providerInformation.experience }}
        </p>
      </div>
    </div>
    <div v-if="canReport" class="mt-4 flex justify-end">
      <NuxtLink
        :to="`/report/provider/${provider.id}`"
        class="inline-flex items-center text-xs text-gray-400 hover:text-red-600 hover:underline transition"
        style="gap: 0.25rem"
      >
        <font-awesome-icon :icon="['fas', 'flag']" class="w-3 h-3" />
        Report
      </NuxtLink>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { User } from "~/model/user";
import { useAuthStore } from "~/stores/auth";
import { computed } from "vue";
import type { Review } from "~/model/Review";

const props = defineProps<{
  provider: User;
  reviews: Review[];
}>();

const authStore = useAuthStore();
const canReport = computed(() => {
  return authStore.user && authStore.user.id !== props.provider.id;
});
</script>
