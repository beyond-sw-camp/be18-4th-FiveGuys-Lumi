<template>
  <v-list-item class="assignment-item" @click="$emit('click', assignment)">
    <div class="d-flex justify-space-between align-center w-100">
      <div>
        <div class="font-weight-bold">{{ assignment.title }}</div>
        <small class="text-grey">마감일: {{ assignment.deadlineAt }}</small>
        <div class="text-caption mt-1">
          생성: {{ assignment.createdAt }} / 수정: {{ assignment.updatedAt }}
        </div>
      </div>

      <div class="d-flex align-center">
        <v-chip class="mr-2" :color="assignment.submission ? 'success' : 'warning'" size="small">
          {{ assignment.submission ? '제출 완료' : '미제출' }}
        </v-chip>
        <v-chip class="mr-2" :color="assignment.evaluation ? 'info' : 'grey'" size="small">
          {{ assignment.evaluation ? '평가 있음' : '평가 없음' }}
        </v-chip>

        <!-- 역할별 버튼 -->
        <template v-if="channel?.roleName === 'TUTOR'">
          <v-btn
            class="mr-2"
            color="primary"
            size="small"
            variant="flat"
            @click.stop="$emit('edit', assignment)"
            >수정</v-btn
          >
          <v-btn color="red" size="small" variant="flat" @click.stop="$emit('delete', assignment)"
            >삭제</v-btn
          >
        </template>
        <v-btn
          v-else-if="channel?.roleName === 'STUDENT' && !assignment.submission"
          color="primary"
          size="small"
          variant="flat"
        >
          제출
        </v-btn>
      </div>
    </div>
  </v-list-item>
</template>

<script setup>
defineProps({
  assignment: Object,
  channel: { type: Object, default: () => ({}) },
});
defineEmits(['edit', 'delete', 'click']);
</script>

<style lang="scss" scoped>
.assignment-item {
  border-bottom: 1px solid #eee;
  padding: 12px 0;
}
</style>
