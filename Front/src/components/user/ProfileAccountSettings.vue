<template>
  <ProfileInfoCard
    v-if="canModify"
    title="Account Settings"
    subtitle="Manage your account preferences"
  >
    <div class="flex justify-between items-center px-6 py-4">
      <p class="text-sm text-gray-500">
        Deleting your account is permanent and cannot be undone.
      </p>
      <div class="flex">
        <button
          class="text-red-600 flex items-center space-x-1 border px-2 py-1 rounded-lg border-red-600 hover:bg-red-50 font-medium"
          :disabled="loadingDelete"
          @click="$emit('delete-account')"
        >
          <span>Delete Account</span>
          <font-awesome-icon v-if="!loadingDelete" icon="fa-solid fa-trash" />
          <font-awesome-icon v-else icon="fa-solid fa-circle-notch" spin />
        </button>
      </div>
    </div>
  </ProfileInfoCard>
</template>

<script lang="ts" setup>
import ProfileInfoCard from "~/components/user/ProfileInfoCard.vue";

defineProps<{
  canModify: boolean;
  loadingDelete: boolean;
}>();

defineEmits<{
  (e: "delete-account"): void;
}>();
</script>
