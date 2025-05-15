<template>
  <ProfileInfoCard
    v-if="canModify"
    title="Security Settings"
    subtitle="Manage your security settings"
  >
    <InfoRow
      label="Password"
      icon="fa-solid fa-lock"
      value="••••••••"
      action
      badge
      badge-classes="bg-red-100 text-red-800 rounded-md"
    >
      <template #form>
        <div class="flex justify-between items-center">
          <p class="text-sm text-gray-500">
            To change your password, please click on the link on the right.
          </p>
          <div class="flex">
            <NuxtLink
              :to="`/user/${userId}/change-password`"
              class="text-blue-600 flex items-center space-x-1 border px-2 py-1 rounded-lg border-blue-600 hover:bg-blue-50 font-medium"
            >
              <span>Change Password</span>
              <font-awesome-icon icon="fa-solid fa-external-link-alt" />
            </NuxtLink>
          </div>
        </div>
      </template>
    </InfoRow>
  </ProfileInfoCard>
</template>

<script lang="ts" setup>
import type { User } from "~/model/user";

defineProps<{
  user: User;
  tempUser: User;
  canModify: boolean;
  userId: string;
}>();
</script>
