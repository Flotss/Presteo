<template>
  <div class="flex min-h-screen bg-gray-50">
    <!-- Left Navigation -->
    <ProfileNavigation
      v-model:active-section="activeSection"
      v-model:is-open="isNavOpen"
      :is-provider-or-admin="isProviderOrAdmin"
    />

    <!-- Main Content -->
    <div class="flex-1 p-4 md:p-8">
      <!-- Mobile Navigation Button -->
      <button
        class="md:hidden w-full bg-white text-gray-800 border border-gray-200 p-4 rounded-lg mb-4 flex items-center justify-between shadow-sm"
        @click="isNavOpen = !isNavOpen"
      >
        <span class="font-medium">Profile Menu</span>
        <font-awesome-icon
          :icon="['fas', isNavOpen ? 'times' : 'bars']"
          class="text-gray-600 text-xl"
        />
      </button>

      <ProfileStatusMessages
        :loading="loading"
        :user="user"
        :error-message="errorMessage"
        :info-message="infoMessage"
        :not-found="notFound"
        @clear-error-message="errorMessage = ''"
        @clear-info-message="infoMessage = ''"
      />

      <ProfileContent
        :active-section="activeSection"
        :user="user"
        :temp-user="tempUser"
        :is-own-profile="isOwnProfile"
        :is-user-logged-fetched="isUserLoggedFetched"
        :has-changes="hasChanges"
        :saved="saved"
        :loading-update="loadingUpdate"
        :can-modify="canModify"
        :user-id="userId"
        :loading-delete="loadingDelete"
        @update:temp-user="handleTempUserUpdate($event)"
        @save="save"
        @reset="resetAllFields"
        @delete-account="deleteAccount"
      />

      <!-- Services Section -->
      <ProfileServices
        v-if="activeSection === 'services' && isProviderOrAdmin"
        :services="services"
        :loading="loadingServices"
        @delete-service="deleteService"
      />

      <!-- Bookings Section -->
      <ProfileBookings
        v-if="activeSection === 'bookings' && isProviderOrAdmin"
        :bookings="providerBookings"
        :loading="loadingBookings"
        @update-status="updateBookingStatus"
      />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from "vue";
import type { ProfileHeader } from "#components";
import { RoleType } from "~/model/roleType";
import type { User } from "~/model/user";
import type { Service } from "~/model/service";
import type { Booking } from "~/model/booking";

useHead({
  title: "User Profile | Presteo",
});

definePageMeta({
  middleware: "auth",
});

const router = useRouter();
const route = useRoute();
const userId = ref<string>(route.params.id as string);
const user = ref<User | null>(null);
const tempUser = ref<User | null>(null);
const profileHeader = ref<InstanceType<typeof ProfileHeader> | null>(null);
const errorMessage = ref("");
const infoMessage = ref("");
const notFound = ref(false);
const authStore = useAuthStore();
const confirmSave = ref(false);
const saved = ref(false);
const isOwnProfile = ref(false);
const isNavOpen = ref(false);

// New state for navigation and sections
const activeSection = ref("profile");
const services = ref<Service[]>([]);
const providerBookings = ref<Booking[]>([]);
const loadingServices = ref(false);
const loadingBookings = ref(false);

const isProviderOrAdmin = computed(() => {
  return (
    user.value?.role?.name === RoleType.PROVIDER ||
    user.value?.role?.name === RoleType.ADMIN
  );
});

const canModify = computed(() => {
  return canUserModify(authStore.user, user.value);
});

const isUserLoggedFetched = computed(() => {
  return authStore.user?.id === user.value?.id;
});

const validateField = (field: string, value: unknown) => {
  if (field === "email") {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(value as string);
  } else if (field === "phoneNumber") {
    const phoneRegex = /^\+?[0-9]{10}$/;
    return phoneRegex.test(value as string);
  } else if (field === "birthDate") {
    return !isNaN(Date.parse(value as string));
  }
  return true;
};

const hasChanges = computed(() => {
  if (!user.value || !tempUser.value) return false;

  return Object.keys(tempUser.value).some((key) => {
    const tempValue = tempUser.value![key as keyof User];
    const originalValue = user.value![key as keyof User];

    if (!validateField(key, tempValue as unknown)) {
      return false;
    }
    return tempValue !== originalValue;
  });
});

const handleTempUserUpdate = (updatedUser: Partial<User>) => {
  tempUser.value = { ...tempUser.value, ...updatedUser } as User;
};

const {
  fetch: fetchData,
  loading,
  data,
} = useApi<User>(`users/${userId.value}`, true);

const fetchUser = async () => {
  const response = await fetchData();
  if (data.value) {
    user.value = data.value;
    initTempUser();
    errorMessage.value = "";
    infoMessage.value = "";
  } else {
    if (response.status === 404) {
      notFound.value = true;
      infoMessage.value = "";
    } else {
      errorMessage.value = "An error occurred while fetching user data.";
    }
  }
};

const initTempUser = () => {
  if (user.value) {
    tempUser.value = { ...user.value };
  }
};

