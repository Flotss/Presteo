import { ref } from "vue";
import { envLogger, useEnvironment } from "~/utils/environment";

export function useApi<T>(endpoint: string) {
  const data = ref<T | null>(null);
  const loading = ref(false);
  const error = ref<string | null>(null);

  const env = useEnvironment();
  const baseUrl = env.apiBaseUrl;

  const fetchData = async () => {
    loading.value = true;
    error.value = null;

    try {
      envLogger.log(`Fetching data from ${baseUrl}/${endpoint}`);
      const response = await fetch(`${baseUrl}/${endpoint}`);
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      data.value = await response.json();
      envLogger.log(`Data successfully fetched from ${endpoint}`, data.value);
    } catch (err: any) {
      error.value = err.message || "Une erreur est survenue";
      data.value = null;
      envLogger.error(`Error fetching data from ${endpoint}:`, err);
    } finally {
      loading.value = false;
    }
  };

  const postData = async (payload: any) => {
    loading.value = true;
    error.value = null;

    try {
      envLogger.log(`Posting data to ${baseUrl}/${endpoint}`, payload);
      const response = await fetch(`${baseUrl}/${endpoint}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }

      const result = await response.json();
      envLogger.log(`Data successfully posted to ${endpoint}`, result);
      return result;
    } catch (err: any) {
      error.value = err.message || "Une erreur est survenue";
      envLogger.error(`Error posting data to ${endpoint}:`, err);
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const putData = async (id: string | number, payload: any) => {
    loading.value = true;
    error.value = null;

    try {
      const url = `${baseUrl}/${endpoint}${id ? `/${id}` : ""}`;
      envLogger.log(`Updating data at ${url}`, payload);

      const response = await fetch(url, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }

      const result = await response.json();
      envLogger.log(`Data successfully updated at ${url}`, result);
      return result;
    } catch (err: any) {
      error.value = err.message || "Une erreur est survenue";
      envLogger.error(`Error updating data at ${endpoint}:`, err);
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const deleteData = async (id: string | number) => {
    loading.value = true;
    error.value = null;

    try {
      const url = `${baseUrl}/${endpoint}${id ? `/${id}` : ""}`;
      envLogger.log(`Deleting data at ${url}`);

      const response = await fetch(url, {
        method: "DELETE",
      });

      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }

      envLogger.log(`Data successfully deleted at ${url}`);
      return true;
    } catch (err: any) {
      error.value = err.message || "Une erreur est survenue";
      envLogger.error(`Error deleting data at ${endpoint}:`, err);
      throw err;
    } finally {
      loading.value = false;
    }
  };

  return {
    data,
    loading,
    error,
    fetchData,
    postData,
    putData,
    deleteData,
  };
}
