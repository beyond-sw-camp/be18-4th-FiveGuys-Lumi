<template>
  <div>
    <h2>{{ channel?.name }}</h2>

    <!-- 🔹 참여자 목록 -->
    <v-card class="mb-4">
      <v-card-title>참여자 목록</v-card-title>
      <v-list dense>
        <v-list-item v-for="user in participants" :key="user.userId">
          <v-list-item-title> {{ user.name }} ({{ user.roleName }}) </v-list-item-title>
        </v-list-item>
        <v-list-item v-if="participants.length === 0">
          <v-list-item-title class="text-grey">참여자가 없습니다.</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-card>

    <!-- 🔹 초대발송 모달 버튼 -->
    <Index :channel="channel" @invited="fetchParticipants" />
  </div>
</template>

<script>
import apiClient from '@/apis/apiClient';
import Index from './Index.vue';

export default {
  name: 'ChannelPage',
  components: { Index },
  data() {
    return {
      channel: { name: '' },
      participants: [],
    };
  },
  async mounted() {
    await this.fetchChannel();
    await this.fetchParticipants();
  },
  methods: {
    async fetchChannel() {
      // 채널 상세 API 있다고 가정
      const channelId = this.$route.params.channelId;
      const res = await apiClient.get(`/channels/${channelId}`);
      this.channel = res.data?.data || {};
    },
    async fetchParticipants() {
      const channelId = this.$route.params.channelId;
      const res = await apiClient.get(`/channels/${channelId}/participants`);
      this.participants = res.data?.data?.content || [];
    },
  },
};
</script>
