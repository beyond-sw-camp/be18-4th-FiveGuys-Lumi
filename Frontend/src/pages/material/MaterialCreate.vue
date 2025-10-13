<template>
  <v-container class="d-flex justify-center">
    <v-card class="pa-6 rounded-xl shadow-lg" max-width="600" width="100%">
      <v-card-title class="text-h5 font-weight-bold"> 자료 등록 </v-card-title>
      <v-divider class="mb-4" />

      <v-form @submit.prevent="handleCreate">
        <!-- 제목 -->
        <v-text-field
          v-model="form.title"
          class="mb-4"
          density="comfortable"
          label="자료 제목"
          required
          variant="outlined"
        />

        <!-- 설명 -->
        <v-textarea
          v-model="form.content"
          class="mb-4"
          label="자료 설명"
          rows="4"
          variant="outlined"
        />

        <!-- 파일 업로드 -->
        <v-file-input
          v-model="form.files"
          accept=".pdf,.doc,.docx,.png,.jpg,.jpeg"
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
              <v-btn
                class="ml-2"
                color="red"
                size="x-small"
                variant="text"
                @click="removeFile(idx)"
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
          <v-btn color="primary" type="submit">등록</v-btn>
        </div>
      </v-form>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { uploadFiles } from '@/apis/file';
import { createMaterial } from '@/apis/material';

const route = useRoute();
const router = useRouter();

const channelId = route.params.channelId;

const form = ref({
  title: '',
  content: '',
  files: [],
});

// 자료 생성
async function handleCreate() {
  try {
    let fileIds = [];
    if (form.value.files.length > 0) {
      const uploaded = await uploadFiles('MATERIAL', form.value.files);
      fileIds = uploaded.map(f => f.fileId);
    }

    // API에 맞는 payload
    const payload = {
      title: form.value.title,
      content: form.value.content,
      fileIds,
    };

    await createMaterial(channelId, payload);

    router.push(`/channels/${channelId}/materials`);
  } catch (error) {
    console.error('자료 생성 실패:', error);
  }
}

function goBack() {
  router.push(`/channels/${channelId}/materials`);
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

.shadow-lg {
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08) !important;
}
</style>
