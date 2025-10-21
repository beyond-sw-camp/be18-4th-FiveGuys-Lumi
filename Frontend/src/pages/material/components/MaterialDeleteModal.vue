<template>
  <v-dialog
    max-width="400"
    :model-value="modelValue"
    @update:model-value="$emit('update:modelValue', $event)"
  >
    <v-card>
      <v-card-title>자료 삭제</v-card-title>
      <v-card-text> "{{ props.material?.title }}" 자료를 정말 삭제하시겠습니까? </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn text @click="$emit('update:modelValue', false)">취소</v-btn>
        <v-btn color="red" @click="confirmDelete">삭제하기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
const props = defineProps({
  material: { type: Object, default: null },
  modelValue: { type: Boolean, required: true },
});

const emit = defineEmits(['update:modelValue', 'confirm']);

function confirmDelete() {
  emit('confirm', props.material);
  emit('update:modelValue', false);
}
</script>

<style lang="scss" scoped></style>
