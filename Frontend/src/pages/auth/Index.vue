<template>
  <v-container class="login-page" fluid>
    <v-row align="center" class="fill-height" justify="center">
      <v-col cols="12" md="8">
        <v-card class="card-container" elevation="10">
          <!-- 로그인 폼 -->
          <v-card class="form-card sign-in" :class="{ active: isSignUpActive }">
            <v-card-title class="justify-center">로그인</v-card-title>
            <v-card-text>
              <LoginForm @form-submit="handleLogin" />
            </v-card-text>
          </v-card>

          <!-- 회원가입 폼 -->
          <v-card class="form-card sign-up" :class="{ active: isSignUpActive }">
            <v-card-title class="justify-center">회원가입 v1.0</v-card-title>
            <v-card-text>
              <div style="margin-bottom: 12px; font-size: 12px; color: #555; text-align: center">
                서비스 이용을 위해 아래 정보를 입력해주세요.
              </div>
              <SignupForm @switch-to-login="isSignUpActive = false" />
            </v-card-text>
          </v-card>

          <!-- 오버레이 -->
          <div class="overlay" :class="{ 'right-active': isSignUpActive }">
            <div class="overlay-bg"></div>
            <div class="overlay-panel">
              <div v-if="isSignUpActive">
                <h1>Sign In</h1>
                <v-btn color="white" outlined @click="toggleForm">로그인</v-btn>
              </div>
              <div v-else>
                <h1>Sign up</h1>
                <v-btn color="white" outlined @click="toggleForm">회원가입</v-btn>
              </div>
            </div>
          </div>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import LoginForm from '@/pages/auth/components/LoginForm.vue';
import SignupForm from '@/pages/auth/components/SignupForm.vue';
import { useAuthStore } from '@/stores/authStore';

const isSignUpActive = ref(false);
const router = useRouter();
const authStore = useAuthStore();

function toggleForm() {
  isSignUpActive.value = !isSignUpActive.value;

  if (isSignUpActive.value) {
    // URL만 /signup으로 변경
    window.history.replaceState(history.state, '', '/signup');
  } else {
    // URL만 /login으로 변경
    window.history.replaceState(history.state, '', '/login');
  }
}

async function handleLogin(formData) {
  try {
    await authStore.login(formData);
    console.log('로그인 후 accessToken:', authStore.tokenInfo.accessToken);
    router.push('/channels');
  } catch (error) {
    const backendMessage =
      error.response?.data?.message || error.message || '알 수 없는 오류가 발생했습니다.';
    alert(backendMessage);
  }
}
</script>

<style scoped lang="scss">
::v-deep(.v-input input) {
  color: black !important;
}
::v-deep(.v-input .v-field__outline) {
  border-color: black !important;
}

.login-page {
  position: relative;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: none;
  z-index: 1;
}

.login-page::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url('@/assets/image.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  opacity: 0.7;
}

.card-container {
  position: relative;
  width: 100%;
  min-height: 600px;
  overflow: hidden;
  background-color: #eeddff;
}

.form-card {
  position: absolute;
  width: 50%;
  height: 100%;
  top: 0;
  transition: all 0.6s ease-in-out;
  background-color: #eeddff;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px;
}
.form-card.sign-in {
  left: 0;
  z-index: 2;
}
.form-card.sign-up {
  left: 0;
  opacity: 0;
  z-index: 1;
  pointer-events: none;
}
.form-card.active.sign-in {
  transform: translateX(100%);
}
.form-card.active.sign-up {
  transform: translateX(100%);
  opacity: 1;
  z-index: 5;
  pointer-events: auto;
}
.form-card .v-card-text {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

/* 오버레이 */
.overlay {
  position: absolute;
  left: 50%;
  width: 50%;
  height: 100%;
  transition: transform 0.6s ease-in-out;
  z-index: 100;
  background-color: #eeddff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;

  .overlay-bg {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-image: url('@/assets/image.png');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    opacity: 0.5;
    z-index: -1;
  }
}
.overlay.right-active {
  transform: translateX(-100%);
}
.overlay-panel {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  text-align: center;
}
.overlay-panel h1 {
  transform: translateY(-200px);
}
</style>
