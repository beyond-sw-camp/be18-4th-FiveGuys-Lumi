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
        class="rounded-xl"
        color="primary-button-1"
        elevation="0"
        @click="goToCreate"
      >
        자료 생성하기
      </v-btn>
    </div>

    <!-- 자료 목록 -->
    <MaterialList
      :channel="channel"
      :materials="sortedMaterials"
      @click="goToDetail"
      @delete="openDeleteModal"
      @edit="goToEdit"
    />

    <!-- 삭제 모달 -->
    <MaterialDeleteModal
      v-model="deleteDialog"
      :material="selectedMaterial"
      @confirm="handleDelete"
    />
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getChannel } from '@/apis/channel';
import { deleteMaterial, getMaterials } from '@/apis/material';
import MaterialDeleteModal from './components/MaterialDeleteModal.vue';
import MaterialList from './components/MaterialList.vue';

const route = useRoute();
const router = useRouter();

const channelId = route.params.channelId;

const channel = ref(null);

// 상태
const materials = ref([]);
const sortOrder = ref('최신순'); // 정렬 옵션
const deleteDialog = ref(false);
const selectedMaterial = ref(null);

// 목록 불러오기
async function loadMaterials() {
  materials.value = await getMaterials(channelId);
}

// 정렬된 목록
const sortedMaterials = computed(() => {
  return [...materials.value].sort(
    (a, b) =>
      sortOrder.value === '최신순'
        ? new Date(b.createdAt) - new Date(a.createdAt) // 최신순
        : new Date(a.createdAt) - new Date(b.createdAt), // 오래된순
  );
});

function goToCreate() {
  router.push(`/channels/${channelId}/materials/new`);
}
function goToDetail(material) {
  router.push(`/channels/${channelId}/materials/${material.materialId}`);
}
function goToEdit(material) {
  router.push(`/channels/${channelId}/materials/${material.materialId}/edit`);
}

function openDeleteModal(material) {
  selectedMaterial.value = material;
  deleteDialog.value = true;
}
async function handleDelete(material) {
  await deleteMaterial(channelId, material.materialId);
  await loadMaterials();
  deleteDialog.value = false;
}

onMounted(async () => {
  await loadMaterials();
  channel.value = await getChannel(channelId);
});
</script>

<style lang="scss" scoped>
.material-list {
  background-color: #fdf6ff;
  border-radius: 12px;
  padding: 12px;
}
.material-item {
  border-bottom: 1px solid #eee;
  padding: 12px 0;
}
</style>
