import { ref } from "vue";
import { envLogger, useEnvironment } from "~/utils/environment";

export function useApi<T>(endpoint: string) {
  const data = ref<T | null>(null);
  const loading = ref(false);
  const error = ref<string | null>(null);

  const env = useEnvironment();
  const baseUrl = env.apiBaseUrl;

  const token = useCookie("bearer").value;

  const headers = new Headers(
    token
      ? {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        }
      : {
          "Content-Type": "application/json",
        }
  );

  const parseResponse = async (response: Response) => {
    const contentType = response.headers.get("Content-Type") || "";
    if (contentType.includes("application/json")) {
      return response.json();
    } else if (contentType.includes("text/html")) {
      return response.text();
    } else if (contentType.includes("text/plain")) {
      return response.text();
    } else {
      throw new Error(`Unsupported content type: ${contentType}`);
    }
  }

  const fetchData = async () => {
    loading.value = true;
    error.value = null;

    try {
      envLogger.log(`Fetching data from ${baseUrl}/${endpoint}`);
      const response = await fetch(`${baseUrl}/${endpoint}`, {
        headers,
        credentials: "include",
      });
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      data.value = await parseResponse(response);
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
        headers,
        body: JSON.stringify(payload),
        credentials: "include",
      });

      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }

      const result = await parseResponse(response);
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
        headers,
        body: JSON.stringify(payload),
        credentials: "include",
      });

      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }

      const result = await parseResponse(response);
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
        headers,
        method: "DELETE",
        credentials: "include",
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
