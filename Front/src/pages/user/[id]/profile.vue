<template>
  <div class="user-profile-container max-w-4xl mx-auto p-4">
    <ProfileStatusMessages
      :loading="loading"
      :user="user"
      :errorMessage="errorMessage"
      :infoMessage="infoMessage"
      :notFound="notFound"
    />

    <div v-if="user && tempUser" class="profile-content">
      <ProfileHeader
        ref="profileHeader"
        :user="user"
        :isUserLoggedIn="isUserLoggedFetched"
        @update:description="tempUser.description = $event"
      />

      <SaveActionButtons
        v-if="hasChanges || saved"
        :loading="loadingUpdate"
        :saved="saved"
        @save="save"
        @reset="resetAllFields"
      />

      <ProfileInfoCard
        title="General Information"
        subtitle="Basic details about your account"
      >
        <InfoRow
          label="Name"
          :value="`${user.firstName} ${user.lastName}`"
          :action="true"
        >
          <template #form>
            <div class="space-y-4">
              <div class="flex space-x-4" v-if="canModify">
                <BasicInput
                  label="First Name"
                  v-model="tempUser.firstName"
                  placeholder="Enter first name"
                  id="firstName"
                  class="flex-1"
                />
                <BasicInput
                  label="Last Name"
                  v-model="tempUser.lastName"
                  placeholder="Enter last name"
                  id="lastName"
                  class="flex-1"
                />
              </div>
              <UnauthorizedInfo v-else />
            </div>
          </template>
        </InfoRow>
        <InfoRow label="Username" :value="user.username" :action="true">
          <template #form>
            <div class="space-y-4">
              <BasicInput
                v-if="canModify"
                label="Username"
                v-model="tempUser.username"
                placeholder="Enter username"
                id="username"
              />
              <UnauthorizedInfo v-else />
            </div>
          </template>
        </InfoRow>
        <InfoRow label="Birth Date" :value="user.birthDate" :action="true">
          <template #form>
            <div class="space-y-4">
              <BasicInput
                v-if="canModify"
                label="Birth Date"
                v-model="tempUser.birthDate"
                type="date"
                id="birthDate"
              />
              <UnauthorizedInfo v-else />
            </div>
          </template>
        </InfoRow>
        <InfoRow
          label="Type of account"
          :value="user.role?.name"
          :badge="true"
        />
      </ProfileInfoCard>

      <ProfileInfoCard
        title="Contact Information"
        subtitle="Your contact details"
      >
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
                label="Email Address"
                v-model="tempUser.email"
                type="email"
                placeholder="Enter email address"
                id="email"
                format="\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*"
                formatError="Fomat should be like jean.dupont@domain.com"
              />
              <UnauthorizedInfo v-else />
            </div>
          </template>
        </InfoRow>
        <InfoRow
          label="Phone number"
          icon="fa-solid fa-phone"
          :value="user.phoneNumber"
          action
        >
          <template #form>
            <div class="space-y-4">
              <BasicInput
                v-if="canModify"
                label="Phone Number"
                v-model="tempUser.phoneNumber"
                type="tel"
                placeholder="Enter phone number"
                id="phoneNumber"
                format="^[0-9]{10}$"
                formatError="Format should be like 0612345678"
              />
              <UnauthorizedInfo v-else />
            </div>
          </template>
        </InfoRow>
      </ProfileInfoCard>

      <ProfileInfoCard
        title="Security Settings"
        subtitle="Manage your security settings"
        v-if="canModify"
      >
        <InfoRow
          label="Password"
          icon="fa-solid fa-lock"
          value="••••••••"
          action
          badge
          badgeClasses="bg-red-100 text-red-800 rounded-md"
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
      <ProfileInfoCard
        title="Account Settings"
        subtitle="Manage your account preferences"
        v-if="canModify"
      >
        <div class="flex justify-between items-center px-6 py-4">
          <p class="text-sm text-gray-500">
            Deleting your account is permanent and cannot be undone.
          </p>
          <div class="flex">
            <button
              @click="deleteAccount"
              class="text-red-600 flex items-center space-x-1 border px-2 py-1 rounded-lg border-red-600 hover:bg-red-50 font-medium"
              :disabled="loadingDelete"
            >
              <span>Delete Account</span>
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
import { User } from "~/model/user";

const router = useRouter();
const route = useRoute();
const userId = ref<string>(route.params.id as string);
const user = ref<User | null>(null);
const tempUser = ref<Partial<Record<keyof User, any>>>();
const profileHeader = ref<InstanceType<typeof ProfileHeader> | null>(null);
const errorMessage = ref("");
const infoMessage = ref("");
const notFound = ref(false);
const authStore = useAuthStore();
const confirmSave = ref(false);
const saved = ref(false);
const isOwnProfile = ref(false);

const canModify = computed(() => {
  return canUserModify(authStore.user, user.value);
});

const isUserLoggedFetched = computed(() => {
  return authStore.user?.id === user.value?.id;
});

const validateField = (field: string, value: any) => {
  if (field === "email") {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(value);
  } else if (field === "phoneNumber") {
    const phoneRegex = /^\+?[0-9]{10}$/;
    return phoneRegex.test(value);
  } else if (field === "birthDate") {
    return !isNaN(Date.parse(value));
  }
  return true;
};

const hasChanges = computed(() => {
  if (!user.value || !tempUser.value) return false;

  return (
    user.value &&
    Object.keys(tempUser.value).some((key) => {
      const tempValue = tempUser.value![key as keyof User];
      const originalValue = user.value![key as keyof User];

      if (!validateField(key, tempValue)) {
        return false;
      }
      return tempValue !== originalValue;
    })
  );
});

const {
  fetch: fetchData,
  loading,
  error,
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

onMounted(() => {
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
    infoMessage.value = "You are viewing your own profile.";
    loading.value = false;
  } else {
    fetchUser();
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

const { loading: loadingDelete, delete: deleteUser, data: dataDelete, error: errorDelete } = useApi<User>(
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