const resetAllFields = () => {
  initTempUser();
  if (profileHeader.value) {
    profileHeader.value.resetDescription();
  }
};

const fetchServices = async () => {
  if (!isProviderOrAdmin.value) return;

  loadingServices.value = true;
  try {
    const { fetch, data } = useApi<Service[]>(
      `services/provider/${userId.value}`
    );
    await fetch();
    if (data.value) {
      services.value = data.value;
    }
  } catch (error) {
    console.error("Error fetching services:", error);
    errorMessage.value = "Failed to fetch services";
  } finally {
    loadingServices.value = false;
  }
};

const fetchBookings = async () => {
  if (!isProviderOrAdmin.value) return;

  loadingBookings.value = true;
  try {
    const { fetch, data } = useApi<Booking[]>(
      `bookings/provider/${userId.value}`
    );
    await fetch();
    if (data.value) {
      providerBookings.value = data.value;
    }
  } catch (error) {
    console.error("Error fetching bookings:", error);
    errorMessage.value = "Failed to fetch bookings";
  } finally {
    loadingBookings.value = false;
  }
};

const updateBookingStatus = async (booking: Booking) => {
  const { error, post } = useApi<Booking>(`bookings/${booking.id}/status`);
  await post({ status: booking.status });

  if (error.value) {
    errorMessage.value = "Failed to update booking status " + error.value.error;
    await fetchBookings();
  } else {
    infoMessage.value = "Booking status updated successfully";
  }
};

const deleteService = async (service: Service) => {
  if (!confirm("Are you sure you want to delete this service?")) return;

  try {
    const { delete: deleteService } = useApi<Service>(`services/${service.id}`);
    await deleteService();
    await fetchServices();
    infoMessage.value = "Service deleted successfully";
  } catch (error) {
    console.error("Error deleting service:", error);
    errorMessage.value = "Failed to delete service";
  }
};

onMounted(async () => {
  if (!userId.value) {
    errorMessage.value = "User ID is not provided";
    setTimeout(() => {
      errorMessage.value = "";
      infoMessage.value = "Please provide a valid user ID.";
    }, 2000);
    return;
  }

  if (isNaN(Number(userId.value))) {
    loading.value = false;
    errorMessage.value = "User ID is not a number : " + userId.value;
    setTimeout(() => {
      infoMessage.value = "Please provide a valid user ID. Like 1, 2, 3...";
    }, 1000);
    return;
  }

  isOwnProfile.value = authStore.user?.id === Number(userId.value);

  if (isOwnProfile.value && authStore.user) {
    user.value = authStore.user;
    initTempUser();
    errorMessage.value = "";
    infoMessage.value = "";
    loading.value = false;
  } else {
    await fetchUser();
  }

  if (isProviderOrAdmin.value) {
    await fetchServices();
    await fetchBookings();
  }
});

const {
  loading: loadingUpdate,
  put: putDataUpdate,
  data: dataUpdate,
  error: errorUpdate,
} = useApi<User>(`users/${userId.value}`);

const save = () => {
  if (!tempUser.value) return;
  putDataUpdate(tempUser.value)
    .then((response) => {
      if (response.ok) {
        const responseUser = dataUpdate.value as User;
        user.value = { ...responseUser };
        if (isOwnProfile.value) {
          authStore.user = { ...responseUser };
        }
        initTempUser();
        errorMessage.value = "";
        saved.value = true;
        setTimeout(() => {
          saved.value = false;
        }, 2000);
      } else {
        console.error("Error updating user data:", errorUpdate.value);
        window.scrollTo({
          top: 0,
          behavior: "smooth",
        });
        errorMessage.value = "Failed to update user data.";
      }
    })
    .catch((error) => {
      window.scrollTo({
        top: 0,
        behavior: "smooth",
      });
      errorMessage.value = "An error occurred while updating user data.";
      console.error("Error updating user data:", error);
    })
    .finally(() => {
      setTimeout(() => {
        confirmSave.value = false;
      }, 2000);
      if (profileHeader.value) {
        profileHeader.value.closeDescriptionEdit();
      }
    });
};

const { loading: loadingDelete, delete: deleteUser } = useApi<User>(
  `users/${userId.value}`
);

const deleteAccount = () => {
  const confirmDelete = confirm(
    "Are you sure you want to delete your account? This action cannot be undone."
  );
  if (confirmDelete) {
    deleteUser()
      .then((response) => {
        if (response.status === 204) {
          infoMessage.value = "Account deleted successfully.";
          window.scrollTo({
            top: 0,
            behavior: "smooth",
          });
          setTimeout(() => {
            if (authStore.isAdmin) {
              router.push("/admin/users");
            } else {
              authStore.logout();
            }
          }, 2000);
        } else {
          errorMessage.value = "Failed to delete account.";
        }
      })
      .catch((error) => {
        errorMessage.value = "An error occurred while deleting the account.";
        console.error("Error deleting account:", error);
      });
  }
};
</script>

<style></style>
