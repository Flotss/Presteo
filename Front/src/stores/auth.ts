import { useCookie } from "#app";
import { defineStore } from "pinia";
import { RoleType } from "~/model/roleType";
import type { User } from "~/model/user";

export const useAuthStore = defineStore("auth", () => {
  const tokenCookie = useCookie("bearer");
  const token = computed(() => tokenCookie?.value);
  const user = ref<User | null>(null);
  const isReady = ref(false);
  const isInitializing = ref(false);
  const router = useRouter();

  const isLoggedIn = computed(() => !!token.value);
  const isAdmin = computed(() => user.value?.role?.name === RoleType.ADMIN);

  const { fetchData: fetchUser, error: errorFetchingUser } =
    useApi<User>("users/me");

  watch(errorFetchingUser, (newVal) => {
    if (newVal) {
      console.error("Erreur lors de la récupération de l'utilisateur:", newVal);
      if (newVal.status === 401) {
        logout();
      }
    }
  });

  // Fonction pour récupérer les données utilisateur
  async function fetchUserData() {
    try {
      if (!token.value) {
        user.value = null;
        return null;
      }

      const userData = await fetchUser();
      user.value = userData;
      return userData;
    } catch (error) {
      console.error(
        "Erreur lors de la récupération des données utilisateur:",
        error
      );
      user.value = null;
      return null;
    }
  }

  async function initialize() {
    if (isReady.value || isInitializing.value) {
      return;
    }

    isInitializing.value = true;

    try {
      if (token.value) {
        await fetchUserData();
      }

      watch(tokenCookie, async (newVal, oldVal) => {
        if (newVal && newVal !== oldVal) {
          await fetchUserData();
        } else if (!newVal) {
          user.value = null;
        }
      });
    } catch (error) {
      console.error("Erreur lors de l'initialisation de l'auth store:", error);
    } finally {
      isReady.value = true;
      isInitializing.value = false;
    }
  }

  function logout() {
    tokenCookie.value = null;
    user.value = null;
    router.push("/");
  }

  onMounted(() => {
    if (process.client && !isReady.value) {
      initialize();
    }
  });

  return {
    user,
    isLoggedIn,
    isAdmin,
    isReady,
    logout,
    fetchUserData,
    initialize,
  };
});
