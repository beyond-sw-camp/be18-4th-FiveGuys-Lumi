<template>
  <v-list-item class="material-item" @click="$emit('click', material)">
    <div class="d-flex justify-space-between align-center w-100">
      <div>
        <!-- 자료 제목 -->
        <div class="font-weight-bold">{{ material.title }}</div>

        <!-- 첨부 파일 -->
        <div v-if="material.fileUrl" class="text-caption mt-1">
          <v-icon class="mr-1" size="small">mdi-paperclip</v-icon>
          <a :href="material.fileUrl" target="_blank">첨부 파일</a>
        </div>

        <!-- 생성/수정일 -->
        <div class="text-caption mt-1">
          생성: {{ material.createdAt }} / 수정: {{ material.updatedAt }}
        </div>
      </div>

      <!-- TUTOR 전용 버튼 -->
      <div v-if="channel?.roleName === 'TUTOR'" class="d-flex align-center">
        <v-btn
          class="mr-2"
          color="primary"
          size="small"
          variant="flat"
          @click.stop="$emit('edit', material)"
        >
          수정
        </v-btn>
        <v-btn color="red" size="small" variant="flat" @click.stop="$emit('delete', material)">
          삭제
        </v-btn>
      </div>
    </div>
  </v-list-item>
</template>

<script setup>
defineProps({
  material: Object,
  channel: { type: Object, default: () => ({}) },
});
defineEmits(['edit', 'delete', 'click']);
</script>

<style lang="scss" scoped>
.material-item {
  border-bottom: 1px solid #ffe8ff;
  padding: 12px 0;
}
</style>
