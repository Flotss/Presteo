export default defineNuxtRouteMiddleware(async (to) => {
  const authStore = useAuthStore();

  const publicPages = ["/login", "/signup", "/"];
  const isPublicPage = publicPages.includes(to.path);

  if (import.meta.server) {
    return;
  }

  try {
    if (!authStore.isReady) {
      await authStore.initialize();
    }
  } catch (error) {
    envLogger.error(
      "Failed to initialize the authentication store:",
      error
    );
  }

  if ((to.path === "/login" || to.path === "/signup") && authStore.isLoggedIn) {
    return navigateTo("/");
  }

  if (!isPublicPage && !authStore.isLoggedIn) {
    return navigateTo(`/login?redirect=${encodeURIComponent(to.fullPath)}`);
  }

  if (authStore.isLoggedIn && !authStore.user) {
    try {
      await authStore.fetchUserData();

      if (!authStore.user) {
        envLogger.log(
          "Unable to fetch user data despite the token, logging out"
        );
        authStore.logout();
        return navigateTo(
          `/login?redirect=${encodeURIComponent(
            to.fullPath
          )}&error=session-expired`
        );
      }
    } catch (error) {
      envLogger.error("Failed to fetch user data:", error);
      authStore.logout();
      return navigateTo(
        `/login?redirect=${encodeURIComponent(to.fullPath)}&error=session-error`
      );
    }
  }
});
