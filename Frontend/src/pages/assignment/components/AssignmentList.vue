<template>
  <v-list v-if="assignments.length > 0" class="assignment-list">
    <AssignmentCard
      v-for="a in assignments"
      :key="a.assignmentId"
      :assignment="a"
      :channel="channel"
      @click="$emit('click', a)"
      @delete="$emit('delete', a)"
      @edit="$emit('edit', a)"
    />
  </v-list>

  <div v-else class="text-grey text-center pa-4">등록된 수업 과제가 없습니다.</div>
</template>

<script setup>
import AssignmentCard from './AssignmentCard.vue';

defineProps({
  assignments: Array,
  channel: { type: Object, default: () => ({}) },
});

defineEmits(['edit', 'delete', 'click']);
</script>

<style lang="scss" scoped></style>
