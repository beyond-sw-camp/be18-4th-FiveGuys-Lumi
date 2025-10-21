<template>
  <v-container class="pa-6" fluid>
    <h2 class="text-h6 font-weight-bold mb-4">제출물</h2>
    <v-divider class="mb-4" />

    <!-- 제출이 없으면 생성 -->
    <SubmissionCreate
      v-if="!submission && channel?.roleName === 'STUDENT'"
      mode="create"
      @submitted="loadSubmission"
    />

    <!-- 제출이 있을 때 -->
    <div v-else-if="submission">
      <!-- 제목 / 설명 -->
      <div class="mb-4">
        <div class="font-weight-bold text-body-1">{{ submission.title }}</div>
        <div class="text-body-2 text-grey">{{ submission.description }}</div>
      </div>

      <!-- 파일 목록 -->
      <div v-if="submission.files && submission.files.length > 0" class="mb-4">
        <h4 class="text-subtitle-1 font-weight-bold">첨부 파일</h4>
        <v-list>
          <v-list-item v-for="file in submission.files" :key="file.fileId" :title="file.fileName">
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

      <v-divider class="my-4" />

      <!-- 역할별 버튼 -->
      <div class="d-flex justify-end">
        <!-- 교사 -->
        <template v-if="channel?.roleName === 'TUTOR'">
          <v-btn
            v-if="!evaluation && canWriteEvaluation"
            color="primary"
            @click="openEval('create')"
          >
            평가 작성
          </v-btn>
        </template>

        <!-- 학생 -->
        <template v-else-if="channel?.roleName === 'STUDENT'">
          <v-btn class="mr-2" color="primary" @click="openEdit">수정</v-btn>
          <v-btn color="red" @click="openDeleteModal">삭제</v-btn>
        </template>
      </div>
    </div>

    <!-- 수정 모달 -->
    <v-dialog v-model="editDialog" max-width="640">
      <SubmissionUpdate
        v-if="submission"
        class="submissionCreateClass"
        :in-dialog="true"
        :initial-data="submission"
        mode="edit"
        @close="editDialog = false"
        @submitted="
          updated => {
            submission = updated;
          }
        "
      />
    </v-dialog>

    <!-- 삭제 모달 -->
    <v-dialog v-model="deleteDialog" max-width="400">
      <v-card>
        <v-card-title>제출 삭제</v-card-title>
        <v-card-text> "{{ submission?.title }}" 제출을 정말 삭제하시겠습니까? </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn text @click="deleteDialog = false">취소</v-btn>
          <v-btn color="red" @click="handleDelete">삭제하기</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-divider class="my-6" />
    <!-- 평가 영역 -->
    <h3 class="text-h6 font-weight-bold mb-4">평가</h3>

    <!-- 평가 없음 -->
    <div v-if="!evaluation" class="text-grey mb-4">아직 평가가 없습니다.</div>

    <!-- 평가 있음 -->
    <div v-else class="mb-4">
      <v-chip class="mb-4" color="info" size="small"> 점수: {{ evaluation.grade }} </v-chip>
      <div class="text-body-2 mb-2">
        {{ evaluation.feedback || '피드백 없음' }}
      </div>

      <div v-if="canWriteEvaluation" class="d-flex">
        <v-btn class="mr-2" color="#ffe8ff" @click="openEval('edit')">수정</v-btn>
        <v-btn color="red" @click="confirmEvalDelete = true">삭제</v-btn>
      </div>
    </div>

    <!-- 평가 작성/수정 모달 -->
    <EvaluationModal
      v-model="evalDialog"
      :evaluation="evaluation"
      :mode="evalMode"
      @submit="onEvalSubmit"
    />

    <!-- 평가 삭제 확인 -->
    <v-dialog v-model="confirmEvalDelete" max-width="400">
      <v-card>
        <v-card-title>평가 삭제</v-card-title>
        <v-card-text>정말 평가를 삭제하시겠습니까?</v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn text @click="confirmEvalDelete = false">취소</v-btn>
          <v-btn color="red" @click="onEvalDelete">삭제하기</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getChannel } from '@/apis/channel';
import {
  createEvaluation,
  deleteEvaluation,
  getEvaluation,
  updateEvaluation,
} from '@/apis/evaluation';
import { downloadFile } from '@/apis/file';
import { deleteSubmission, getSubmission, updateSubmission } from '@/apis/submission';
import EvaluationModal from '@/pages/evaluation/EvaluationModal.vue';
import SubmissionCreate from './SubmissionCreate.vue';
import SubmissionUpdate from './SubmissionUpdate.vue';

const route = useRoute();
const router = useRouter();

const channelId = route.params.channelId;
const assignmentId = route.params.assignmentId;

const channel = ref(null);
const submission = ref(null);
const evaluation = ref(null);

const editDialog = ref(false);
const deleteDialog = ref(false);
const evalDialog = ref(false);
const evalMode = ref('create');
const confirmEvalDelete = ref(false);

const canWriteEvaluation = computed(() => {
  const isTutor = channel.value?.roleName === 'TUTOR';
  // 지금은 단순히 교사만 평가 가능, 필요하다면 마감일/평가 여부 조건 추가
  return isTutor && submission.value?.submissionId;
});

async function loadSubmission() {
  try {
    const data = await getSubmission(channelId, assignmentId);
    submission.value = Array.isArray(data) ? data[0] : data;
    if (submission.value) {
      await loadEvaluation();
    }
  } catch (error) {
    console.log('제출 없음', error);
    submission.value = null;
  }
}

async function loadEvaluation() {
  try {
    evaluation.value = await getEvaluation(channelId, submission.value.submissionId);
    console.log('loadEvaluation호출시 평가 값 :', evaluation.value);
  } catch (error) {
    console.log('평가 없음', error);
    evaluation.value = null; // 평가 없음
  }
}

async function onEdited(payload) {
  // 수정 API 호출
  const updated = await updateSubmission(
    channelId,
    assignmentId,
    submission.value.submissionId,
    payload,
  );
  submission.value = updated;
  editDialog.value = false; // 모달 닫기
}

function openEdit() {
  editDialog.value = true;
}

function openEval(mode) {
  evalMode.value = mode;
  evalDialog.value = true;
}

async function onEvalSubmit(payload) {
  if (!submission.value?.submissionId) return;
  evaluation.value =
    evalMode.value === 'create'
      ? await createEvaluation(channelId, submission.value.submissionId, payload)
      : await updateEvaluation(channelId, submission.value.submissionId, payload);
}

async function onEvalDelete() {
  if (!submission.value?.submissionId) return;
  await deleteEvaluation(channelId, submission.value.submissionId);
  evaluation.value = null;
  confirmEvalDelete.value = false;
}

function openDeleteModal() {
  deleteDialog.value = true;
}

async function handleDelete() {
  await deleteSubmission(channelId, assignmentId, submission.value.submissionId);
  submission.value = null; // 삭제 후 → 다시 제출 폼 보이도록
  deleteDialog.value = false;
  evaluation.value = null;
}
function download(file) {
  downloadFile(file.fileId, file.fileName);
}

onMounted(async () => {
  loadSubmission();
  channel.value = await getChannel(channelId);
  console.log('채널 확인', channel.value);
});
</script>

<style scoped>
.submission-item {
  border-bottom: 1px solid #eee;
  padding: 12px 0;
}

.submissionCreateClass {
  background-color: aliceblue;
}
</style>
