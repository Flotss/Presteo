import { ref } from "vue";
import { envLogger, useEnvironment } from "~/utils/environment";

export function useApi<T>(endpoint: string, loadingState?: boolean) {
  const data = ref<T | null>(null);
  const loading = ref(loadingState || false);
  const error = ref<any>(null);

  const env = useEnvironment();
  const baseUrl = env.apiBaseUrl;

  const headers = new Headers({ "Content-Type": "application/json" });

  const parseResponse = async (response: Response) => {
    try {
      let contentType = response.headers.get("Content-Type");

      if (!contentType) {
        return null;
      }

      if (contentType.includes("application/json"))
        return await response.json();
      if (contentType.includes("text/")) return await response.text();
      throw new Error("Unsupported content type: " + contentType);
    } catch (err) {
      envLogger.error("Error parsing response:", err);
      return null;
    }
  };

  const makeRequest = async (method: string, payload?: any): Promise<any> => {
    loading.value = true;
    error.value = null;
    let url = `${baseUrl}/${endpoint}`;

    const options: RequestInit = {
      method,
      headers,
      credentials: "include",
    };

    if (method === "GET" && payload) {
      const queryParams = new URLSearchParams(payload).toString();
      url += `?${queryParams}`;
    }

    if (payload && ["POST", "PUT", "PATCH"].includes(method)) {
      options.body = JSON.stringify(payload);
    }

    envLogger.log(`${method} request to ${url}`, payload || "");

    try {
      const response = await fetch(url, options);
      const result = await parseResponse(response);

      if (!response.ok) {
        error.value = result;
        data.value = null;
        envLogger.error(`Error ${method} on ${endpoint}:`, response.statusText);
      } else {
        data.value = method !== "DELETE" ? result : true;
        envLogger.log(`${method} successful on ${endpoint}`, result);
      }

      return response;
    } catch (err) {
      error.value = err;
      data.value = null;
      envLogger.error(`Exception ${method} on ${endpoint}:`, err);
      return { error: err, result: null };
    } finally {
      loading.value = false;
    }
  };

  return {
    data,
    loading,
    error,
    fetch: (payload?: any) => makeRequest("GET", payload),
    post: (payload: any) => makeRequest("POST", payload),
    put: (payload: any) => makeRequest("PUT", payload),
    delete: () => makeRequest("DELETE"),
  };
}
