<template>
  <div
    v-if="activeSection === 'profile' && user && tempUser"
    class="profile-content"
  >
    <div
      v-if="isOwnProfile"
      class="mb-4 px-6 py-3 bg-blue-50 text-blue-700 rounded-lg"
    >
      <p class="text-sm">You are viewing your own profile.</p>
    </div>

    <ProfileHeader
      ref="profileHeader"
      :user="user"
      :is-user-logged-in="isUserLoggedFetched"
      :can-modify="canModify"
      @update:description="emit('update:tempUser', { description: $event })"
      @update:image="handleImageUpdate"
    />

    <SaveActionButtons
      v-if="hasChanges || saved"
      :loading="loadingUpdate"
      :saved="saved"
      @save="emit('save')"
      @reset="resetAllFields"
    />

    <ProfileInformation
      :user="user"
      :temp-user="tempUser"
      :can-modify="canModify"
      @update:first-name="emit('update:tempUser', { firstName: $event })"
      @update:last-name="emit('update:tempUser', { lastName: $event })"
      @update:username="emit('update:tempUser', { username: $event })"
      @update:birth-date="emit('update:tempUser', { birthDate: $event })"
      @update:experience="
        emit('update:tempUser', {
          providerInformation: {
            ...tempUser.providerInformation,
            experience: $event,
          },
        })
      "
    />

    <ProfileContactInformation
      :user="user"
      :temp-user="tempUser"
      :can-modify="canModify"
      @update:email="emit('update:tempUser', { email: $event })"
      @update:phone-number="emit('update:tempUser', { phoneNumber: $event })"
      @update:address="emit('update:tempUser', { address: $event })"
    />

    <ProfileSecuritySettings
      :user="user"
      :temp-user="tempUser"
      :can-modify="canModify"
      :user-id="userId"
    />

    <ProfileAccountSettings
      :can-modify="canModify"
      :loading-delete="loadingDelete"
      @delete-account="emit('delete-account')"
    />
  </div>
</template>

<script lang="ts" setup>
import type { ProfileHeader } from "#components";
import type { User } from "~/model/user";

defineProps<{
  activeSection: string;
  user: User | null;
  tempUser: User | null;
  isOwnProfile: boolean;
  isUserLoggedFetched: boolean;
  hasChanges: boolean;
  saved: boolean;
  loadingUpdate: boolean;
  canModify: boolean;
  userId: string;
  loadingDelete: boolean;
}>();

const emit = defineEmits<{
  (e: "update:description", value: string): void;
  (e: "update:tempUser", value: Partial<User>): void;
  (e: "save" | "reset" | "delete-account"): void;
  (e: "update:image", value: File): void;
}>();

const profileHeader = ref<InstanceType<typeof ProfileHeader> | null>(null);

const resetAllFields = () => {
  if (profileHeader.value) {
    profileHeader.value.resetDescription();
  }
  emit("reset");
};

const handleImageUpdate = (file: File) => {
  emit("update:image", file);
};
</script>
