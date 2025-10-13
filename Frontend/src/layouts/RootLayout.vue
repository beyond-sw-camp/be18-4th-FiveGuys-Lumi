<template>
  <v-navigation-drawer color="primary" permanent>
    <v-list color="primary">
      <v-list-item color="primary" link title="Lumi" to="/" />
      <v-divider />
      <v-list-item
        color="primary"
        link
        prepend-icon="mdi-view-dashboard"
        title="Channel"
        to="/channels"
      />
      <v-list-item
        color="primary"
        link
        prepend-icon="mdi-calendar-blank"
        title="Calendar"
        to="/calendar"
      />
      <!-- <v-list-item
        color="primary"
        link
        prepend-icon="mdi-message-outline"
        title="Chat"
        to="/chats"
      /> -->
    </v-list>

    <template #append>
      <v-divider />
      <div class="text-center ma-4 text-caption">
        COPYRIGHT ⓒ {{ new Date().getFullYear() }} BY <strong>YuGuanZhang</strong>
      </div>
    </template>
  </v-navigation-drawer>
  <v-app-bar class="bg-primary-lighten-1" elevation="0">
    <v-spacer />
    <span class="user-name">{{ userName }}</span>
    <v-btn variant="plain" @click="logout"> 로그아웃 </v-btn>
    <v-btn icon @click="profileDialog = true">
      <v-icon>mdi-account-cog-outline</v-icon>
    </v-btn>
  </v-app-bar>

  <v-main class="d-flex align-center justify-center">
    <v-container class="fill-height w-100" fluid>
      <v-sheet class="root-content rounded-xl fill-height w-100">
        <slot />
      </v-sheet>
    </v-container>
  </v-main>

  <v-dialog v-model="profileDialog" max-width="400">
    <v-card>
      <v-card-title class="d-flex align-center">
        <span> 프로필 정보 </span>
        <v-spacer />
        <v-btn icon variant="text" @click="profileDialog = false">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>

      <v-card-text>
        <v-container class="pa-0" fluid>
          <v-row class="mb-2" no-gutters>
            <v-col class="font-weight-bold" cols="2">이름</v-col>
            <v-col cols="10">{{ authStore.tokenInfo.name }}</v-col>
          </v-row>
          <v-row class="mb-2" no-gutters>
            <v-col class="font-weight-bold" cols="2">이메일</v-col>
            <v-col cols="10">{{ authStore.tokenInfo.email }}</v-col>
          </v-row>
          <v-row>
            <v-col class="text-right">
              <v-btn @click="deleted">회원탈퇴</v-btn>
            </v-col>
          </v-row>
        </v-container>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';

const authStore = useAuthStore();
const router = useRouter();
const profileDialog = ref(false);

async function logout() {
  try {
    await authStore.logout();
    alert('로그아웃 되었습니다.');
    router.push('/login');
  } catch (error) {
    console.error(error);
    alert('로그아웃 실패했습니다.');
  }
}

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

onMounted(async () => {
  if (authStore.tokenInfo.accessToken) {
    try {
      await authStore.fetchProfile();
    } catch (error) {
      console.error(error);
    }
  }
});

const userName = computed(() => authStore.tokenInfo.name);
</script>

<style lang="scss" scoped>
.v-navigation-drawer,
.v-navigation-drawer .v-list,
.v-navigation-drawer .v-list-item {
  background-color: rgb(var(--v-theme-primary)) !important;
  color: white !important;
}
</style>
