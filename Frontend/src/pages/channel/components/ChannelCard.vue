<template>
  <v-card class="channel-card pa-4" rounded-2xl>
    <!-- 카드 헤더 (채널명 + 캐밥 버튼) -->
    <div class="d-flex justify-space-between align-center mb-2">
      <h3>{{ channel.name }}</h3>
      <v-menu v-if="channel?.roleName === 'TUTOR'">
        <template #activator="{ props }">
          <v-btn color="#ffe8ff" elevation="0" v-bind="props">
            <v-icon>mdi-dots-vertical</v-icon>
          </v-btn>
        </template>
        <v-list elevation="0">
          <v-list-item @click="$emit('edit', channel)">수정</v-list-item>
          <v-list-item @click="$emit('delete', channel)">삭제</v-list-item>
        </v-list>
      </v-menu>
    </div>

    <!-- 카드 내용 (과목명) -->
    <p class="text-subtitle-1">과목: {{ channel.subject }}</p>
  </v-card>
</template>

<script setup>
defineProps({
  channel: {
    type: Object,
    required: true,
  },
});

// console.log('channel', channel);

// 이벤트: edit, delete를 부모에게 전달
defineEmits(['edit', 'delete']);
</script>

<style lang="scss" scoped>
.channel-card {
  height: 200px; /* 카드 높이 고정 */
  display: flex;
  flex-direction: column;
  justify-content: space-between; /* 내용 위/아래로 정렬 */
  border-radius: 20px;
  color: #4d4352;
  background-color: #ffe8ff;
  box-shadow: none !important;
}
</style>
