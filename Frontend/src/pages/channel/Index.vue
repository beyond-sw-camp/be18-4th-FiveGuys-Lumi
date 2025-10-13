<template>
  <div>
    <!-- 상단 등록하기 버튼 -->
    <div class="d-flex justify-end mb-4 pa-4 ga-2">
      <v-btn class="rounded-xl" color="primary-button-1" elevation="0" @click="openJoinModal"
        >채널 참가하기</v-btn
      >
      <v-btn class="rounded-xl" color="primary-button-1" elevation="0" @click="openCreateModal">
        채널 생성하기
      </v-btn>
    </div>

    <!-- 채널 카드 목록 -->
    <v-container fluid>
      <v-row>
        <v-col v-for="channel in channels" :key="channel.channelId" cols="12" md="4" sm="6">
          <ChannelCard
            :channel="channel"
            @click="goToClasses(channel)"
            @delete="openDeleteModal"
            @edit="openEditModal"
          />
        </v-col>
      </v-row>
    </v-container>

    <!-- 등록/수정 모달 -->
    <ChannelFormModal
      v-model="formDialog"
      :channel="selectedChannel"
      :mode="formMode"
      @submit="handleSubmit"
    />

    <!-- 삭제 모달 -->
    <ChannelDeleteModal v-model="deleteDialog" :channel="selectedChannel" @delete="handleDelete" />

    <!-- 참가 모달 -->
    <ChannelJoinModal v-model="joinDialog" @join="handleJoin" />
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

import {
  createChannel,
  deleteChannel,
  getChannel,
  getChannels,
  joinChannel,
  updateChannel,
} from '@/apis/channel';

import ChannelCard from './components/ChannelCard.vue';
import ChannelDeleteModal from './components/ChannelDeleteModal.vue';
import ChannelFormModal from './components/ChannelFormModal.vue';
import ChannelJoinModal from './components/ChannelJoinModal.vue';

const router = useRouter();
const channels = ref([]);

// 더미 데이터
// const channels = ref([
//   { id: 1, name: '프론트엔드 스터디', subject: 'Vue.js' },
//   { id: 2, name: '백엔드 프로젝트', subject: 'Spring Boot' },
//   { id: 3, name: '알고리즘 모임', subject: '자료구조' },
// ]);

// 등록/수정 모달
const formDialog = ref(false);
const formMode = ref('create'); // "create" | "edit"
const selectedChannel = ref(null);

// 삭제 모달
const deleteDialog = ref(false);

const joinDialog = ref(false);

// ✅ 채널 목록 불러오기
async function loadChannels() {
  channels.value = await getChannels();
  console.log('ch', channels.value);
}

onMounted(async () => await loadChannels());

function openCreateModal() {
  formMode.value = 'create';
  selectedChannel.value = null;
  formDialog.value = true;
}

function openJoinModal() {
  joinDialog.value = true;
}

function openEditModal(channel) {
  formMode.value = 'edit';
  selectedChannel.value = channel;
  formDialog.value = true;
}

function openDeleteModal(channel) {
  selectedChannel.value = channel;
  deleteDialog.value = true;
}

function goToClasses(channel) {
  router.push(`/channels/${channel.channelId}/classes`);
}

// ✅ 등록/수정 처리 (삼항 연산자 사용)
async function handleSubmit(newData) {
  formMode.value === 'create'
    ? await createChannel(newData)
    : await updateChannel(selectedChannel.value.channelId, newData);

  await loadChannels();
  formDialog.value = false;
}

// ✅ 삭제 처리
async function handleDelete(channel) {
  await deleteChannel(channel.channelId);
  await loadChannels();
  deleteDialog.value = false;
}

// 초대
async function handleJoin(code) {
  try {
    // ✅ 초대 코드를 사용하여 API 호출
    const newChannel = await joinChannel(code);
    console.log('채널 참가 성공:', newChannel);

    await loadChannels();
    joinDialog.value = false;
  } catch (error) {
    console.error('채널 참가 실패:', error.response.data.message);
    // TODO: v-snackbar 등을 사용해 사용자에게 에러 메시지를 보여주는 로직 추가
    alert(`채널 참가 실패: ${error.response.data.message}`);
  }
}
</script>

<style lang="scss" scoped></style>
