<template>
  <!-- 모달에서도 보기 좋게 카드로 감싸기 -->
  <v-card class="pa-6 rounded-xl shadow-lg" max-width="600" width="100%">
    <v-card-title class="text-h6 font-weight-bold">제출 수정</v-card-title>
    <v-divider class="mb-4" />

    <v-form @submit.prevent="handleUpdate">
      <!-- 제목 -->
      <v-text-field
        v-model="form.title"
        class="mb-4"
        density="comfortable"
        label="제출 제목"
        required
        variant="outlined"
      />

      <!-- 설명 -->
      <v-textarea
        v-model="form.description"
        class="mb-4"
        label="설명"
        rows="3"
        variant="outlined"
      />

      <!-- 기존 파일 -->
      <div v-if="existingFiles.length > 0" class="mb-4">
        <h4 class="text-subtitle-1 font-weight-bold">기존 파일</h4>
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

      <!-- 새 파일 -->
      <v-file-input
        v-model="newFiles"
        accept=".pdf,.doc,.docx,.png,.jpg,.jpeg,.zip"
        class="mb-4"
        label="새 파일 추가"
        multiple
        prepend-icon="mdi-paperclip"
        variant="outlined"
      />

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
        <v-btn class="mr-2" variant="text" @click="onCancel">취소</v-btn>
        <v-btn color="primary" type="submit">수정</v-btn>
      </div>
    </v-form>
  </v-card>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { deleteFile, uploadFiles } from '@/apis/file';
import { getSubmission, updateSubmission } from '@/apis/submission';

const props = defineProps({
  // 모달로 쓰면 true, 단독 페이지면 false(기본)
  inDialog: { type: Boolean, default: false },
  // 모달로 열 때 부모가 넘겨주는 기존 데이터(선택)
  initialData: { type: Object, default: null },
});

const emit = defineEmits(['submitted', 'close']);

const route = useRoute();
const router = useRouter();

const channelId = route.params.channelId;
const assignmentId = route.params.assignmentId;

// ✅ submissionId를 안전하게 계산 (라우트 → props 순서)
const submissionId = computed(() => {
  return route.params.submissionId || props?.initialData?.submissionId || null;
});

const form = ref({
  title: '',
  description: '',
});

// 기존 파일 목록
const existingFiles = ref([]);
// 새로 추가할 파일 목록
const newFiles = ref([]);

// 페이지 진입 시 제출 데이터 불러오기
// 초기 로딩
onMounted(async () => {
  if (props.initialData) {
    // 부모가 넘겨준 데이터로 세팅 (모달 케이스)
    form.value.title = props.initialData.title || '';
    form.value.description = props.initialData.description || '';
    existingFiles.value = props.initialData.files || [];
  } else {
    // 단독 페이지 진입: API에서 조회요:
    const data = await getSubmission(channelId, assignmentId);
    form.value.title = data.title;
    form.value.description = data.description;
    existingFiles.value = data.files || [];
  }
});

// 제출 수정
async function handleUpdate() {
  // 1. 새 파일 업로드
  let newFileIds = [];
  if (newFiles.value.length > 0) {
    const uploaded = await uploadFiles('SUBMISSION', newFiles.value);
    newFileIds = uploaded.map(f => f.fileId);
  }

  // 2. 기존 파일 중 삭제되지 않은 것만 유지
  const keptFileIds = existingFiles.value.map(f => f.fileId);

  // 3. payload 구성
  const payload = {
    title: form.value.title,
    description: form.value.description,
    fileIds: [...keptFileIds, ...newFileIds],
  };

  // 4) API 호출 (submissionId 반드시 존재해야 함)
  const subId = submissionId.value;
  if (!subId) {
    console.error('submissionId가 없습니다. (라우트 또는 props.initialData 확인)');
    return;
  }
  const updated = await updateSubmission(channelId, assignmentId, subId, payload);

  // 5) 완료 처리
  if (props.inDialog) {
    emit('submitted', updated); // 부모가 리스트/상세 갱신
    emit('close'); // 모달 닫기
  } else {
    // 단독 페이지면 상세로 이동
    router.push(
      `/channels/${channelId}/assignments/${assignmentId}/submissions/${updated.submissionId}`,
    );
  }
}

// 취소: 모달이면 닫고, 페이지면 뒤로가기
function onCancel() {
  if (props.inDialog) {
    emit('close');
  } else {
    router.push(
      `/channels/${channelId}/assignments/${assignmentId}/submissions/${submissionId.value}`,
    );
  }
}

// 파일 제거
function removeExistingFile(index) {
  const file = existingFiles.value[index];
  // 서버에서도 삭제 처리 (선택)
  deleteFile(file.fileId);
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
</style>
