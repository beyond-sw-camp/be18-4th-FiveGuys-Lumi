import axios from 'axios';
import { useAuthStore } from '@/stores/authStore';

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL + '/api',
  timeout: 5000,
  withCredentials: true,
});

// Axios 인터셉터
//   - 요청(Request) 또는 응답(Response)이 처리되기 전에 가로채서 특정 로직을 수행하도록 하는 기능이다.!!!!!!!!!!

// 요청(Request) 인터셉터
//   - HTTP 요청이 서버로 전송되기 전에 실행된다.
apiClient.interceptors.request.use(
  config => {
    // @ts-ignore
    if (config._skipInterceptor) {
      return config;
    }

    // authStore에서 accessToken을 가져온다.
    const authStore = useAuthStore();
    const accessToken = authStore.tokenInfo.accessToken;
    if (accessToken) {
      config.headers['Authorization'] = `Bearer ${accessToken}`;
    }

    return config;
  },
  error => {
    // 비동기 코드에서 에러를 처리하거나 에러를 즉시 반환할 때 사용된다.
    return Promise.reject(error);
  },
);

// 응답(Response) 인터셉터
//   - 서버에서 HTTP 응답이 도착한 후에 실행된다.
apiClient.interceptors.response.use(
  response => response,
  async error => {
    const originConfig = error.config;
    const authStore = useAuthStore();

    const backendMessage = error.response?.data?.message;

    // 백엔드에서 내려주는 메시지를 그대로 UI로 전달
    if (backendMessage) {
      throw error; // 메시지가 있으면 refresh 시도하지 않고 그대로 전달
    }

    // 401 발생 시 refresh 토큰 재발급
    if (error.response?.status === 401 && !originConfig._retry) {
      originConfig._retry = true;
      try {
        await authStore.refreshAccessToken();
        return apiClient(originConfig);
      } catch (refreshError) {
        authStore.performLogout();
        throw refreshError;
      }
    }

    throw error;
  },
);

export default apiClient;
