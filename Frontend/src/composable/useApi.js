import { ref } from 'vue';

export function useApi(apiFn) {
  const data = ref(null);
  const loading = ref(false);
  const error = ref(null);

  const queryFnExecute = async (...args) => {
    loading.value = true;
    error.value = null;
    try {
      const response = (data.value = await apiFn(...args));
      return response;
    } catch (error) {
      error.value = error;
    }
  };

  return { data, loading, error, queryFnExecute };
}
