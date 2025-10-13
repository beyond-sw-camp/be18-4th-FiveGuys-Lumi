<!-- src/pages/evaluation/EvaluationModal.vue -->
<template>
  <v-dialog
    max-width="500"
    :model-value="modelValue"
    @update:model-value="$emit('update:modelValue', $event)"
  >
    <v-card>
      <v-card-title>{{ mode === 'create' ? '평가 작성' : '평가 수정' }}</v-card-title>
      <v-divider />
      <v-card-text>
        <v-text-field
          v-model.number="form.grade"
          class="mb-4"
          label="점수 (0~100)"
          max="100"
          min="0"
          required
          type="number"
          variant="outlined"
        />
        <v-textarea v-model="form.feedback" label="피드백" rows="3" variant="outlined" />
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn text @click="$emit('update:modelValue', false)">취소</v-btn>

        <v-btn color="primary" @click="submit">
          {{ mode === 'create' ? '등록하기' : '수정하기' }}
        </v-btn>
      </v-card-actions>
    </v-card>

    <!-- 삭제 확인 -->
    <v-dialog v-model="deleteDialog" max-width="400">
      <v-card>
        <v-card-title>평가 삭제</v-card-title>
        <v-card-text>평가를 정말 삭제하시겠습니까?</v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn text @click="deleteDialog = false">취소</v-btn>
          <v-btn color="red" @click="confirmDelete">삭제하기</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-dialog>
</template>

<script setup>
import { reactive, ref, watch } from 'vue';

const props = defineProps({
  modelValue: { type: Boolean, required: true },
  mode: { type: String, default: 'create' }, // create | edit
  evaluation: { type: Object, default: null }, // edit일 때 초기값
});

const emit = defineEmits(['update:modelValue', 'submit', 'delete']);

const form = reactive({ grade: '', feedback: '' });
const deleteDialog = ref(false);

watch(
  () => props.evaluation,
  val => {
    if (props.mode === 'edit' && val) {
      form.grade = val.grade ?? '';
      form.feedback = val.feedback ?? '';
    } else {
      form.grade = '';
      form.feedback = '';
    }
  },
  { immediate: true },
);

function submit() {
  // 간단 검증
  const g = Number(form.grade);
  if (Number.isNaN(g) || g < 0 || g > 100) return;
  emit('submit', { grade: g, feedback: form.feedback });
  emit('update:modelValue', false);
}

function confirmDelete() {
  emit('delete');
  deleteDialog.value = false;
  emit('update:modelValue', false);
}
</script>
