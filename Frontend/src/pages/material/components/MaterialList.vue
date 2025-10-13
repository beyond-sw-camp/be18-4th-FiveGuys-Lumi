<template>
  <v-list v-if="materials.length > 0" class="material-list">
    <MaterialCard
      v-for="m in materials"
      :key="m.materialId"
      :channel="channel"
      :material="m"
      @click="$emit('click', m)"
      @delete="$emit('delete', m)"
      @edit="$emit('edit', m)"
    />
  </v-list>

  <div v-else class="text-grey text-center pa-4">등록된 수업 자료가 없습니다.</div>
</template>

<script setup>
import MaterialCard from './MaterialCard.vue';

defineProps({
  materials: Array,
  channel: { type: Object, default: () => ({}) },
});

defineEmits(['edit', 'delete', 'click']);
</script>

<style lang="scss" scoped></style>
