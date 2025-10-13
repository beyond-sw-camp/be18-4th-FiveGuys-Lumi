<template>
  <div class="submission-create mt-8">
    <h3 class="text-h6 font-weight-bold mb-4">제출하기</h3>

    <v-form @submit.prevent="handleCreate">
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

      <!-- 파일 업로드 -->
      <v-file-input
        v-model="form.files"
        accept=".pdf,.doc,.docx,.png,.jpg,.jpeg,.zip"
        class="mb-4"
        label="파일 첨부"
        multiple
        prepend-icon="mdi-paperclip"
        variant="outlined"
      />

      <!-- 파일 미리보기 -->
      <div v-if="form.files.length > 0" class="mb-4">
        <h4 class="text-subtitle-1 font-weight-bold">첨부된 파일</h4>
        <ul class="file-list">
          <li v-for="(file, idx) in form.files" :key="idx">
            {{ file.name }} ({{ (file.size / 1024).toFixed(1) }} KB)
            <v-btn class="ml-2" color="red" size="x-small" variant="text" @click="removeFile(idx)">
              제거
            </v-btn>
          </li>
        </ul>
      </div>

      <v-divider class="my-4" />

      <!-- 버튼 -->
      <div class="d-flex justify-end">
        <v-btn color="primary" type="submit">
          {{ mode === 'create' ? '제출' : '수정' }}
        </v-btn>
      </div>
    </v-form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { uploadFiles } from '@/apis/file';
import { createSubmission, updateSubmission } from '@/apis/submission';

const route = useRoute();
const router = useRouter();

const channelId = route.params.channelId;
const assignmentId = route.params.assignmentId;

const props = defineProps({
  mode: { type: String, default: 'create' }, // "create" or "edit"
  initialData: { type: Object, default: () => ({}) },
});

const form = ref({
  title: props.initialData.title || '',
  description: props.initialData.description || '',
  files: [],
});

async function handleCreate() {
  try {
    let fileIds = [];
    if (form.value.files.length > 0) {
      const uploaded = await uploadFiles('SUBMISSION', form.value.files);
      fileIds = uploaded.map(f => f.fileId);
    }
    // 실제 구현에서는 FormData로 변환 후 업로드 API 필요
    const payload = {
      title: form.value.title,
      description: form.value.description,
      fileIds, // TODO: 파일 업로드 API 붙이면 fileIds 채우기
    };

    props.mode === 'create'
      ? await createSubmission(channelId, assignmentId, payload)
      : await updateSubmission(channelId, assignmentId, props.initialData.submissionId, payload);

    // 제출 성공 시 → 상세 페이지 새로고침
    router.replace(`/channels/${channelId}/assignments/${assignmentId}`);
    router.go(0);
  } catch (error) {
    console.error('제출 생성 실패:', error);
  }
}

function removeFile(index) {
  const next = [...form.value.files];
  next.splice(index, 1);
  form.value.files = next;
}
</script>

<style scoped>
.file-list {
  padding-left: 16px;
}
</style>
