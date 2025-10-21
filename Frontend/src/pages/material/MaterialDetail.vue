<template>
  <v-container class="pa-6" fluid>
    <!-- 제목 -->
    <div class="text-h5 font-weight-bold mb-2">
      {{ material.title }}
    </div>
    <v-divider class="mb-4" />

    <!-- 자료 설명 -->
    <div class="mb-4">
      <p class="text-body-1">{{ material.content }}</p>
    </div>

    <!-- 첨부 파일 -->
    <div v-if="material.files && material.files.length > 0" class="mb-4">
      <h4 class="text-subtitle-1 font-weight-bold">첨부 파일</h4>
      <v-list>
        <v-list-item v-for="file in material.files" :key="file.fileId" :title="file.fileName">
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

    <!-- 교사 전용 버튼 -->
    <div v-if="channel?.roleName === 'TUTOR'" class="d-flex justify-end">
      <v-btn class="mr-2" color="primary" variant="flat" @click="goToEdit"> 수정 </v-btn>
      <v-btn color="red" variant="flat" @click="deleteDialog = true"> 삭제 </v-btn>
    </div>

    <!-- 자료 삭제 모달 -->
    <MaterialDeleteModal v-model="deleteDialog" :material="material" @confirm="confirmDelete" />
  </v-container>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getChannel } from '@/apis/channel';
import { downloadFile } from '@/apis/file';
import { deleteMaterial, getMaterial } from '@/apis/material';
import MaterialDeleteModal from './components/MaterialDeleteModal.vue';

const route = useRoute();
const router = useRouter();

const channelId = route.params.channelId;
const materialId = route.params.materialId;

const channel = ref(null);
const material = ref({ files: [] });
const deleteDialog = ref(false);

// 자료 불러오기
async function loadMaterial() {
  const data = await getMaterial(channelId, materialId);
  material.value = Array.isArray(data) ? data[0] : data;
}

function download(file) {
  console.log('파일 다운로드:', file);
  downloadFile(file.fileId, file.fileName);
}

function goToEdit() {
  router.push(`/channels/${channelId}/materials/${materialId}/edit`);
}

async function confirmDelete() {
  await deleteMaterial(channelId, materialId);
  router.push(`/channels/${channelId}/materials`);
}

onMounted(async () => {
  await loadMaterial();
  channel.value = await getChannel(channelId);
});
</script>

<style scoped>
.shadow-lg {
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08) !important;
}
</style>
