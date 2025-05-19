<template>
  <div class="service-profile-container max-w-4xl mx-auto p-4">
    <ProfileStatusMessages
      :loading="loading"
      :errorMessage="errorMessage"
      :infoMessage="infoMessage"
      :notFound="notFound"
    />

    <div v-if="service && tempService" class="profile-content">
      <ProfileHeader
        ref="profileHeader"
        :title="service.title"
        :subtitle="service.domain"
        :imageUrl="service.imageUrl || 'https://via.placeholder.com/800x400'"
      />

      <SaveActionButtons
        v-if="hasChanges || saved"
        :loading="loadingUpdate"
        :saved="saved"
        @save="save"
        @reset="resetAllFields"
      />

      <ProfileInfoCard
        title="Service Details"
        subtitle="Basic details about this service"
      >
        <InfoRow label="Title" :value="service.title" :action="true">
          <template #form>
            <div class="space-y-4">
              <BasicInput
                v-if="canModify"
                label="Title"
                v-model="tempService.title"
                placeholder="Enter service title"
                id="title"
              />
              <UnauthorizedInfo v-else />
            </div>
          </template>
        </InfoRow>
        <InfoRow label="Domain" :value="service.domain" :action="true">
          <template #form>
            <div class="space-y-4">
              <BasicInput
                v-if="canModify"
                label="Domain"
                v-model="tempService.domain"
                placeholder="Enter service domain"
                id="domain"
              />
              <UnauthorizedInfo v-else />
            </div>
          </template>
        </InfoRow>
        <InfoRow
          label="Description"
          :value="service.description"
          :action="true"
        >
          <template #form>
            <div class="space-y-4">
              <BasicInput
                v-if="canModify"
                label="Description"
                v-model="tempService.description"
                placeholder="Enter service description"
                id="description"
              />
              <UnauthorizedInfo v-else />
            </div>
          </template>
        </InfoRow>
        <InfoRow
          label="Price"
          :value="`${service.price} € / hour`"
          :action="true"
        >
          <template #form>
            <div class="space-y-4">
              <BasicInput
                v-if="canModify"
                label="Price"
                v-model="tempService.price"
                type="number"
                placeholder="Enter service price"
                id="price"
              />
              <UnauthorizedInfo v-else />
            </div>
          </template>
        </InfoRow>
        <InfoRow
          label="Duration"
          :value="`${service.duration} hours`"
          :action="true"
        >
          <template #form>
            <div class="space-y-4">
              <BasicInput
                v-if="canModify"
                label="Duration"
                v-model="tempService.duration"
                type="number"
                placeholder="Enter service duration"
                id="duration"
              />
              <UnauthorizedInfo v-else />
            </div>
          </template>
        </InfoRow>
      </ProfileInfoCard>

      <ProfileInfoCard
        title="Provider Information"
        subtitle="Details about the provider"
      >
        <InfoRow label="Provider Name" :value="service.providerName" />
        <InfoRow label="Provider Email" :value="service.providerEmail" />
      </ProfileInfoCard>

      <ProfileInfoCard
        title="Service Settings"
        subtitle="Manage your service settings"
        v-if="canModify"
      >
        <div class="flex justify-between items-center px-6 py-4">
          <p class="text-sm text-gray-500">
            Deleting this service is permanent and cannot be undone.
          </p>
          <div class="flex">
            <button
              @click="deleteService"
              class="text-red-600 flex items-center space-x-1 border px-2 py-1 rounded-lg border-red-600 hover:bg-red-50 font-medium"
              :disabled="loadingDelete"
            >
              <span>Delete Service</span>
              <font-awesome-icon
                v-if="!loadingDelete"
                icon="fa-solid fa-trash"
              />
              <font-awesome-icon v-else icon="fa-solid fa-circle-notch" spin />
            </button>
          </div>
        </div>
      </ProfileInfoCard>
    </div>
  </div>
</template>

<script lang="ts" setup>
definePageMeta({
  middleware: "auth",
});

import type { ProfileHeader } from "#components";
import { ref, computed, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useApi } from "@/composables/useApi";

const router = useRouter();
const route = useRoute();
const serviceId = ref<string>(route.params.id as string);
const service = ref<any | null>(null);
const tempService = ref<Partial<Record<string, any>>>();
const profileHeader = ref<InstanceType<typeof ProfileHeader> | null>(null);
const errorMessage = ref("");
const infoMessage = ref("");
const notFound = ref(false);
const authStore = useAuthStore();
const saved = ref(false);

const canModify = computed(() => {
  return (
    (authStore.user?.role?.name as string) === "Admin" ||
    authStore.user?.id === service.value?.providerId
  );
});

const hasChanges = computed(() => {
  if (!service.value || !tempService.value) return false;

  return Object.keys(tempService.value).some((key) => {
    return tempService.value![key] !== service.value![key];
  });
});

const {
  fetch: fetchService,
  loading,
  error,
  data,
} = useApi<any>(`services/${serviceId.value}`, true);

const fetchServiceDetails = async () => {
  const response = await fetchService();
  if (data.value) {
    service.value = data.value;
    tempService.value = { ...data.value };
    errorMessage.value = "";
    infoMessage.value = "";
  } else {
    if (response.status === 404) {
      notFound.value = true;
      infoMessage.value = "Service not found.";
    } else {
      errorMessage.value = "An error occurred while fetching service data.";
    }
  }
};

const resetAllFields = () => {
  if (service.value) {
    tempService.value = { ...service.value };
  }
};

const {
  loading: loadingUpdate,
  put: putDataUpdate,
  data: dataUpdate,
  error: errorUpdate,
} = useApi<any>(`services/${serviceId.value}`);

const save = () => {
  if (!tempService.value) return;
  putDataUpdate(tempService.value)
    .then((response) => {
      if (response.ok) {
        service.value = { ...dataUpdate.value };
        tempService.value = { ...dataUpdate.value };
        saved.value = true;
        setTimeout(() => {
          saved.value = false;
        }, 2000);
      } else {
        errorMessage.value = "Failed to update service data.";
      }
    })
    .catch(() => {
      errorMessage.value = "An error occurred while updating service data.";
    });
};

const { loading: loadingDelete, delete: deleteServiceApi } = useApi<any>(
  `services/${serviceId.value}`
);

const deleteService = () => {
  const confirmDelete = confirm(
    "Are you sure you want to delete this service? This action cannot be undone."
  );
  if (confirmDelete) {
    deleteServiceApi()
      .then((response) => {
        if (response.status === 204) {
          infoMessage.value = "Service deleted successfully.";
          setTimeout(() => {
            router.push("/services");
          }, 2000);
        } else {
          errorMessage.value = "Failed to delete service.";
        }
      })
      .catch(() => {
        errorMessage.value = "An error occurred while deleting the service.";
      });
  }
};

onMounted(() => {
  fetchServiceDetails();
});
</script>

<style>
.service-profile-container {
  max-width: 800px;
}
</style>
