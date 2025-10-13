<template>
  <v-dialog
    :max-width="500"
    :model-value="modelValue"
    @update:model-value="emit('update:modelValue', $event)"
  >
    <v-card>
      <v-card-title>
        {{ mode === 'create' ? '채널 등록' : '채널 수정' }}
      </v-card-title>
      <v-card-text>
        <v-text-field v-model="form.name" label="채널 이름" />
        <v-text-field v-model="form.subject" label="과목" />
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn text @click="$emit('update:modelValue', false)">닫기</v-btn>
        <v-btn color="primary" @click="submit">
          {{ mode === 'create' ? '등록' : '수정' }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { reactive, watch } from 'vue';

const props = defineProps({
  modelValue: Boolean, // dialog 열림 상태
  mode: { type: String, default: 'create' }, // "create" | "edit"
  channel: { type: Object, default: () => ({}) },
});

const emit = defineEmits(['update:modelValue', 'submit']);

const form = reactive({
  name: '',
  subject: '',
  roleName: '',
});

// 채널 수정 시 초기값 세팅
watch(
  () => props.channel,
  val => {
    form.name = props.mode === 'edit' && val ? val.name : '';
    form.subject = props.mode === 'edit' && val ? val.subject : '';
    form.roleName = props.mode == 'edit' && val ? val.roleName : '';
  },
  { immediate: true },
);

function submit() {
  emit('submit', { ...form });
  emit('update:modelValue', false);
}
</script>

<style lang="scss" scoped></style>
