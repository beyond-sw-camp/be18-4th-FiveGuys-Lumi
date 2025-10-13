<template>
  <v-container class="pa-6" fluid>
    <!-- 제목 -->
    <div class="text-h5 font-weight-bold mb-2">
      {{ assignment.title }}
    </div>
    <v-divider class="mb-4" />

    <!-- 기한 정보 -->
    <v-row class="mb-4">
      <v-col cols="12" md="6">
        <strong>제출 마감일:</strong>
        {{ formatDate(assignment.deadlineAt) }}
      </v-col>
    </v-row>

    <!-- 과제 설명 -->

    <div class="mb-4">
      <p class="text-body-1">{{ assignment.content }}</p>
    </div>

    <!-- 상태 -->
    <div class="mb-4">
      <v-chip class="mr-2" :color="assignment.submission ? 'success' : 'warning'" size="small">
        {{ assignment.submission ? '제출 완료' : '미제출' }}
      </v-chip>
      <v-chip class="mr-2" :color="assignment.evaluation ? 'info' : 'grey'" size="small">
        {{ assignment.evaluation ? '평가 있음' : '평가 없음' }}
      </v-chip>
    </div>

    <!-- 첨부 파일 -->
    <div v-if="assignment.files.length > 0" class="mb-4">
      <h4 class="text-subtitle-1 font-weight-bold">첨부 파일</h4>
      <v-list>
        <v-list-item v-for="file in assignment.files" :key="file.fileId" :title="file.fileName">
          <template #prepend>
            <v-icon>mdi-file</v-icon>
          </template>
          <template #append>
            <v-btn color="primary" icon size="small" @click.stop="download(file)">
              <v-icon>mdi-download</v-icon>
            </v-btn>
          </template>
        </v-list-item>
      </v-list>
    </div>

    <v-divider class="my-6" />
    <!-- 역할별 버튼 -->
    <!-- 제출 영역 -->
    <!-- 학생: 제출이 없으면 제출 폼 -->
    <!-- 학생: 제출 없을 때 -->
    <SubmissionCreate
      v-if="channel?.roleName === 'STUDENT' && !submission"
      mode="create"
      @submitted="handleCreate"
    />

    <!-- 제출 있음 -->
    <SubmissionIndex
      v-else-if="submission"
      :assignment="assignment"
      :channel="channel"
      :submission="submission"
      @delete="handleDelete"
      @edit="handleUpdate"
    />

    <!-- 교사인데 제출 없음 -->
    <div v-else-if="channel?.roleName === 'TUTOR'" class="text-grey">아직 제출이 없습니다.</div>

    <!-- 과제 삭제 모달 -->
    <AssignmentDeleteModal
      v-model="deleteDialog"
      :assignment="assignment"
      @confirm="confirmDelete"
    />
  </v-container>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { deleteAssignment, getAssignment } from '@/apis/assignment';
import { getChannel } from '@/apis/channel';
import { downloadFile } from '@/apis/file';
import {
  createSubmission,
  deleteSubmission,
  getSubmission,
  updateSubmission,
} from '@/apis/submission';
import SubmissionIndex from '@/pages/submission/Index.vue';
import SubmissionCreate from '@/pages/submission/SubmissionCreate.vue';
import AssignmentDeleteModal from './components/AssignmentDeleteModal.vue';

const route = useRoute();
const router = useRouter();

const channelId = route.params.channelId;
const assignmentId = route.params.assignmentId;

const channel = ref(null);

console.log('assignmentId:', assignmentId);

const assignment = ref({ files: [] });
const submission = ref(null);
const deleteDialog = ref(false);

async function loadAssignment() {
  const data = await getAssignment(channelId, assignmentId);
  assignment.value = Array.isArray(data) ? data[0] : data;
}

async function loadSubmission() {
  try {
    const data = await getSubmission(channelId, assignmentId);
    console.log('getSubmission 응답:', data);

    submission.value = Array.isArray(data) ? data[0] : data;
  } catch (error) {
    console.log(error);

    submission.value = null; // 제출 없음
  }
}

function formatDate(dateStr) {
  if (!dateStr) return '-';
  return dateStr.split(' ')[0].replace(/-/g, '.'); // yyyy.MM.dd
}

function download(file) {
  console.log('파일 다운로드:', file);
  // TODO: 실제 다운로드 API 연동
  downloadFile(file.fileId, file.fileName);
}

function goToEdit() {
  router.push(`/channels/${channelId}/assignments/${assignmentId}/edit`);
}

async function confirmDelete() {
  await deleteAssignment(channelId, assignmentId);
  router.push(`/channels/${channelId}/assignments`);
}

/* ---- 제출 관련 핸들러 ---- */

// 제출 생성
async function handleCreate(payload) {
  const newSubmission = await createSubmission(channelId, assignmentId, payload);
  submission.value = newSubmission;
}

// 제출 수정
async function handleUpdate(payload) {
  if (!submission.value) return;
  const updated = await updateSubmission(
    channelId,
    assignmentId,
    submission.value.submissionId,
    payload,
  );
  submission.value = updated;
}

// 제출 삭제
async function handleDelete() {
  if (!submission.value) return;
  await deleteSubmission(channelId, assignmentId, submission.value.submissionId);

  submission.value = null;
}

onMounted(async () => {
  loadAssignment();
  loadSubmission();
  channel.value = await getChannel(channelId);
  console.log('채널 확인', channel.value);
});
</script>

<style scoped>
.shadow-lg {
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08) !important;
}
</style>
