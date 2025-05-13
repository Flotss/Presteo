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
      @update:description="tempUser.description = $event"
    />

    <SaveActionButtons
      v-if="hasChanges || saved"
      :loading="loadingUpdate"
      :saved="saved"
      @save="save"
      @reset="resetAllFields"
    />

    <ProfileInformation
      :user="user"
      :temp-user="tempUser"
      :can-modify="canModify"
      @update:first-name="tempUser.firstName = $event"
      @update:last-name="tempUser.lastName = $event"
      @update:username="tempUser.username = $event"
      @update:birth-date="tempUser.birthDate = $event"
      @update:experience="
        tempUser.providerInformation &&
          (tempUser.providerInformation.experience = $event)
      "
    />

    <ProfileContactInformation
      :user="user"
      :temp-user="tempUser"
      :can-modify="canModify"
      @update:email="tempUser.email = $event"
      @update:phone-number="tempUser.phoneNumber = $event"
      @update:address="tempUser.address = $event"
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
      @delete-account="deleteAccount"
    />
  </div>
</template>

<script lang="ts" setup>
import type { User } from "~/model/user";
import type { ProfileHeader } from "#components";

const props = defineProps<{
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
  (e: "save"): void;
  (e: "reset"): void;
  (e: "delete-account"): void;
}>();

const profileHeader = ref<InstanceType<typeof ProfileHeader> | null>(null);

const resetAllFields = () => {
  if (profileHeader.value) {
    profileHeader.value.resetDescription();
  }
  emit("reset");
};
</script>
