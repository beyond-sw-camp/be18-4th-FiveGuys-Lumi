<template>
  <v-dialog :model-value="visible" @update:model-value="$emit('close')" max-width="500">
    <v-card>
      <v-card-title class="text-h6">참여자 정보</v-card-title>
      <v-divider />

      <!-- 로딩 중 -->
      <v-card-text v-if="loading">로딩 중...</v-card-text>

      <!-- 데이터 표시 -->
      <v-card-text v-else-if="userInfo">
        <v-text-field
          v-model="userInfo.name"
          label="이름"
          outlined
          readonly
          :value="userInfo.name || userInfo.userId"
        />
        <v-text-field
          v-model="userInfo.email"
          label="이메일"
          outlined
          readonly
          :value="userInfo.email || userInfo.userId"
        />
        <v-text-field v-model="userInfo.roleName" label="역할" outlined readonly />
      </v-card-text>

      <!-- 데이터 없음 -->
      <v-card-text v-else>데이터가 없습니다.</v-card-text>

      <v-card-actions class="justify-end">
        <v-btn text @click="$emit('close')">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import { useAuthStore } from '@/stores/authStore';

export default {
  name: 'InfoDialog',
  props: {
    visible: Boolean,
    channelId: String,
    userId: String,
  },
  data() {
    return {
      userInfo: null,
      loading: false,
    };
  },
  watch: {
    async visible(newVal) {
      if (newVal && this.userId) {
        console.log('모달 열림, userId:', this.userId);
        await this.loadUserInfo();
      }
    },
  },
  methods: {
    async loadUserInfo() {
      try {
        this.loading = true;
        this.userInfo = null;

        const authStore = useAuthStore();
        const res = await authStore.fetchChannelUser(this.channelId, this.userId);

        console.log('API response:', res);

        // API response.data.data가 배열이므로 첫 번째 요소 사용
        this.userInfo = res.data?.data?.[0] || null;
      } catch (err) {
        console.error('참여자 정보 조회 실패:', err);
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>
