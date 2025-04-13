<template>
  <div>
    <div
      class="info-row px-6 py-4 flex items-center justify-between"
      @click="action ? (isOpen = !isOpen) : null"
    >
      <div class="info-label flex items-center">
        <font-awesome-icon :icon="icon" class="mr-3 text-gray-400" />
        <div class="font-medium text-gray-700">{{ label }}</div>
      </div>
      <div class="info-value flex items-center">
        <span v-if="!badge" class="text-gray-900">
          {{ value || "Not provided" }}
        </span>

        <span
          v-else
          class="px-2 py-1 text-sm font-medium rounded-full"
          :class="badgeClasses"
        >
          {{ value || "Not provided" }}
        </span>
        <font-awesome-icon
          v-if="action"
          :icon="
            isOpen ? 'fa-solid fa-chevron-down' : 'fa-solid fa-chevron-right'
          "
          class="ml-3 text-gray-400 text-xs"
        />
      </div>
    </div>

    <TransitionOpen :showContent="isOpen">
      <div class="px-6 py-4 bg-gray-50 border-t">
        <slot name="form"></slot>
      </div>
    </TransitionOpen>
  </div>
</template>

<script setup>
import { ref } from "vue";

const isOpen = ref(false);

defineProps({
  label: {
    type: String,
    required: true,
  },
  value: {
    type: [String, undefined],
    required: false,
    default: undefined,
  },
  action: {
    type: Boolean,
    default: false,
  },
  icon: {
    type: String,
    default: "",
  },
  badge: {
    type: Boolean,
    default: false,
  },
  badgeClasses: {
    type: String,
    default: "bg-blue-100 text-blue-800",
  },
});
</script>
