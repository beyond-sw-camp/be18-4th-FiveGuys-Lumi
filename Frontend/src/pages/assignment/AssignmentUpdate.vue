<template>
  <v-container class="d-flex justify-center">
    <v-card class="pa-6 rounded-xl shadow-lg" max-width="600" width="100%">
      <v-card-title class="text-h5 font-weight-bold">과제 수정</v-card-title>
      <v-divider class="mb-4" />

      <v-form @submit.prevent="handleUpdate">
        <!-- 제목 -->
        <v-text-field
          v-model="form.title"
          class="mb-4"
          density="comfortable"
          label="과제 제목"
          required
          variant="outlined"
        />

        <!-- 설명 -->
        <v-textarea
          v-model="form.content"
          class="mb-4"
          label="과제 설명"
          rows="4"
          variant="outlined"
        />

        <!-- 마감일 -->
        <v-row>
          <v-col cols="12" md="6" sm="12">
            <v-text-field
              v-model="form.deadlineAt"
              class="mb-4"
              density="comfortable"
              label="제출 마감일"
              required
              type="datetime-local"
              variant="outlined"
            />
          </v-col>
        </v-row>

        <!-- 스위치 -->
        <v-row>
          <v-col cols="12" md="6" sm="12">
            <v-switch v-model="form.evaluation" color="primary" inset label="평가 필요 여부" />
          </v-col>
        </v-row>

        <!-- 기존 파일 목록 -->
        <div v-if="existingFiles.length > 0" class="mb-4">
          <h4 class="text-subtitle-1 font-weight-bold">기존 첨부 파일</h4>
          <ul class="file-list">
            <li v-for="(file, idx) in existingFiles" :key="file.fileId">
              {{ file.fileName }}
              <v-btn
                class="ml-2"
                color="red"
                size="x-small"
                variant="text"
                @click="removeExistingFile(idx)"
              >
                제거
              </v-btn>
            </li>
          </ul>
        </div>

        <!-- 새 파일 추가 -->
        <v-file-input
          v-model="newFiles"
          accept=".pdf,.doc,.docx,.png,.jpg,.jpeg"
          class="mb-4"
          label="파일 추가"
          multiple
          prepend-icon="mdi-paperclip"
          variant="outlined"
        />

        <!-- 새 파일 미리보기 -->
        <div v-if="newFiles.length > 0" class="mb-4">
          <h4 class="text-subtitle-1 font-weight-bold">추가된 파일</h4>
          <ul class="file-list">
            <li v-for="(file, idx) in newFiles" :key="idx">
              {{ file.name }} ({{ (file.size / 1024).toFixed(1) }} KB)
              <v-btn
                class="ml-2"
                color="red"
                size="x-small"
                variant="text"
                @click="removeNewFile(idx)"
              >
                제거
              </v-btn>
            </li>
          </ul>
        </div>

        <v-divider class="my-4" />

        <!-- 버튼 -->
        <div class="d-flex justify-end">
          <v-btn class="mr-2" variant="text" @click="goBack">취소</v-btn>
          <v-btn color="primary" type="submit">수정하기</v-btn>
        </div>
      </v-form>
    </v-card>
  </v-container>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getAssignment, updateAssignment } from '@/apis/assignment';
import { deleteFile, uploadFiles } from '@/apis/file';
const route = useRoute();
const router = useRouter();

const channelId = route.params.channelId;
const assignmentId = route.params.assignmentId;

const form = ref({
  title: '',
  content: '',
  deadlineAt: '',
  evaluation: false,
});

// 기존 파일 (API에서 불러올 예정)
const existingFiles = ref([]);
// 새로 추가할 파일
const newFiles = ref([]);

// 기존 데이터 불러오기
async function loadAssignment() {
  const data = await getAssignment(channelId, assignmentId);

  form.value = {
    title: data.title,
    content: data.content,
    // API는 "yyyy-MM-dd HH:mm:ss" → input은 "yyyy-MM-ddTHH:mm"
    deadlineAt: data.deadlineAt.replace(' ', 'T').slice(0, 16),
    evaluation: data.evaluation,
  };

  existingFiles.value = data.files || [];
}

console.log('확인', form.value);

onMounted(() => loadAssignment());

console.log('확인2', form.value);

async function handleUpdate() {
  let newFileIds = [];
  if (newFiles.value.length > 0) {
    const uploaded = await uploadFiles('ASSIGNMENT', newFiles.value);
    newFileIds = uploaded.map(f => f.fileId);
  }
  console.log('수정할 데이터:', form.value);
  console.log('삭제된 파일:', existingFiles.value); // TODO: 실제 삭제 목록 처리
  console.log('추가된 파일:', newFiles.value); // TODO: FormData로 전송

  const keptFileIds = existingFiles.value.map(f => f.fileId);

  const payload = {
    title: form.value.title,
    content: form.value.content,
    deadlineAt: form.value.deadlineAt.replace('T', ' ') + ':00',
    isEvaluation: form.value.evaluation,
    fileIds: [...keptFileIds, ...newFileIds],
  };

  await updateAssignment(channelId, assignmentId, payload);

  router.push(`/channels/${channelId}/assignments`);
}

function goBack() {
  router.push(`/channels/${channelId}/assignments`);
}

function removeExistingFile(index) {
  existingFiles.value.splice(index, 1);
}

function removeNewFile(index) {
  newFiles.value.splice(index, 1);
}
</script>
<style scoped>
.file-list {
  padding-left: 16px;
}

.shadow-lg {
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08) !important;
}
</style>
