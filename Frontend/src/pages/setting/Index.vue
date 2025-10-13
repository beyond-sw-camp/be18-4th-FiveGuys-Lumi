<template>
  <div class="profileContainer">
    <v-icon class="profile">mdi-account</v-icon>

    <div class="userInfoBox">
      <p><strong>이름:</strong> {{ authStore.tokenInfo.name }}</p>
      <p><strong>이메일:</strong> {{ authStore.tokenInfo.email }}</p>
    </div>
    <div><v-spacer /><v-btn @click="deleted">회원탈퇴</v-btn></div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';

const authStore = useAuthStore();
const router = useRouter();

async function deleted() {
  try {
    await authStore.deleted({ email: authStore.tokenInfo.email });
    alert('회원탈퇴 되었습니다.');
    router.push('/login');
  } catch (error) {
    console.error(error);
    alert('회원탈퇴 실패했습니다.');
  }
}

async function userInfo() {
  try {
    // @ts-ignore
    await authStore.fetchUserInfo();
  } catch (error) {
    console.error(error);
    alert('회원정보 불러오기 실패했습니다.');
  }
}
</script>

<style lang="scss" scoped>
.profileContainer {
  display: flex;
  align-items: flex-start;
  gap: 20px;
  margin-top: 20px;
}

.profile {
  font-size: 300px;
  color: gray;
}

.userInfoBox {
  display: flex;
  flex-direction: column;
  justify-content: center;
}
</style>
