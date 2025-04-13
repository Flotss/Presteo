import { ref } from "vue";
import { envLogger, useEnvironment } from "~/utils/environment";

export function useApi<T>(endpoint: string, loadingState?: boolean) {
  const data = ref<T | null>(null);
  const loading = ref(loadingState || false);
  const error = ref<Response | null>(null);

  const env = useEnvironment();
  const baseUrl = env.apiBaseUrl;

  const headers = new Headers({ "Content-Type": "application/json" });

  const parseResponse = async (response: Response) => {
    const contentType = response.headers.get("Content-Type") || "";

    if (contentType.includes("application/json")) return response.json();
    if (contentType.includes("text/")) return response.text();
    throw new Error(`Unsupported content type: ${contentType}`);
  };

  const makeRequest = async (method: string, payload?: any): Promise<any> => {
    loading.value = true;
    error.value = null;
    let url = `${baseUrl}/${endpoint}`;

    try {
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
      const response = await fetch(url, options);

      if (!response.ok) {
        error.value = response;
        data.value = null;
        envLogger.error(`Error ${method} on ${endpoint}:`, response.statusText);
        throw await parseResponse(response);
      }

      const result = method !== "DELETE" ? await parseResponse(response) : true;
      data.value = result;

      envLogger.log(`${method} successful on ${endpoint}`, result);
      return result;
    } catch (err: any) {
      error.value = err;
      data.value = null;
      envLogger.error(`Error ${method} on ${endpoint}:`, err);
    } finally {
      loading.value = false;
    }
  };

  return {
    data,
    loading,
    error,
    fetchData: (payload?: any) => makeRequest("GET", payload),
    postData: (payload: any) => makeRequest("POST", payload),
    putData: (payload: any) => makeRequest("PUT", payload),
    deleteData: () => makeRequest("DELETE"),
  };
}
