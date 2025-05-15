<template>
  <ProfileInfoCard title="Contact Information" subtitle="Your contact details">
    <InfoRow
      label="Email Address"
      icon="fa-solid fa-envelope"
      :value="user.email"
      :action="true"
    >
      <template #form>
        <div class="space-y-4">
          <BasicInput
            v-if="canModify"
            id="email"
            label="Email Address"
            type="email"
            placeholder="Enter email address"
            format="\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*"
            format-error="Format should be like jean.dupont@domain.com"
            :model-value="tempUser.email"
            @update:model-value="$emit('update:email', $event)"
          />
          <UnauthorizedInfo v-else />
        </div>
      </template>
    </InfoRow>
    <InfoRow
      label="Phone number"
      icon="fa-solid fa-phone"
      :value="user.phoneNumber"
      :action="true"
    >
      <template #form>
        <div class="space-y-4">
          <BasicInput
            v-if="canModify"
            id="phoneNumber"
            label="Phone Number"
            type="tel"
            placeholder="Enter phone number"
            format="^[0-9]{10}$"
            format-error="Format should be like 0612345678"
            :model-value="tempUser.phoneNumber"
            @update:model-value="$emit('update:phoneNumber', $event)"
          />
          <UnauthorizedInfo v-else />
        </div>
      </template>
    </InfoRow>
    <InfoRow
      label="Address"
      icon="fa-solid fa-map-marker-alt"
      :value="user.address"
      :action="true"
    >
      <template #form>
        <div class="space-y-4">
          <BasicInput
            v-if="canModify"
            id="address"
            label="Address"
            placeholder="Enter address"
            :model-value="tempUser.address"
            @update:model-value="$emit('update:address', $event)"
          />
          <UnauthorizedInfo v-else />
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
}>();

defineEmits<{
  (
    e: "update:email" | "update:phoneNumber" | "update:address",
    value: string
  ): void;
}>();
</script>
