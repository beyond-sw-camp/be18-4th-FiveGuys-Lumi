<template>
  <div>
    <!-- 상단 영역 -->
    <div class="d-flex justify-space-between align-center mb-4">
      <!-- 정렬 드롭다운 -->
      <v-select
        v-model="sortOrder"
        density="compact"
        :items="['최신순', '오래된순']"
        style="max-width: 120px"
        variant="outlined"
      />

      <!-- 교사만 생성 버튼 -->
      <v-btn
        v-if="channel?.roleName === 'TUTOR'"
        color="primary-button-1"
        elevation="0"
        rounded="xl"
        @click="goToCreate"
        >과제 생성하기</v-btn
      >
    </div>

    <!-- 과제 목록 -->
    <AssignmentList
      :assignments="sortedAssignments"
      :channel="channel"
      @click="goToDetail"
      @delete="openDeleteModal"
      @edit="goToEdit"
    />

    <!-- 삭제 모달 -->
    <AssignmentDeleteModal
      v-model="deleteDialog"
      :assignment="selectedAssignment"
      @confirm="handleDelete"
    />
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { deleteAssignment, getAssignments } from '@/apis/assignment';
import { getChannel } from '@/apis/channel';
import AssignmentDeleteModal from './components/AssignmentDeleteModal.vue';
import AssignmentList from './components/AssignmentList.vue';

const route = useRoute();
const router = useRouter();

const channelId = route.params.channelId;
console.log(channelId);

const channel = ref(null);

// 상태
const assignments = ref([]);
const sortOrder = ref('최신순'); // 정렬 옵션
const deleteDialog = ref(false);
const selectedAssignment = ref(null);

// 목록 불러오기
async function loadAssignments() {
  assignments.value = await getAssignments(channelId);
}

// 정렬된 목록
const sortedAssignments = computed(() => {
  return [...assignments.value].sort(
    (a, b) =>
      sortOrder.value === '최신순'
        ? new Date(b.createdAt) - new Date(a.createdAt) // 최신순
        : new Date(a.createdAt) - new Date(b.createdAt), // 오래된순
  );
});
function goToCreate() {
  router.push(`/channels/${channelId}/assignments/new`);
}
function goToDetail(assignment) {
  router.push(`/channels/${channelId}/assignments/${assignment.assignmentId}`);
  console.log('assignmentId:', assignment.assignmentId);
}
function goToEdit(assignment) {
  router.push(`/channels/${channelId}/assignments/${assignment.assignmentId}/edit`);
}

function openDeleteModal(assignment) {
  selectedAssignment.value = assignment;
  deleteDialog.value = true;
}
async function handleDelete(assignment) {
  await deleteAssignment(channelId, assignment.assignmentId);
  await loadAssignments();
  deleteDialog.value = false;
}

onMounted(async () => {
  await loadAssignments();
  channel.value = await getChannel(channelId);
  console.log('채널 확인', channel.value);
});
</script>

<style lang="scss" scoped>
.assignment-list {
  background-color: #fdf6ff;
  border-radius: 12px;
  padding: 12px;
}
.assignment-item {
  border-bottom: 1px solid #eee;
  padding: 12px 0;
}
</style>
