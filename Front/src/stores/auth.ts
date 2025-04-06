import { useCookie } from "#app";
import { defineStore } from "pinia";
import { ref, watch, computed } from "vue";
import { RoleType } from "~/model/roleType";
import type { User } from "~/model/user";

export const useAuthStore = defineStore("auth", () => {
  const tokenCookie = useCookie("bearer");
  const token = computed(() => tokenCookie?.value);
  const user = ref<User | null>(null);
  const router = useRouter();
  const isLoggedIn = computed(() => !!token.value);
  const isAdmin = computed(() => user.value?.role?.name === RoleType.ADMIN);
  const {
    fetchData: fetchUser,
    error: errorFetchingUser,
    loading,
  } = useApi<User>("users/me");

 

  watch(errorFetchingUser, (newVal) => {
    if (newVal) {
      console.error("Error fetching user:", newVal);
      user.value = null;
    }
  });

  function initializeAuth() {
    if (token.value) {
      fetchUserData();
    }

    watch(tokenCookie, async (newVal) => {
      if (newVal) {
        await fetchUserData();
        await nextTick();
      } else {
        user.value = null;
      }
    });
  }

  onMounted(() => {
    initializeAuth();
  });

  async function fetchUserData() {
    try {
      user.value = await fetchUser();
    } catch (error) {
      console.error("Error fetching user data:", error);
      user.value = null;
    }
  }

  function logout() {
    tokenCookie.value = null;
    user.value = null;
    router.push("/");
  }

  return {
    user,
    logout,
    isLoggedIn,
    isAdmin,
    loading,
  };
});
