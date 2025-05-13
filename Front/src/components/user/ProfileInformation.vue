<template>
  <ProfileInfoCard
    title="Profile Information"
    subtitle="Basic details about your account"
  >
    <InfoRow
      label="Name"
      :value="`${user.firstName} ${user.lastName}`"
      :action="true"
      icon="fa-solid fa-user"
    >
      <template #form>
        <div v-if="canModify" class="flex space-x-4">
          <BasicInput
            id="firstName"
            :model-value="tempUser.firstName"
            label="First Name"
            placeholder="Enter first name"
            class="flex-1"
            @update:model-value="$emit('update:firstName', $event)"
          />
          <BasicInput
            id="lastName"
            :model-value="tempUser.lastName"
            label="Last Name"
            placeholder="Enter last name"
            class="flex-1"
            @update:model-value="$emit('update:lastName', $event)"
          />
        </div>
        <UnauthorizedInfo v-else />
      </template>
    </InfoRow>
    <InfoRow
      label="Username"
      :value="user.username"
      :action="true"
      icon="fa-solid fa-at"
    >
      <template #form>
        <div class="space-y-4">
          <BasicInput
            v-if="canModify"
            id="username"
            :model-value="tempUser.username"
            label="Username"
            placeholder="Enter username"
            @update:model-value="$emit('update:username', $event)"
          />
          <UnauthorizedInfo v-else />
        </div>
      </template>
    </InfoRow>
    <InfoRow
      v-if="user.role?.name === RoleType.PROVIDER && user.providerInformation"
      label="Experience"
      :value="user.providerInformation.experience?.substring(0, 20) + '...'"
      :action="true"
      icon="fa-solid fa-briefcase"
    >
      <template #form>
        <div class="space-y-4">
          <BasicInput
            v-if="canModify && tempUser.providerInformation"
            id="experience"
            :model-value="tempUser.providerInformation.experience"
            label="Experience"
            placeholder="Enter experience"
            @update:model-value="$emit('update:experience', $event)"
          />
          <UnauthorizedInfo v-else />
        </div>
      </template>
    </InfoRow>
    <InfoRow
      label="Birth Date"
      :value="user.birthDate"
      :action="true"
      icon="fa-solid fa-calendar"
    >
      <template #form>
        <div class="space-y-4">
          <BasicInput
            v-if="canModify"
            id="birthDate"
            :model-value="tempUser.birthDate"
            label="Birth Date"
            type="date"
            class="w-40"
            @update:model-value="$emit('update:birthDate', $event)"
          />
          <UnauthorizedInfo v-else />
        </div>
      </template>
    </InfoRow>
    <InfoRow
      label="Type of account"
      :value="user.role?.name"
      :badge="true"
      icon="fa-solid fa-user-tag"
    />
  </ProfileInfoCard>
</template>

<script lang="ts" setup>
import { RoleType } from "~/model/roleType";
import type { User } from "~/model/user";
import InfoRow from "~/components/user/InfoRow.vue";
import BasicInput from "~/components/user/BasicInput.vue";
import UnauthorizedInfo from "~/components/user/UnauthorizedInfo.vue";
import ProfileInfoCard from "~/components/user/ProfileInfoCard.vue";

defineProps<{
  user: User;
  tempUser: User;
  canModify: boolean;
}>();

defineEmits<{
  (
    e:
      | "update:firstName"
      | "update:lastName"
      | "update:username"
      | "update:birthDate"
      | "update:experience",
    value: string
  ): void;
}>();
</script>
